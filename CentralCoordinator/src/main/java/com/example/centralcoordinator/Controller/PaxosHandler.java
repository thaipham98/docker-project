package com.example.centralcoordinator.Controller;

import com.example.centralcoordinator.Configuration.ApplicationProperties;
import com.example.centralcoordinator.Resource.MyLogger;
import com.example.centralcoordinator.model.ForwardRequestRepr;
import com.example.centralcoordinator.model.PaxosResponse;
import com.example.centralcoordinator.model.Promise;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
/**
 * This class handles the paxos protocol for the central coordinator
 */
public class PaxosHandler {
    private List<Integer> nodePorts;
    private List<String> nodeHostnames;
    @Value("${appconfig.masterNodePort}")
    private int masterPort;
    @Value("${appconfig.masterNodeHostName}")
    private String masterHostname;
    private int port;
    private String hostname;
    private Long currentProposal;
    private ForwardRequestRepr mockCurrentValue; // current request to send with body for paxos
    private int numTrials;
    // fo rlogging events
    private static final Logger myLogger = LogManager.getLogger(PaxosHandler.class);


    public PaxosHandler(ApplicationProperties props) {
        this.nodePorts = props.getNodePorts();
        this.nodeHostnames = props.getNodeHostnames();
        this.port = 8080;
        this.hostname = "localhost";
        this.currentProposal = 0L;
        this.mockCurrentValue = null;
        this.numTrials = 0;
    }

    /**
     * This method implements Paxos protocol with all the nodes in the cluster
     * Acceptors are the slove nodes in the cluster
     * Learners are the master node
     * @param request: Incoming request from the client
     * @param requestBody: Body of the incoming request
     * @return ResponseEntity<String>: Response to be sent back to the client
     */
    public synchronized ResponseEntity<String> handleRequest(HttpServletRequest request, String requestBody) {
        myLogger.info("Paxos handler: send to ports: " + this.nodePorts);
        try {
            if (numTrials > 3) {
                numTrials = 0;
                return ResponseEntity.status(500).body("Server Error. Max number of trials reached for Paxos. " +
                        "The majority of server replicas are not prepared or accepted");
            }

            // if get request, call from master db
            if (request.getMethod().equals("GET")) {
                ForwardRequestRepr forwardRequest = new ForwardRequestRepr(request, requestBody);
                ResponseEntity<String> response = consensusReached(forwardRequest);
                return response;
            }

            // write request, do Paxos
            String currentProposalString = new SimpleDateFormat("MMddHHmmssSSS").format(new Date());
            currentProposal = Long.parseLong(currentProposalString);

            boolean isPrepared = sendPrepare(currentProposal);

            if (!isPrepared) {
                numTrials++;
                return handleRequest(request, requestBody);
//                return ResponseEntity.status(500).body("The majority of server replicas are not prepared");
            }

            if (this.mockCurrentValue == null) {
                this.mockCurrentValue = new ForwardRequestRepr(request, requestBody);
            }

            myLogger.info("===== From paxos handler: Majority of replicas are prepared. Current proposal: " + currentProposal + "Proceed to accept phase");

            //paxos accept phase
            boolean isAccepted = sendPropose(currentProposal, this.mockCurrentValue);

            if (!isAccepted) {
//            numTrials++;
//            return handleRequest(request, requestBody);
                return ResponseEntity.status(500).body("The majority of server replicas are not accepted");
            }

            myLogger.info("===== From paxos handler: Majority of replicas are accepted. Current proposal: " + currentProposal + "Value acccepted:" + this.mockCurrentValue + "Proceed to decide phase");

            //consensus is reached
            int numDecide = this.sendDecide();
            myLogger.info("===== From paxos handler: Consensus reached. numDecide = " + numDecide);
            ResponseEntity<String> response = consensusReached(this.mockCurrentValue);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server Error " + e.getMessage());
        }
    }

    /**
     * This method sends a prepare request to all the nodes in the cluster
     * @param mockCurrentValue: Represents the incoming request from the client
     * @return ResponseEntity<String>: Response to be sent back to the client
     */
    public ResponseEntity<String> consensusReached(ForwardRequestRepr mockCurrentValue) {
        // print the ccurrentValue
        myLogger.info("==== From paxos handler: consensusReached mockCurrentValue:" + mockCurrentValue);

        // Add info for master node, this is the learner
        Integer backupNodePort = this.masterPort;
        String backupHostName = this.masterHostname;
        List<Integer> nodePortsWithBackup = new ArrayList<>(); //nodePorts.stream().collect(Collectors.toList());
        nodePortsWithBackup.add(backupNodePort);
        List<String> hostnamesWithBackup = new ArrayList<>(); //nodeHostnames.stream().collect(Collectors.toList());
        hostnamesWithBackup.add(backupHostName);
        myLogger.info("Send request to all servers: nodesWithBackup = " + nodePortsWithBackup);

        // route request to correct server controller, or server request
        ResponseEntity<String> result = null;
        HttpMethod method = HttpMethod.valueOf(mockCurrentValue.getMethod());
        String path = mockCurrentValue.getRequestURI();
        String queryString = mockCurrentValue.getQueryString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        myLogger.info(">>>>>> Sending to master at: " +backupHostName + ":" + backupNodePort + " <<<<<");
        for (int i = 0; i < nodePortsWithBackup.size(); i++) {
            //send request to each node via HTTP
            String base_url = "http://" + hostnamesWithBackup.get(i) + ":" + nodePortsWithBackup.get(i);

            String forwardUrl = base_url + path + "?" + queryString;

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Object> requestEntity = new HttpEntity<>(mockCurrentValue.getBody(), headers); // body, headers
            try{
                ResponseEntity<String> serverResponse = restTemplate.exchange(forwardUrl, method, requestEntity, String.class);
                // print the result
                myLogger.info("==== From paxos handler: From server " + i + "Response:\n" + serverResponse);
                result = serverResponse;
            } catch (ResourceAccessException e){
                // server not available, just print message and skip
                myLogger.info("Error sending ACTUAL request to server " + hostnamesWithBackup.get(i) + ":" + nodePortsWithBackup.get(i));
                myLogger.info(e.getMessage());
            } catch (HttpClientErrorException e){
                // malformed request, return error. All server will response the same anyway
                result = ResponseEntity
                        .status(e.getStatusCode())
                        .headers(e.getResponseHeaders())
                        .body(e.getResponseBodyAsString());
            } catch (Exception e){
                // any other exception, return 500 error.
                myLogger.error("Error sending ACTUAL request to server: " + hostnamesWithBackup.get(i) + ":" + nodePortsWithBackup.get(i) + "\n" + e.getMessage());
                myLogger.error(e.getMessage());
                result = ResponseEntity.status(500).body(e.getMessage());
            }
        }

        this.mockCurrentValue = null;
        this.numTrials = 0;
        return result; //ResponseEntity.status(200).body("Consensus Reached");
    }

    /**
     * Send decide request to all nodes
     * @return
     */
    public int sendDecide(){
        // assume once accept succeeds, decide succeeds
        // send decide to all nodes
        String decideUrl;
        int numDecided = 0;
        for (int i = 0; i < nodePorts.size(); i++){
            decideUrl = "http://" + nodeHostnames.get(i) + ":" + nodePorts.get(i) + "/decide";
            myLogger.info("decideUrl:" + decideUrl);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //create request body
            HttpEntity<ForwardRequestRepr> requestEntity = new HttpEntity<>(null, headers); // body and header
            try {
                ResponseEntity<String> response = restTemplate.exchange(decideUrl, HttpMethod.POST, requestEntity, String.class);
                if (response.getStatusCode().equals(HttpStatus.OK)){
                    numDecided++;
                }
            } catch (Exception e) {
                myLogger.info("Error sending decide request to server: " + this.nodeHostnames.get(i) + ":" + this.nodePorts.get(i) + "\n" + e.getMessage());
                myLogger.info(e.getMessage());
            }
        }
        return numDecided;
    }

    /**
     * Send propose to all nodes - phase 2 of Paxos
     * @param currentProposal
     * @param mockCurrentValue
     * @return true if majority of nodes accept the proposal
     */
    public boolean sendPropose(Long currentProposal, ForwardRequestRepr mockCurrentValue) {
        myLogger.info("sendPropose with HTTP request:\n" + mockCurrentValue);
        int numAccepted = 0;
        ForwardRequestRepr valueToSend = mockCurrentValue;
        myLogger.info("sendPropose with ForwardRequestRepr:\n" + valueToSend);
        String acceptUrl;

        // send to accept endpoint of Paxos Controller on server. To make server replica aware of this request.
        for (int i = 0; i < nodePorts.size(); i++) {
            acceptUrl = "http://" + nodeHostnames.get(i) + ":" + nodePorts.get(i) + "/accept?proposalId=" + currentProposal;
            myLogger.info("acceptUrl:" + acceptUrl);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //create request body
            HttpEntity<ForwardRequestRepr> requestEntity = new HttpEntity<>(valueToSend, headers); // body and header
            try{
                ResponseEntity<String> response = restTemplate.exchange(acceptUrl, HttpMethod.POST, requestEntity, String.class);
                myLogger.info("Received Paxos Response from server:");
                // extracting response value
                PaxosResponse paxosResponse = this.parsePaxosResponse(response);
                if (paxosResponse != null) {
                    // extract promise from response
                    Promise promise = paxosResponse.getData();
                    if (promise != null && promise.isAccepted()) {
                        numAccepted++;
                    }
                }
            } catch (Exception e) {
                myLogger.error("Error sending propose request to server: " + this.nodeHostnames.get(i) + ":" + this.nodePorts.get(i) + "\n" + e.getMessage());
                myLogger.error(e.getMessage());
            }
        }

        if (numAccepted <= nodePorts.size() / 2) {
            return false;
        }
        myLogger.info("==== From paxos handler: sendPropose numAccepted:" + numAccepted);

        return true;
    }

    /**
     * This method sends a prepare request to all the nodes in the cluster
     * @param currentProposal: Proposal number
     * @return boolean: True if majority of the nodes are prepared, false otherwise
     */
    public boolean sendPrepare(Long currentProposal) {
        int numPrepared = 0;
        ForwardRequestRepr valueToSend = null;
        String prepareUrl;

        for (int i = 0; i < nodePorts.size(); i++) {
            prepareUrl = "http://" + nodeHostnames.get(i) + ":" + nodePorts.get(i) + "/prepare?proposalId=" + currentProposal;
            myLogger.info("prepareUrl:" + prepareUrl);
            // send to prepare endpoint of Paxos Controller on server
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //create request body
            HttpEntity<ForwardRequestRepr> requestEntity = new HttpEntity<>(null, headers); // in prepare phase, we dont send value

            try{
                ResponseEntity<String> response = restTemplate.exchange(prepareUrl, HttpMethod.POST, requestEntity, String.class);
                // extracting response value
                PaxosResponse paxosResponse = this.parsePaxosResponse(response);
                if (paxosResponse != null) {
                    // extract promise from response
                    Promise promise = paxosResponse.getData();
                    if (promise != null && promise.isPrepared()) {
                        numPrepared++;
                    }
                    if (promise != null && promise.getAcceptedValue() != null) {
                        valueToSend = promise.getAcceptedValue();
                    }
                }

            } catch (Exception e){
                // eg. server not available
                myLogger.error("Exception in sendPrepare to server: " + this.nodeHostnames.get(i) + ":" + this.nodePorts.get(i));
                myLogger.error(e.getMessage());
            }

        }

        myLogger.info("===From sendPrepare: proposalId = " + currentProposal + " numPrepared: " + numPrepared + "===");

        if (numPrepared <= nodePorts.size() / 2) {
            return false;
        }

        if (valueToSend != null) {
            mockCurrentValue = valueToSend;
        }

        return true;
    }
    // =========== Helper functions to pass request to other nodes and parse Response ===========

    /**
     * This method parses the response received from the Paxos Controller
     * @param response: Response received from Paxos Controller from Server Replica
     * @return: Parsed response object from Json formatted string
     */
    private PaxosResponse parsePaxosResponse(ResponseEntity<String> response){
        // extract response value
        // print response received from paxos controller
        myLogger.info("Response received from paxos controller:" + response.getBody());
        try{
            if (response.getStatusCode() == HttpStatus.OK) {
                //parse response body
                ObjectMapper mapper = new ObjectMapper();
                // TODO: parse nested object properly:
                PaxosResponse responseObject = mapper.readValue(response.getBody(), PaxosResponse.class);
                String message = responseObject.getMessage();
                int status = responseObject.getStatus();
                Promise data = responseObject.getData();
                // print response objects
                myLogger.info("Parsed response body received from paxos controller:");
                myLogger.info("Message: " + message);
                myLogger.info("Status: " + status);
                myLogger.info("Data: " + data);

                //do something with the parsed values
                return responseObject;
            } else {
                //handle error response
                // TODO: change this by sth else than null
                myLogger.error("Error response: " + response.getStatusCode());
                return null;
            }
        } catch (JacksonException e) {
            //handle error response
            myLogger.error("Error parsing response: " + e.getMessage());
            return null;
        }

    }
}
