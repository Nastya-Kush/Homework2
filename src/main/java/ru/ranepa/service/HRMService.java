package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HRMService {
    private final EmployeeRepository repository; //зависимость
    public HRMService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee addEmployee(Employee employee) {
        return repository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Сортировка по дате
    public List<Employee> getAllEmployeesSortedByHireDate() {
        return repository.findAll().stream()
                .sorted(Comparator.comparing(Employee::getHireDate))
                .collect(Collectors.toList());
    }


    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    public boolean deleteEmployee(Long id) {
        return repository.delete(id);
    }

    // среднее по зп
    public double getAverageSalary() {
        return repository.findAll().stream()
                .mapToDouble(e -> e.getSalary().doubleValue())
                .average()
                .orElse(0.0);
    }

    // макс зп
    public Optional<Employee> findHighestPaidEmployee() {
        return repository.findAll().stream()
                .max(Comparator.comparingDouble(e -> e.getSalary().doubleValue()));
    }

    // фильтр по позиции
    public List<Employee> filterByPosition(String position) {
        return repository.findAll().stream()
                .filter(e -> e.getPosition().equalsIgnoreCase(position))
                .collect(Collectors.toList());
    }
}