package com.example.finalproject.Model;

import jakarta.servlet.http.HttpServletRequest;

public class Promise {

    private Long acceptedProposal;
    private ForwardRequestRepr acceptedValue;
    private boolean accepted;
    private boolean prepared;

    public Promise(Long acceptedProposal, ForwardRequestRepr acceptedValue) {
        this.acceptedProposal = acceptedProposal;
        this.acceptedValue = acceptedValue;
        this.accepted = false;
        this.prepared = false;
    }

    public ForwardRequestRepr getAcceptedValue() {
        return acceptedValue;
    }

    public boolean getAccepted() {
        return accepted;
    }

    /**
     * @return the acceptedProposal
     */
    public Promise accept() {
        accepted = true;
        return this;
    }

    /**
     * @return the promisedProposal
     */
    public Promise promise() {
        prepared = true;
        return this;
    }

    public boolean getPrepared() {
        return prepared;
    }

    @Override
    public String toString() {
        return "Promise{" +
                "acceptedProposal=" + acceptedProposal +
                ", acceptedValue=" + acceptedValue +
                ", accepted=" + accepted +
                ", prepared=" + prepared +
                '}';
    }
}
