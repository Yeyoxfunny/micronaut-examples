package com.smorales.dynamodb.controller;

import com.smorales.dynamodb.entity.Employee;
import com.smorales.dynamodb.repository.EmployeeRepository;
import com.smorales.dynamodb.repository.PolicyCounterRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/employees")
@RestController
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repository;
    private final PolicyCounterRepository policyCounterRepository;

    @PostMapping
    public Employee save(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") String id, @RequestBody Employee employee) {
        repository.update(id, employee);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        repository.delete(id);
    }

    @GetMapping("/counter/{policyId}/increment")
    public Map<Object, Object> increment(@PathVariable("policyId") String policyId) {
        var count = policyCounterRepository.increment(policyId);
        return Map.of("cont", count);
    }

    @GetMapping("/counter/{policyId}")
    public Map<Object, Object> getPolicyCount(@PathVariable("policyId") String policyId) {
        var count = policyCounterRepository.get(policyId);
        return Map.of("cont", count);
    }

}
