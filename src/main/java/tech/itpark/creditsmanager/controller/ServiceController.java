package tech.itpark.creditsmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.itpark.creditsmanager.model.Payment;
import tech.itpark.creditsmanager.service.CreditService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ServiceController {
    private final CreditService service;

    @PostMapping("/credits/{id}/calculateBy{months}")
    public List<Payment> calculatePaymentByTerm(@PathVariable long id, @PathVariable int months) {
        return service.getPaymentsByTerm(id, months);
    }
}
