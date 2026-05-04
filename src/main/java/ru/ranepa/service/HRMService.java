package ru.ranepa.service;

import ru.ranepa.model.Employee;
import ru.ranepa.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HRMService {
    private final EmployeeRepository employeeRepository; //репозиторий для работы с БД
    // внедрение репозитория
    @Autowired
    public HRMService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // добавление нового сотрудника
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // список всех сотрудников
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // сортировка по дате приёма
    public List<Employee> getAllEmployeesSortedByHireDate() {
        List<Employee> employees = employeeRepository.findAll();
        employees.sort((e1, e2) -> e1.getHireDate().compareTo(e2.getHireDate()));
        return employees;
    }

    // поиск по id
    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    // удаление сотрудника
    public boolean deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            return false;
        }
        employeeRepository.deleteById(id);
        return true;
    }

    // средняя ЗП по компании
    public double getAverageSalary() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return 0.0;
        }
        return employees.stream()
                .mapToDouble(e -> e.getSalary().doubleValue())
                .average()
                .orElse(0.0);
    }

    // макс. ЗП
    public Optional<Employee> findHighestPaidEmployee() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            return Optional.empty();
        }
        return employees.stream()
                .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()));
    }

    // фильтр по должности
    public List<Employee> filterByPosition(String position) {
        return employeeRepository.findByPosition(position);
    }

    // фильтр по ЗП
    public List<Employee> filterByMinSalary(BigDecimal minSalary) {
        return employeeRepository.findBySalaryGreaterThanEqual(minSalary);
    }
}