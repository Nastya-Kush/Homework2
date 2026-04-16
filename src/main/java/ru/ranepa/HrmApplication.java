package ru.ranepa;
import ru.ranepa.model.Employee;
import ru.ranepa.presentation.Menu;
import ru.ranepa.repository.EmployeeRepository;
import ru.ranepa.service.HRMService;
import java.math.BigDecimal;
import java.time.LocalDate;
public class HrmApplication {
    public static void main(String[] args) {
        EmployeeRepository employeeRepository = new EmployeeRepository();
        employeeRepository.save(
                new Employee(1L, "Sasha", "dev", BigDecimal.TEN, LocalDate.now())
        );
        HRMService service = new HRMService(employeeRepository);
        Menu menu = new Menu(service);
        try {
            menu.showMenu();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
