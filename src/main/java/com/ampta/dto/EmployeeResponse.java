package com.ampta.dto;

public record EmployeeResponse(
    Long id,
    String name,
    String email,
    String department,
    Double salary
) {}
