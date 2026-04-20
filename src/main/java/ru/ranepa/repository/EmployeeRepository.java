package ru.ranepa.repository;

import ru.ranepa.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeRepository {
    private final Map<Long, Employee> employees = new HashMap<>();

    // Сохранение нового сотрудника или замена существующего по ID
    // Возвращает true если сотрудник успешно сохранён
    public boolean save(Employee employee) {
        if (employee == null) {
            return false;
        }
        Long id = employee.getId();
        if (id == null) {
            throw new IllegalArgumentException("У сотрудника должен быть id");
        }
        employees.put(id, employee);
        return true;
    }

    // Получение всех сотрудников (возвращаем копию списка, чтобы избежать модификации внешним кодом)
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }

    // Поиск по ID, бросаем исключение если нет
    public Employee findById(long id) {
        Employee emp = employees.get(id);
        if (emp == null) {
            throw new IllegalArgumentException("Такого сотрудника нет");
        }
        return emp;
    }

    // Удаление по ID, возвращает true если удалили, false если не найден
    public boolean delete(Long id) {
        if (id == null || !employees.containsKey(id)) {
            System.out.println("Такого сотрудника нет");
            return false;
        }
        employees.remove(id);
        return true;
    }
}