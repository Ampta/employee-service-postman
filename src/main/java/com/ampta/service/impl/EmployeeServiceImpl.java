package com.ampta.service.impl;

import com.ampta.dto.EmployeeRequest;
import com.ampta.dto.EmployeeResponse;
import com.ampta.entity.Employee;
import com.ampta.exception.ResourceAlreadyExistsException;
import com.ampta.exception.ResourceNotFoundException;
import com.ampta.repository.EmployeeRepository;
import com.ampta.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if(employeeRepository.existsByEmail(request.getEmail().trim())){
            throw new ResourceAlreadyExistsException("Email already exists: "+ request.getEmail());
        }

        Employee employee = modelMapper.map(request, Employee.class);
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeResponse.class);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return modelMapper.map(employee, EmployeeResponse.class);
    }

    @Override
    public EmployeeResponse updateEmployee(Long employeeId, EmployeeRequest request) {
        Employee exEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        String trimmedEmail = request.getEmail() != null ? request.getEmail().trim() : null;
        if (trimmedEmail != null && !trimmedEmail.equalsIgnoreCase(exEmployee.getEmail()) && employeeRepository.existsByEmail(trimmedEmail)) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        exEmployee.setName(request.getName());
        exEmployee.setEmail(trimmedEmail != null ? trimmedEmail : request.getEmail());
        exEmployee.setPassword(request.getPassword());
        exEmployee.setDepartment(request.getDepartment());
        exEmployee.setSalary(request.getSalary());

        Employee savedEmployee = employeeRepository.save(exEmployee);
        return modelMapper.map(savedEmployee, EmployeeResponse.class);
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

        return employeePage.map(employee -> modelMapper.map(employee, EmployeeResponse.class));
    }
}
