package ru.ranepa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.ranepa.Controller.EmployeeController;
import ru.ranepa.model.Employee;
import ru.ranepa.service.HRMService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmployeeController.class)  // Тестируем только EmployeeController
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HRMService employeeService;

    // Тестовые данные
    private Employee testEmployee1;
    private Employee testEmployee2;
    private String testEmployeeJson;

    // инициализация тестовых заданий
    @BeforeEach
    void setUp() throws Exception {
        // Создаем тестовых сотрудников
        testEmployee1 = new Employee("Кушнарева Анастасия Алексеевна", "Директор",
                BigDecimal.valueOf(300000), LocalDate.of(2023, 6, 20));
        testEmployee1.setId(1L);  // Устанавливаем ID, как если бы он был из БД
        testEmployee1.setCreatedAt(java.time.LocalDateTime.now());

        testEmployee2 = new Employee("Гурецкая Дарья Денисовна", "Бизнес аналитик",
                BigDecimal.valueOf(120000), LocalDate.of(2024, 3, 6));
        testEmployee2.setId(2L);
        testEmployee2.setCreatedAt(java.time.LocalDateTime.now());

        // Преобразуем тестового сотрудника в JSON для POST запросов
        testEmployeeJson = objectMapper.writeValueAsString(testEmployee1);
    }

    // тест 1: Получение списка (GET 200 OK)
    @Test
    void shouldReturnAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(testEmployee1, testEmployee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Кушнарева Анастасия Алексеевна")))
                .andExpect(jsonPath("$[0].position", is("Директор")))
                .andExpect(jsonPath("$[0].salary", is(300000.0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Гурецкая Дарья Денисовна")));

        // Проверяем, что метод сервиса был вызван 1 раз
        verify(employeeService, times(1)).getAllEmployees();
    }


    // тест 2: Создание сотрудника (POST 201 Created)
    @Test
    void shouldCreateEmployee() throws Exception {
        when(employeeService.addEmployee(any(Employee.class))).thenReturn(testEmployee1);

        // Создаем JSON для нового сотрудника (без ID, без createdAt)
        String newEmployeeJson = """
                {
                    "name": "Кушнарева Анастасия Алексеевна",
                    "position": "Директор",
                    "salary": 300000,
                    "hireDate": "2023-06-20"
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newEmployeeJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Кушнарева Анастасия Алексеевна")))
                .andExpect(jsonPath("$.position", is("Директор")))
                .andExpect(jsonPath("$.salary", is(300000.0)));
        verify(employeeService, times(1)).addEmployee(any(Employee.class));
    }


    // тест 3: Получение несуществующего (GET 404 Not Found)
    @Test
    void shouldReturnNotFoundForNonExistingEmployee() throws Exception {
        when(employeeService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());  // 404 NOT FOUND

        verify(employeeService, times(1)).findById(999L);
    }


    // тест 4: Статистику при пустой БД
    @Test
    void shouldReturnEmptyStatisticsWhenNoEmployees() throws Exception {
        when(employeeService.getAverageSalary()).thenReturn(0.0);
        when(employeeService.findHighestPaidEmployee()).thenReturn(Optional.empty());
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/employees/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageSalary", is(0.0)))
                .andExpect(jsonPath("$.totalEmployees", is(0)))
                .andExpect(jsonPath("$.highestPaidEmployee").value(nullValue()));
    }
}
