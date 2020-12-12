package tech.itpark.creditsmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.itpark.creditsmanager.manager.PaymentManager;
import tech.itpark.creditsmanager.model.Payment;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentManager manager;

    @GetMapping("/credits/payments")
    public List<Payment> getAll(){
        return manager.getAll();
    }

    @GetMapping("/credits/{id}/payments")
    public List<Payment> getAllByCredit(@PathVariable long id) {
        return manager.getByCreditId(id);
    }

    @PostMapping("/credits/payments/save")
    public Payment save(@RequestBody Payment item) {
        return manager.save(item);
    }
}
