package com.example.finalproject.Controller;

import com.example.finalproject.Manager.PaxosManager;
import com.example.finalproject.Model.ForwardRequestRepr;
import com.example.finalproject.Model.Promise;
import com.example.finalproject.Response.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PaxosController {

    private PaxosManager paxosManager;

    @PostMapping("/prepare")
    public ResponseEntity<Object> prepare(@RequestParam Long proposalId) {
        System.out.println("Paxos Controller /prepare: Receiving proposalId: " + proposalId);
        // print out the request received:
        Object result = paxosManager.prepare(proposalId);
        return ResponseHandler.generateResponse("Success preparing!", HttpStatus.OK, result);
    }

    @PostMapping("/accept")
    public ResponseEntity<Object> accept(@RequestParam Long proposalId, @RequestBody ForwardRequestRepr forwardRequest) {
        System.out.println("Paxos Controller /accept: Receiving forwarded request:\n");
        System.out.println(forwardRequest.toString());
        Object result = paxosManager.accept(proposalId, forwardRequest);
        System.out.println("Paxos Controller /accept: Sending back response with Data (Promise):" + ((Promise)result));
        return ResponseHandler.generateResponse("Success accepting!", HttpStatus.OK, result);
    }

    @PostMapping("/decide")
    public ResponseEntity<Object> decide() {
        Object result = paxosManager.decide();
        return ResponseHandler.generateResponse("Success deciding!", HttpStatus.OK, result);
    }

    // print request received, to String mmethod
    private void printRequest(HttpServletRequest request) {
        // Get information about the request
        String method = request.getMethod();
        String contentType = request.getContentType();
        String userAgent = request.getHeader("User-Agent");
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        String pathInfo = request.getPathInfo();

        // Construct the response
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("Method: " + method + "\n");
        responseBuilder.append("Content-Type: " + contentType + "\n");
        responseBuilder.append("User-Agent: " + userAgent + "\n");
        responseBuilder.append("URL: " + url + "\n");

        if (queryString != null) {
            responseBuilder.append("Query string: " + queryString + "\n");
        }
        if (pathInfo != null) {
            responseBuilder.append("Path info: " + pathInfo + "\n");
        }
        System.out.println(responseBuilder.toString());
    }


}
