
package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class HRMService {
    private final EmployeeRepository repository;

    public HRMService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public double getAverageSalary() {
        return repository.findAll()
                .stream()
                .mapToDouble(e -> e.getSalary().doubleValue())
                .average()
                .orElse(0.0);
    }

    public Optional<Employee> getHighestPaidEmployee() {
        return repository.findAll()
                .stream()
                .max(Comparator.comparingDouble(e -> e.getSalary().doubleValue()));
    }

    public List<Employee> filterByPosition(String position) {
        return filterByPosition(position, false);
    }

    public List<Employee> filterByPosition(String position, boolean matchExact) {
        if (position == null) return List.of();
        if (matchExact) {
            return repository.findAll()
                    .stream()
                    .filter(e -> position.equals(e.getPosition()))
                    .collect(Collectors.toList());
        } else {
            String posLower = position.toLowerCase(Locale.ROOT);
            return repository.findAll()
                    .stream()
                    .filter(e -> e.getPosition() != null && e.getPosition().toLowerCase(Locale.ROOT).contains(posLower))
                    .collect(Collectors.toList());
        }
    }

    public String getAllEmployees() {
        return repository.findAll()
                .stream()
                .map(e -> String.format("%d - %s (%s) - %s",
                        e.getId(),
                        e.getName(),
                        e.getPosition() == null ? "-" : e.getPosition(),
                        e.getSalary()))
                .collect(Collectors.joining("\n"));
    }

    // Добавленные методы
    public void addEmployee(Employee employee) {
        repository.save(employee);
    }

    public boolean deleteEmployee(Long id) {
        return repository.delete(id);
    }

    public Optional<Employee> findById(Long id) {
        if (id == null) return Optional.empty();
        return repository.findAll().stream()
                .filter(e -> id.equals(e.getId()))
                .findFirst();
    }
}
