package ru.ranepa.repository;
import ru.ranepa.model.Employee;
import java.util.HashMap;
import java.util.List;
public class EmployeeRepository {
    private HashMap<Long, Employee> employees = new HashMap<>();

    public boolean save (Employee employee) {
        long id = employee.getId();
        employees.put(id, employee);
        return false;
    }

    public List<Employee> findAll() {
        return employees.values()
                .stream()
                .toList();
    }

    public Employee findById(long id) {
        if (!employees.containsKey(id)){
            throw new IllegalArgumentException("Такого сотрудника нет");
        }
        return employees.get(id);
    }
    public boolean delete(Long id) {
        if (!employees.containsKey(id)) {
            System.out.println("Такого сотрудника нет");
            return false;
        }
        employees.remove(id);
        return true;
    }
}
