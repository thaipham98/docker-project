package com.example.finalproject.Manager;

import com.example.finalproject.Model.ForwardRequestRepr;
import com.example.finalproject.Model.Promise;
import jakarta.servlet.http.HttpServletRequest;

public interface PaxosManager {

    Promise prepare(Long currentProposal);

    Promise accept(Long proposal, ForwardRequestRepr request);

    Boolean decide();
}
