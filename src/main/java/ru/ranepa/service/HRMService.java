package ru.ranepa.service;
import ru.ranepa.repository.EmployeeRepository;
import java.util.List;
public class HRMService {
    private EmployeeRepository repository;

    public HRMService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public double getAverageSalary() {
        return repository.findAll()
                .stream()
                .mapToDouble(e -> e.getSalary().doubleValue())
                .average()
                .orElseThrow();
    }
    //переделать получше
    public String getAllEmployees() {
        List<String> strings = repository.findAll().stream()
                .map(e -> e.getName() + " ")
                .toList();
        return String.join(" ", strings);
    }
}
