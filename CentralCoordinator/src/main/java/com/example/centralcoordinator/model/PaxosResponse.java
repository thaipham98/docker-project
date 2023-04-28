package com.example.centralcoordinator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PaxosResponse: This class is used to represent the Paxos response that is sent by the replica.
 */
public class PaxosResponse {
    @JsonProperty("Status")
    private int status;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Data")
    private Promise data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Promise getData() {
        return data;
    }

    public void setData(Promise data) {
        this.data = data;
    }
}
