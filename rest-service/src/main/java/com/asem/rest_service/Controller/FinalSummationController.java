package com.asem.rest_service.Controller;

import com.asem.rest_service.Service.SavingSummation;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/final")
public class FinalSummationController {

    private final SavingSummation savingSummation;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FinalSummationController.class);

    public FinalSummationController(SavingSummation savingSummation) {
        this.savingSummation = savingSummation;
    }

    @GetMapping
    public int getTotal() {
        log.info("Received request to show final summation");
        int total = savingSummation.readCurrentSummation();
        return total;
    }
}