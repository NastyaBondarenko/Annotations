package com.bondarenko.querygenerator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueryGeneratorTest {

    private QueryGenerator queryGenerator = new QueryGenerator();

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
        String expectedQuery = "SELECT person_id, person_name, person_salary FROM Person WHERE person_id = 10;";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Insert")
    public void testInsert() {
        Person newPerson = new Person(4, "Katya", 1000.0);
        String actualQuery = queryGenerator.insert(newPerson);
        String expectedQuery = "INSERT INTO Person (person_id, person_name, person_salary) VALUES ('4, 'Katya', 1000.0');";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Delete")
    public void testDelete() {
        String actualQuery = queryGenerator.delete(Person.class, 10);
        String expectedQuery = "DELETE FROM Person WHERE person_id = 10;";

        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    @DisplayName("test Update")
    public void testUpdate() {
        Person newPerson = new Person(4, "Katya", 1000);
        String actualQuery = queryGenerator.update(newPerson);
        String expectedQuery = "UPDATE Person SET person_name = 'Katya', person_salary = 1000.0 WHERE person_id = 4;";

        assertEquals(expectedQuery, actualQuery);
    }

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
    @DisplayName("test Get All Values Of Fields")
    public void testGetAllValuesOfFields() {
        Person newPerson = new Person(4, "Katya", 1000.0);
        String allColumnsNames = queryGenerator.getAllValuesOfFields(newPerson);
        assertTrue(allColumnsNames.contains("4"));
        assertTrue(allColumnsNames.contains("Katya"));
        assertTrue(allColumnsNames.contains("1000.0"));
    }

    @Test
    @DisplayName("test Get Column Id Name And Value")
    public void testGetColumnIdNameAndValue() {
        Person newPerson = new Person(4, "Katya", 1000.0);

        String actual = queryGenerator.getColumnIdNameAndValue(newPerson);
        String expected = "person_id = 4";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("test Get Id Column Name")
    public void testGetIdColumnName() {
        String actual = queryGenerator.getIdColumnName(Person.class);
        String expected = "person_id";

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("test Get Columns Names And Values")
    public void testGetColumnsNamesAndValues() {
        Person newPerson = new Person(4, "Katya", 1000.0);

        String actual = queryGenerator.getColumnsNamesAndValues(newPerson);
        String expected = "person_name = 'Katya', person_salary = 1000.0";

        assertEquals(expected, actual);
    }

}
