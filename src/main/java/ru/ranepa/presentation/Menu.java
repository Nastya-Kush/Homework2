package ru.ranepa.presentation;

import ru.ranepa.HrmApplication;
import ru.ranepa.model.Employee;
import ru.ranepa.service.HRMService;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final HRMService service;

    public static void main(String[] args) {
        // Устанавливаем кодировку вывода "UTF-8".
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.setOut(out);
        HrmApplication app = new HrmApplication();
    }

    public Menu(HRMService service) {
        this.service = service;
    }

    public void showMenu() {
        while (true) {
            System.out.println();
            System.out.println("1 - Show all employees");
            System.out.println("2 - Add new employee");
            System.out.println("3 - Delete employee by ID");
            System.out.println("4 - Find employee by ID");
            System.out.println("5 - Statistics (average salary, top manager)");
            System.out.println("6 - Exit");
            System.out.print("Enter option: ");

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Please enter a command");
                continue;
            }

            int option;
            try {
                option = Integer.parseInt(line);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input. Enter number 1-6");
                continue;
            }

            try {
                switch (option) {
                    case 1 -> {
                        String all = service.getAllEmployees();
                        System.out.println(all.isBlank() ? "No employees." : all);
                    }
                    case 2 -> addEmployeeFlow();
                    case 3 -> deleteEmployeeFlow();
                    case 4 -> findEmployeeFlow();
                    case 5 -> showStatistics();
                    case 6 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Unknown option");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void addEmployeeFlow() {
        try {
            System.out.print("Enter id (numeric): ");
            Long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter position (or leave empty): ");
            String position = scanner.nextLine().trim();
            if (position.isEmpty()) position = null;

            System.out.print("Enter salary (e.g. 1234.56): ");
            BigDecimal salary = new BigDecimal(scanner.nextLine().trim());

            System.out.print("Enter hire date (yyyy-MM-dd) or leave empty for today: ");
            String dateInput = scanner.nextLine().trim();
            LocalDate date;
            if (dateInput.isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateInput);
                } catch (DateTimeParseException ex) {
                    System.out.println("Invalid date format, using today");
                    date = LocalDate.now();
                }
            }

            Employee emp = new Employee(id, name, position, salary, date);
            service.addEmployee(emp);
            System.out.println("Employee added.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid numeric input. Aborting add.");
        } catch (Exception ex) {
            System.out.println("Failed to add employee: " + ex.getMessage());
        }
    }

    private void deleteEmployeeFlow() {
        try {
            System.out.print("Enter id to delete: ");
            Long id = Long.parseLong(scanner.nextLine().trim());
            boolean removed = service.deleteEmployee(id);
            System.out.println(removed ? "Deleted." : "Employee not found.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
        }
    }

    private void findEmployeeFlow() {
        try {
            System.out.print("Enter id to find: ");
            Long id = Long.parseLong(scanner.nextLine().trim());
            Optional<Employee> opt = service.findById(id);
            if (opt.isPresent()) {
                Employee e = opt.get();
                System.out.println(String.format("%d - %s (%s) - %s",
                        e.getId(), e.getName(), e.getPosition() == null ? "-" : e.getPosition(), e.getSalary()));
            } else {
                System.out.println("Employee not found.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid id.");
        }
    }

    private void showStatistics() {
        double avg = service.getAverageSalary();
        System.out.println("Average salary: " + avg);

        Optional<Employee> top = service.getHighestPaidEmployee();
        if (top.isPresent()) {
            Employee e = top.get();
            System.out.println("Top paid: " + e.getName() + " id=" + e.getId() + " salary=" + e.getSalary());
        } else {
            System.out.println("No employees to show top paid.");
        }
    }
}