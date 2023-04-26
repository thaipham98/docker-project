package com.example.finalproject.Manager;

import com.example.finalproject.Model.ForwardRequestRepr;
import com.example.finalproject.Model.Promise;
import com.example.finalproject.Resource.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class PaxosManagerImpl implements PaxosManager{

    private ForwardRequestRepr acceptedValue = null;
    private Long acceptedProposal = -1L;
    private boolean alreadyAccepted = false;
    private MyLogger myLogger = MyLogger.getInstance();

    @Override
    public Promise prepare(Long currentProposal) {
        myLogger.info("PAxos before prepare: Current proposal: " + currentProposal);
        if (currentProposal < this.acceptedProposal) {
            return null;
        }

        myLogger.info("Paxos prepare update proposal to Current proposal: " + currentProposal);
        this.acceptedProposal = currentProposal;

        Promise res;

        if (this.alreadyAccepted) {
            res = new Promise(this.acceptedProposal, this.acceptedValue).promise().accept();
            myLogger.info("Paxos prepare return Promise: " + res);
        }
        res = new Promise(this.acceptedProposal, null).promise();
        myLogger.info("Paxos prepare return Promise: " + res);
        return res;
    }

    @Override
    public Promise accept(Long currentProposal, ForwardRequestRepr request) {
        myLogger.info("Paxos accept: Current proposal: " + currentProposal + " Accepted proposal: " + this.acceptedProposal);
        if (currentProposal.equals(this.acceptedProposal)) {
            this.alreadyAccepted = true;
            this.acceptedProposal = currentProposal;
            this.acceptedValue = request;
            Promise res;
            res = new Promise(this.acceptedProposal, this.acceptedValue).promise().accept(); // already prepared in the prepare method, thus .promise() is not needed
            myLogger.info("Paxos accept return Promise: " + res);
            return res;
        }
        return null;
    }

    // TODO: rewrite the decide method to pass request to Service
    @Override
    public Boolean decide() {
        reset();
        return true;
    }

    private void reset() {
        this.acceptedValue = null;
        this.acceptedProposal = -1L;
        this.alreadyAccepted = false;
    }
}
