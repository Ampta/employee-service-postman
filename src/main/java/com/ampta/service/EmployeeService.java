package com.ampta.service;

import com.ampta.dto.EmployeeRequest;
import com.ampta.dto.EmployeeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getById(Long employeeId);
    EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request);
    void deleteEmployee(Long id);
    Page<EmployeeResponse> getEmployeeInOrder(int page, int size, String sortBy, String direction);
}
