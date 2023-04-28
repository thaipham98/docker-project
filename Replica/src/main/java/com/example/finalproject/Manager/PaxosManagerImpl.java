package com.example.finalproject.Manager;

import com.example.finalproject.Model.ForwardRequestRepr;
import com.example.finalproject.Model.Promise;
import com.example.finalproject.Resource.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
/**
 * PaxosManagerImpl
 */
public class PaxosManagerImpl implements PaxosManager{

    private ForwardRequestRepr acceptedValue = null;
    private Long acceptedProposal = -1L;
    private boolean alreadyAccepted = false;
    private static final Logger myLogger = LogManager.getLogger(PaxosManagerImpl.class);


    @Override
    /**
     * prepare phase: check if the current proposal is greater than the accepted proposal
     * if yes, update the accepted proposal to the current proposal
     * if no, return null
     */
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
    /**
     * accept phase: check if the current proposal is equal to the accepted proposal
     * if yes, update the accepted value to the current value
     * if no, return null
     */
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

    /**
     * decide phase: return true and reset the accepted value and proposal for a new round of Paxos
     */
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
