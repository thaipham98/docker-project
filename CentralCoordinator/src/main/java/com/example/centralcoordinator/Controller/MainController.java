package com.example.centralcoordinator.Controller;

import com.example.centralcoordinator.model.ForwardRequestRepr;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@AllArgsConstructor
@RequestMapping("/**")
/**
 * MainController
 * This class is used to gather the requests from the client.
 * The requests are then forwarded to the PaxosHandler class for further processing.
 */
public class MainController {

    private PaxosHandler paxosHandler;
    private static final Logger logger = LogManager.getLogger(MainController.class);

    /**
     * Capture the request from the client and forward it to the PaxosHandler class.
     * @param request
     * @param requestBody
     * @return
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> handleRequest(HttpServletRequest request, @RequestBody(required = false) String requestBody) {
        logger.info("MainController: Received request: " + request.getMethod() + " " + request.getRequestURL() + " " + requestBody);
        ResponseEntity<String> responseEntity = paxosHandler.handleRequest(request, requestBody);
        return responseEntity;
    }

}
