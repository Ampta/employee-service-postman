package com.ampta.service.impl;

import com.ampta.dto.EmployeeRequest;
import com.ampta.dto.EmployeeResponse;
import com.ampta.entity.Employee;
import com.ampta.exception.ResourceAlreadyExistsException;
import com.ampta.exception.ResourceNotFoundException;
import com.ampta.mapper.EmployeeMapper;
import com.ampta.repository.EmployeeRepository;
import com.ampta.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if(employeeRepository.existsByEmail(request.email().trim())){
            throw new ResourceAlreadyExistsException("Email already exists: "+ request.email());
        }

        Employee employee = employeeMapper.toEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeMapper.toResponseList(employeeRepository.findAll());
    }

    @Override
    public EmployeeResponse getById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request) {
        Employee exEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        String trimmedEmail = request.email() != null ? request.email().trim() : null;
        if (trimmedEmail != null && !trimmedEmail.equalsIgnoreCase(exEmployee.getEmail()) && employeeRepository.existsByEmail(trimmedEmail)) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.email());
        }

        exEmployee.setName(request.name());
        exEmployee.setEmail(trimmedEmail != null ? trimmedEmail : request.email());
        exEmployee.setPassword(request.password());
        exEmployee.setDepartment(request.department());
        exEmployee.setSalary(request.salary());

        Employee savedEmployee = employeeRepository.save(exEmployee);
        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employeeRepository.delete(employee);
    }

    @Override
    public Page<EmployeeResponse> getEmployeeInOrder(int page, int size, String sortBy, String direction) {
        Sort sort;

        if(direction.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }else{
            sort = Sort.by(sortBy).ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage = employeeRepository.findAll(pageable);

        return employeePage.map(employeeMapper::toResponse);
    }
}

