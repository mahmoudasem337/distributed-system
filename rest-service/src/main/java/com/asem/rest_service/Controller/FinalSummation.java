package com.asem.rest_service.Controller;

import com.asem.rest_service.Service.SavingSummation;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/final")
public class FinalSummation {

    private final SavingSummation savingSummation;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FinalSummation.class);

    public FinalSummation(SavingSummation savingSummation) {
        this.savingSummation = savingSummation;
    }

    @GetMapping
    public int getTotal() {
        log.info("Received request to show final summation");
        int total = savingSummation.readCurrentSummation();
        return total;
    }

@GetMapping("/error")
   public ResponseEntity<String> triggerError() {
       throw new RuntimeException("Test 500 error");
 }
}