package com.ampta.controller;

import com.ampta.dto.ApiResponse;
import com.ampta.dto.EmployeeRequest;
import com.ampta.dto.EmployeeResponse;
import com.ampta.service.EmployeeService;
import com.ampta.utils.Endpoints;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Endpoints.V1_EMPLOYEES)
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createEmployee(@RequestBody EmployeeRequest request){
        log.info("REST request to create employee with email: {}", request.getEmail());
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Employee created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllEmployees(){
        log.info("REST request to get all employees");
        List<EmployeeResponse> response = employeeService.getAllEmployees();
        return ResponseEntity.ok(new ApiResponse<>(true, "Employees retrieved successfully", response));
    }

    @GetMapping("/sort")
    public ResponseEntity<ApiResponse<?>> sortEmployees(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "7") Integer size,
            @RequestParam(defaultValue = "salary") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        log.info("REST request to sort employees in order by pageable");
        var response = employeeService.getEmployeeInOrder(page, size, sortBy, direction);
        return ResponseEntity.ok(new ApiResponse<>(true, "Sorted employees retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getEmployeeById(@PathVariable Long id){
        log.info("REST request to get employee by ID: {}", id);
        EmployeeResponse response = employeeService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest request){
        log.info("REST request to update employee with ID: {}", id);
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteEmployee(@PathVariable Long id){
        log.info("REST request to delete employee with ID: {}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Employee deleted successfully", null));
    }

}
