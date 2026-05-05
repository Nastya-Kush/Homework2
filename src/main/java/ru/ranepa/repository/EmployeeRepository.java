package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;


@Repository // компонент для работы с бд
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByPosition(String position); //поиск по должности
    List<Employee> findBySalaryGreaterThanEqual(BigDecimal salary); //сотрудники с зп >= указанной
}