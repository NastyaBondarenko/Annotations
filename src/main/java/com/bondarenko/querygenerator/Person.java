package com.bondarenko.querygenerator;

@Table
public class Person {
    @Column(name = "person_id")
    private int id;

    @Column(name = "person_name")
    private String name;

    @Column(name = "person_salary")
    private double salary;
}
