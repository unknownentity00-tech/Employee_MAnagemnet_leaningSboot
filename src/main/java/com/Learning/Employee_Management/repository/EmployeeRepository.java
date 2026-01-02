package com.Learning.Employee_Management.repository;

import com.Learning.Employee_Management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
