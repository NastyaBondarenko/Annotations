package com.bondarenko.querygenerator;

import com.google.common.annotations.VisibleForTesting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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

        StringJoiner columnsNames = new StringJoiner(", ");
        for (String allColumnsName : getAllColumnsNames(clazz)) {
            columnsNames.add(String.valueOf(allColumnsName));
        }
        String AllColumnsNames = columnsNames.toString();

        StringJoiner idColumnName = new StringJoiner(" ");
        String columnName = getIdColumnName(clazz);
        idColumnName.add(columnName);

        stringBuilder.append(" ");
        stringBuilder.append(AllColumnsNames);
        stringBuilder.append(" ");
        stringBuilder.append("FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(idColumnName);
        stringBuilder.append(" ");
        stringBuilder.append("=");
        stringBuilder.append(" ");
        stringBuilder.append(id);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public String insert(Object id) {
        Class<?> clazz = id.getClass();

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO ");

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
        String valuesOfFields = getAllValuesOfFields(id);

        stringBuilder.append(tableName);
        stringBuilder.append(" (");
        stringBuilder.append(AllColumnsNames);
        stringBuilder.append(") VALUES ('");
        stringBuilder.append(valuesOfFields);
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

        StringJoiner idColumnName = new StringJoiner(" ");
        String columnName = getIdColumnName(clazz);
        idColumnName.add(columnName);

        StringBuilder stringBuilder = new StringBuilder("DELETE ");
        stringBuilder.append("FROM ");
        stringBuilder.append(tableName);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(idColumnName);
        stringBuilder.append(" ");
        stringBuilder.append("=");
        stringBuilder.append(" ");
        stringBuilder.append(id);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    public String update(Object value) {
        Class<?> clazz = value.getClass();

        StringBuilder stringBuilder = new StringBuilder("UPDATE ");

        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();

        StringJoiner columnNameById = new StringJoiner(" ");
        String columnName = getIdColumnName(clazz);
        columnNameById.add(columnName);

        String columnIdNameAndValue = getColumnIdValueAndName(value);
        String columnsNamesAndValues = getColumnsNamesAndValues(value);

        stringBuilder.append(tableName);
        stringBuilder.append(" SET ");
        stringBuilder.append(columnsNamesAndValues);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(columnIdNameAndValue);
        stringBuilder.append(";");

        return stringBuilder.toString();
    }

    @VisibleForTesting
    List<String> getAllColumnsNames(Class<?> clazz) {
        List<String> columnsNames = new ArrayList<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                columnsNames.add(columnAnnotation.name());
            }
        }
        return columnsNames;
    }

    @VisibleForTesting
    String getAllValuesOfFields(Object value) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Class<?> clazz = value.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(Column.class) != null) {
                stringJoiner.add(getColumnValue(field, value));
            }
        }
        return stringJoiner.toString();
    }

    @VisibleForTesting
    String getColumnIdValueAndName(Object value) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Class<?> clazz = value.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) != null && (field.getAnnotation(Key.class) != null)) {
                stringJoiner.add(getColumnName(field) + " = " + getColumnValue(field, value));
            }
        }
        return stringJoiner.toString();
    }

    @VisibleForTesting
    String getIdColumnName(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Key.class) != null && (field.getAnnotation(Column.class) != null)) {
                return field.getAnnotation(Column.class).name();
            }
        }
        return null;
    }

    @VisibleForTesting
    String getColumnsNamesAndValues(Object value) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        Class<?> clazz = value.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) != null && (field.getAnnotation(Key.class) == null)) {
                stringJoiner.add(getColumnName(field) + " = " + getColumnValue(field, value));
            }
        }
        return stringJoiner.toString();
    }

    private String getColumnValue(Field field, Object id) {
        field.setAccessible(true);
        try {
            Object value = field.get(id);
            if (value == null) {
                return null;
            }
            if (field.getType() == String.class) {
                return "'" + value + "'";
            }
            return String.valueOf(value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String getColumnName(Field field) {
        return field.getAnnotation(Column.class).name();
    }
}
