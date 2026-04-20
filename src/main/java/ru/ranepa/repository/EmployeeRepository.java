package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EmployeeRepository {
    private final Map<Long, Employee> employees = new HashMap<>(); //хранение
    private long nextId = 1;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //новый сотрудник
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setID(nextId++);
        }
        employees.put(employee.getId(), employee);
        return employee;
    }

    //вывод сотрудников
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    //поиск по id
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    //удалить
    public boolean delete(Long id) {
        if (!employees.containsKey(id)) {
            return false;
        }
        employees.remove(id);
        return true;
    }
}