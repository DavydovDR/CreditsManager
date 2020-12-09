package tech.itpark.creditsmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.itpark.creditsmanager.manager.CreditManager;
import tech.itpark.creditsmanager.model.Credit;


import java.util.List;

@RestController
@RequestMapping("/credits")
@RequiredArgsConstructor
public class CreditController {
    private final CreditManager manager;

    @GetMapping
    public List<Credit> getAll() {
        return manager.getAll();
    }

}
