package com.bondarenko.querygenerator;

@Table
public class Person {
    @Key
    @Column(name = "person_id")
    private int id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_salary")
    private double salary;

    public Person(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
}
