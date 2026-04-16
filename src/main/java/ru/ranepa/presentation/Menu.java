package ru.ranepa.presentation;
import ru.ranepa.service.HRMService;
import java.util.Scanner;
public class Menu {
    private Scanner scanner = new Scanner(System.in);
    private HRMService service;

    public Menu(HRMService service) {
        this.service = service;
    }

    public void showMenu() throws Exception {
        System.out.println("1 - Show all employees");
        System.out.println("2 - Average salary");
        System.out.println("3 - Exit");

        int input = scanner.nextInt();

        String responce = switch (input) {
            case 1 -> service.getAllEmployees();
            case 2 -> String.valueOf(service.getAverageSalary());
            case 3 -> throw new Exception("Program stopped...");
            default -> throw new IllegalArgumentException("Unknown command");
        };
        System.out.println(responce);
    }
}
