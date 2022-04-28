package com.bondarenko.querygenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class QueryGenerator {

    public String findAll(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");

        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();

        StringJoiner columnsNames = new StringJoiner(", ");

        for (String allColumnsName : getAllColumnsNames(clazz)) {
            columnsNames.add(String.valueOf(allColumnsName));
        }
        String AllColumnsNames = columnsNames.toString();

        stringBuilder.append(AllColumnsNames);
        stringBuilder.append(" FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public String findById(Class<?> clazz, Object id) {
        StringBuilder stringBuilder = new StringBuilder("SELECT");

        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();

        StringJoiner columnNameById = new StringJoiner(" ");
        String columnName = getColumnNameById(clazz);
        columnNameById.add(columnName);

        stringBuilder.append(" * ");
        stringBuilder.append("FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(columnNameById);
        stringBuilder.append("=");
        stringBuilder.append(id);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public String insert(Object value) {

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");

        Class<?> clazz = Person.class;
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();

        StringJoiner columnNameById = new StringJoiner(" ");
        String columnName = getColumnNameById(clazz);
        columnNameById.add(columnName);

        stringBuilder.append(tableName);
        stringBuilder.append(" (");
        stringBuilder.append(columnNameById);
        stringBuilder.append(") VALUES ('");
        stringBuilder.append(value);
        stringBuilder.append("')");
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public String delete(Class<?> clazz, Object id) {

        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();

        StringJoiner columnNameById = new StringJoiner(" ");
        String columnName = getColumnNameById(clazz);
        columnNameById.add(columnName);

        StringBuilder stringBuilder = new StringBuilder("DELETE ");
        stringBuilder.append("FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(columnNameById);
        stringBuilder.append("=");
        stringBuilder.append(id);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public String update(Object value, Object id) {

        Class<?> clazz = id.getClass();
        clazz = Person.class;

        StringBuilder stringBuilder = new StringBuilder("UPDATE ");

        Table annotation = clazz.getAnnotation(Table.class);

        if (annotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();

        StringJoiner columnNameById = new StringJoiner(" ");
        String columnName = getColumnNameById(clazz);
        columnNameById.add(columnName);

        stringBuilder.append(tableName);
        stringBuilder.append(" SET ");
        stringBuilder.append(columnName);
        stringBuilder.append("=");
        stringBuilder.append(id);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(columnName);
        stringBuilder.append("=");
        stringBuilder.append(value);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public List<String> getAllColumnsNames(Class<?> clazz) {
        List<String> columnsNames = new ArrayList<>();

        for (Field declaredField : clazz.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                columnsNames.add(columnAnnotation.name());
            }
        }
        return columnsNames;
    }

    public String getColumnNameById(Class<?> clazz) {
        for (Field declaredField : clazz.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (Objects.equals(columnAnnotation.name(), "person_id")) {
                return columnAnnotation.name();
            }
        }
        return null;
    }
}
