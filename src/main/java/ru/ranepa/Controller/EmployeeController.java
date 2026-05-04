package ru.ranepa.Controller;

import ru.ranepa.model.Employee;
import ru.ranepa.service.HRMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final HRMService employeeService;
    @Autowired
    public EmployeeController(HRMService employeeService) {
        this.employeeService = employeeService;
    }

    // получить список всех сотрудников
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    // получить сотрудника по id
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.findById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok(employee.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // создать сотрудника
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // Сервис сохранит сотрудника и вернет его с ID
        Employee savedEmployee = employeeService.addEmployee(employee);

        // HTTP статус 201 = ресурс создан
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    // удалить сотрудника
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // фильтр по должности
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Employee>> getEmployeesByPosition(@PathVariable String position) {
        List<Employee> employees = employeeService.filterByPosition(position);
        return ResponseEntity.ok(employees);
    }

    // фильтр по зп
    @GetMapping("/salary/min")
    public ResponseEntity<List<Employee>> getEmployeesByMinSalary(@RequestParam BigDecimal salary) {
        List<Employee> employees = employeeService.filterByMinSalary(salary);
        return ResponseEntity.ok(employees);
    }

    // статистика по компании
    @GetMapping("/stats")
    public ResponseEntity<StatisticsResponse> getStatistics() {
        double averageSalary = employeeService.getAverageSalary();
        Optional<Employee> highestPaid = employeeService.findHighestPaidEmployee();
        List<Employee> allEmployees = employeeService.getAllEmployees();

        StatisticsResponse stats = new StatisticsResponse(
                averageSalary,
                highestPaid.orElse(null),
                allEmployees.size()
        );
        return ResponseEntity.ok(stats);
    }

    // вспомогательный класс для статистики
    public static class StatisticsResponse {
        private final double averageSalary;
        private final Employee highestPaidEmployee;
        private final int totalEmployees;

        public StatisticsResponse(double averageSalary, Employee highestPaidEmployee, int totalEmployees) {
            this.averageSalary = averageSalary;
            this.highestPaidEmployee = highestPaidEmployee;
            this.totalEmployees = totalEmployees;
        }

        // геттеры
        public double getAverageSalary() {
            return averageSalary;
        }
        public Employee getHighestPaidEmployee() {
            return highestPaidEmployee;
        }
        public int getTotalEmployees() {
            return totalEmployees;
        }
    }
}
