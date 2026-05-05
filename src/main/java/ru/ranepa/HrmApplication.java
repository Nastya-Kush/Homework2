package ru.ranepa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import ru.ranepa.model.Employee;
import ru.ranepa.service.HRMService;
import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class HrmApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmApplication.class, args);
    }

    @Bean
    public CommandLineRunner addTestEmployees(HRMService service) {
        return args -> {
            if (service.getAllEmployees().isEmpty()) {

                Employee employee1 = new Employee(
                        "Кушнарева Анастасия Алексеевна",
                        "Директор",
                        BigDecimal.valueOf(300000),
                        LocalDate.of(2023, 6, 20)
                );
                service.addEmployee(employee1);
                System.out.println("Добавлен сотрудник: " + employee1.getName());

                Employee employee2 = new Employee(
                        "Гурецкая Дарья Дмитриевна",
                        "Бизнес аналитик",
                        BigDecimal.valueOf(120000),
                        LocalDate.of(2024, 3, 6)
                );
                service.addEmployee(employee2);
                System.out.println("Добавлен сотрудник: " + employee2.getName());
            }
        };
    }
}