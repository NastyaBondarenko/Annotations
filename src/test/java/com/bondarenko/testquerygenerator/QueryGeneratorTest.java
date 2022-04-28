package com.bondarenko.testquerygenerator;

import com.bondarenko.querygenerator.Person;
import com.bondarenko.querygenerator.QueryGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueryGeneratorTest {

    private QueryGenerator queryGenerator = new QueryGenerator();

    @Test
    @DisplayName("test Get All Columns Names")
    public void testGetAllColumnsNames() {
        List<String> allColumnsNames = queryGenerator.getAllColumnsNames(Person.class);
        assertTrue(allColumnsNames.contains("person_id"));
        assertTrue(allColumnsNames.contains("person_name"));
        assertTrue(allColumnsNames.contains("person_salary"));

        assertEquals(3, allColumnsNames.size());
    }

    @Test
    @DisplayName("test Get Column Name By Id")
    public void testGetColumnNameById() {
        String columnNameById = queryGenerator.getColumnNameById(Person.class);
        assertEquals("person_id", columnNameById);
    }

    @Test
    @DisplayName("test Find All")
    public void testFindAll() {
        String actualQuery = queryGenerator.findAll(Person.class);
        String expectedQuery = "SELECT person_id, person_name, person_salary FROM Person;";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Find ById")
    public void testFindById() {
        String actualQuery = queryGenerator.findById(Person.class, 10);
        String expectedQuery = "SELECT * FROM Person WHERE person_id=10;";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Insert")
    public void testInsert() {
        String actualQuery = queryGenerator.insert(10);
        String expectedQuery = "INSERT INTO Person (person_id) VALUES ('10');";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Delete")
    public void testDelete() {
        String actualQuery = queryGenerator.delete(Person.class, 10);
        String expectedQuery = "DELETE FROM Person WHERE person_id=10;";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Update")
    public void testUpdate() {
        String actualQuery = queryGenerator.update(10, 5);
        String expectedQuery = "UPDATE Person SET person_id=5 WHERE person_id=10;";

        assertEquals(expectedQuery, actualQuery);
    }
}
