package com.bondarenko.querygenerator;

import com.google.common.annotations.VisibleForTesting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class QueryGenerator {

    public String findAll(Class<?> clazz) {
        StringBuilder query = new StringBuilder("SELECT ");

        Table tableNameAnnotation = clazz.getAnnotation(Table.class);
        if (tableNameAnnotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = tableNameAnnotation.name().isEmpty() ? clazz.getSimpleName() : tableNameAnnotation.name();

        StringJoiner columnsNames = new StringJoiner(", ");
        for (String allColumnsName : getAllColumnsNames(clazz)) {
            columnsNames.add(String.valueOf(allColumnsName));
        }
        String allColumnsNames = columnsNames.toString();

        query.append(allColumnsNames);
        query.append(" FROM ");
        query.append(tableName);
        query.append(";");

        return query.toString();
    }

    public String findById(Class<?> clazz, Object value) {
        StringBuilder query = new StringBuilder("SELECT");

        Table tableNameAnnotation = clazz.getAnnotation(Table.class);
        if (tableNameAnnotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = tableNameAnnotation.name().isEmpty() ? clazz.getSimpleName() : tableNameAnnotation.name();

        StringJoiner columnsNames = new StringJoiner(", ");
        for (String allColumnsName : getAllColumnsNames(clazz)) {
            columnsNames.add(String.valueOf(allColumnsName));
        }
        String allColumnsNames = columnsNames.toString();
        String idColumnName = getIdColumnName(clazz);

        query.append(" ");
        query.append(allColumnsNames);
        query.append(" ");
        query.append("FROM ");
        query.append(tableName);
        query.append(" WHERE ");
        query.append(idColumnName);
        query.append(" ");
        query.append("=");
        query.append(" ");
        query.append("'");
        query.append(value);
        query.append("'");
        query.append(";");

        return query.toString();
    }

    public String insert(Object value) {
        Class<?> clazz = value.getClass();

        StringBuilder query = new StringBuilder("INSERT INTO ");

        Table tableNameAnnotation = clazz.getAnnotation(Table.class);
        if (tableNameAnnotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = tableNameAnnotation.name().isEmpty() ? clazz.getSimpleName() : tableNameAnnotation.name();

        StringJoiner columnsNames = new StringJoiner(", ");
        for (String allColumnsName : getAllColumnsNames(clazz)) {
            columnsNames.add(String.valueOf(allColumnsName));
        }
        String allColumnsNames = columnsNames.toString();
        String allValuesOfFields = getAllValuesOfFields(value);

        query.append(tableName);
        query.append(" (");
        query.append(allColumnsNames);
        query.append(") VALUES (");
        query.append(allValuesOfFields);
        query.append(")");
        query.append(";");

        return query.toString();
    }

    public String delete(Class<?> clazz, Object value) {
        Table tableNameAnnotation = clazz.getAnnotation(Table.class);
        if (tableNameAnnotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = tableNameAnnotation.name().isEmpty() ? clazz.getSimpleName() : tableNameAnnotation.name();
        String columnName = getIdColumnName(clazz);

        StringBuilder query = new StringBuilder("DELETE ");

        query.append("FROM ");
        query.append(tableName);
        query.append(" WHERE ");
        query.append(columnName);
        query.append(" ");
        query.append("=");
        query.append(" ");
        query.append("'");
        query.append(value);
        query.append("'");
        query.append(";");

        return query.toString();
    }

    public String update(Object value) {
        Class<?> clazz = value.getClass();
        StringBuilder query = new StringBuilder("UPDATE ");

        Table tableNameAnnotation = clazz.getAnnotation(Table.class);
        if (tableNameAnnotation == null) {
            throw new IllegalArgumentException("@Table is not exist");
        }
        String tableName = tableNameAnnotation.name().isEmpty() ? clazz.getSimpleName() : tableNameAnnotation.name();

        String columnIdNameAndValue = getColumnIdValueAndName(value);
        String columnsNamesAndValues = getColumnsNamesAndValues(value);

        query.append(tableName);
        query.append(" SET ");
        query.append(columnsNamesAndValues);
        query.append(" WHERE ");
        query.append(columnIdNameAndValue);
        query.append(";");

        return query.toString();
    }

    @VisibleForTesting
    List<String> getAllColumnsNames(Class<?> clazz) {
        List<String> allColumnsNames = new ArrayList<>();
        for (Field declaredField : clazz.getDeclaredFields()) {
            Column columnAnnotation = declaredField.getAnnotation(Column.class);
            if (columnAnnotation != null) {
                String columnName = columnAnnotation.name().isEmpty() ? declaredField.getName() : columnAnnotation.name();
                allColumnsNames.add(columnName);
            }
        }
        return allColumnsNames;
    }

    @VisibleForTesting
    String getAllValuesOfFields(Object value) {
        StringJoiner allValuesOfFields = new StringJoiner(", ");
        Class<?> clazz = value.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(Column.class) != null) {
                allValuesOfFields.add(getColumnValue(field, value));
            }
        }
        return allValuesOfFields.toString();
    }

    @VisibleForTesting
    String getColumnIdValueAndName(Object value) {
        StringJoiner columnIdValueAndName = new StringJoiner(", ");
        Class<?> clazz = value.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) != null && (field.getAnnotation(Id.class) != null)) {
                columnIdValueAndName.add(getColumnName(field) + " = " + getColumnValue(field, value));
            }
        }
        return columnIdValueAndName.toString();
    }

    @VisibleForTesting
    String getIdColumnName(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null && (field.getAnnotation(Column.class) != null)) {
                String columnNameAnnotation = field.getAnnotation(Column.class).name();
                return columnNameAnnotation.isEmpty() ? field.getName() : columnNameAnnotation;
            }
        }
        throw new RuntimeException("Annotation of column is not exist");
    }

    @VisibleForTesting
    String getColumnsNamesAndValues(Object value) {
        StringJoiner columnsNamesAndValues = new StringJoiner(", ");
        Class<?> clazz = value.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(Column.class) != null && (field.getAnnotation(Id.class) == null)) {
                columnsNamesAndValues.add(getColumnName(field) + " = " + getColumnValue(field, value));
            }
        }
        return columnsNamesAndValues.toString();
    }

    private String getColumnValue(Field field, Object value) {
        field.setAccessible(true);
        try {
            Object columnValue = field.get(value);
            if (columnValue == null) {
                return null;
            }
            if (field.getType() == String.class) {
                return "'" + columnValue + "'";
            }
            return "'" + columnValue + "'";
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Value is not an instance of the class declaring by this field", e);
        }
    }

    private String getColumnName(Field field) {
        String columnAnnotationName = field.getAnnotation(Column.class).name();
        String columnName = columnAnnotationName.isEmpty() ? field.getName() : field.getAnnotation(Column.class).name();
        return columnName;
    }
}
