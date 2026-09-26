package com.ampta.dto;

public record EmployeeRequest(
    String name,
    String email,
    String password,
    String department,
    Double salary
) {}
