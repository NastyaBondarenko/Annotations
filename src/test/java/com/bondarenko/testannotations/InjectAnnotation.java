package com.bondarenko.testannotations;

import com.bondarenko.annotations.Inject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InjectAnnotation {
    String id;
    @Inject(clazz = String.class)
    String name;

    @Inject(clazz = ArrayList.class)
    List<String> list1;

    @Inject(clazz = LinkedList.class)
    List<String> list2;

    List<String> list3;
}
