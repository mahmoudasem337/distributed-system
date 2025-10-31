package com.asem.rest_service.Controller;

import com.asem.rest_service.Service.SavingSummation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/final")
public class FinalSummationController {

    private final SavingSummation savingSummation;
    public FinalSummationController(SavingSummation savingSummation) {
        this.savingSummation = savingSummation;
    }

    @GetMapping
    public int getTotal() {
        int total = savingSummation.readCurrentSummation();
        return total;
    }
}