package ru.ranepa.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmployeeResponseDto {
    public Long id;
    public String name;
    public String position;
    public BigDecimal salary;
    public LocalDate hireDate;
    public LocalDateTime createdAt;
}
