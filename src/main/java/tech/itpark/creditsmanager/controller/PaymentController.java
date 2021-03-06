package tech.itpark.creditsmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tech.itpark.creditsmanager.manager.PaymentManager;
import tech.itpark.creditsmanager.model.Payment;

import java.math.BigDecimal;
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
    public List<Payment> getAllByCredit(@PathVariable int id) {
        return manager.getByCreditId(id);
    }

    @PostMapping("/credits/payments/")
    public Payment save(@RequestBody Payment item) {
        return manager.save(item);
    }

    @PostMapping("/credits/{id}/payments")
    public List<Payment> saveAllForCredit(@RequestBody List<Payment> items) {
        return manager.saveAllForCredit(items);
    }

    @GetMapping("/credits/{id}/payments/getSchedule")
    public List<Payment> getShedule(@PathVariable int id) {
        return manager.getScheduleForTermByCreditId(id);
    }

    @GetMapping("/credits/{id}/getAllPercents")
    public BigDecimal getAmountOfPercents(@PathVariable int id) {
        return manager.getAmountOfPercents(id);
    }
}
