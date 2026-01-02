package com.Learning.Employee_Management.controller;

import com.Learning.Employee_Management.Dto.EmployeeDto;
import com.Learning.Employee_Management.entity.Employee;
import com.Learning.Employee_Management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    // 1. GET ALL EMPLOYEES
    @GetMapping
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> new EmployeeDto(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getPosition()))
                .collect(Collectors.toList());
    }

    // 2. POST (CREATE) A NEW EMPLOYEE
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) {
        // Convert DTO to Entity
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPosition(employeeDto.getPosition());

        // Save to PostgreSQL
        Employee savedEmployee = employeeRepository.save(employee);

        // Convert back to DTO for response
        EmployeeDto response = new EmployeeDto(
                savedEmployee.getId(),
                savedEmployee.getName(),
                savedEmployee.getEmail(),
                savedEmployee.getPosition()
        );

        return ResponseEntity.ok(response);
    }

}
