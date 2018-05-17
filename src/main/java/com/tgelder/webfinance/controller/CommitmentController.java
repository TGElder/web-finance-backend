package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Commitment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commitments")
public class CommitmentController extends GenericController<Commitment> {

}
