package tech.itpark.creditsmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public Credit getById(@PathVariable int id) {
        return manager.getById(id);
    }

    @PostMapping
    public Credit save(@RequestBody Credit item) {
        return manager.save(item);
    }

    @DeleteMapping("/{id}")
    public Credit removeById(@PathVariable int id) {
        return manager.removeById(id);
    }
}
