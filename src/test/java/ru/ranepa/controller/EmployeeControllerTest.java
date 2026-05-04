package ru.ranepa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // Тест 1: Проверка, что приложение запускается
    @Test
    void contextLoads() {
        assertThat(restTemplate).isNotNull();
    }

    // Тест 2: Проверка GET /api/employees (должен возвращать 200 OK)
    @Test
    void testGetAllEmployees() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/employees", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // Тест 3: Проверка GET /api/employees/stats (должен возвращать 200 OK)
    @Test
    void testGetStatistics() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/employees/stats", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // Тест 4: Проверка POST /api/employees (создание сотрудника)
    @Test
    void testCreateEmployee() {
        String requestBody = """
                {
                    "name": "Тестовый Сотрудник",
                    "position": "Тестировщик",
                    "salary": 100000,
                    "hireDate": "2024-01-01"
                }
                """;

        ResponseEntity<String> response = restTemplate.postForEntity("/api/employees", requestBody, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}