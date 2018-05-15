package com.tgelder.webfinance.controller;

import com.tgelder.webfinance.model.Reading;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readings")
public class ReadingController extends GenericController<Reading> {

}
