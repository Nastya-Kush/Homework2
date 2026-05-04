package ru.ranepa.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeRequestDto {
    @NotNull(message = "Имя должно быть заполнено")
    @NotEmpty(message = "Имя должно быть заполнено")
    public String name;
    @NotNull(message = "Должность должна быть заполнена")
    @NotEmpty(message = "Должность должна быть заполнена")
    public String position;
    @NotNull(message = "Зарплата должна быть заполнена")
    @PositiveOrZero(message = "Зарплата не должна быть отрицательной")
    public BigDecimal salary;
    @NotNull(message = "Дата должна быть заполнена")
    public LocalDate hireDate;
}
