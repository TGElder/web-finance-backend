package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Transfer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransferController extends GenericGetPostController<Transfer> {

}
