package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController extends GenericGetPostController<Account> {


}
