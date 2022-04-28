package com.bondarenko.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtils {

    public List<String> invokeMethodsWithAnnotationRun(Class<?> clazz, Object object) {
        List<String> listMethodsWithAnnotationRun = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            Run annotation = method.getAnnotation(Run.class);
            if (annotation != null) {
                listMethodsWithAnnotationRun.add(method.getName());
                method.setAccessible(true);
                try {
                    method.invoke(object);
                } catch (InvocationTargetException | IllegalAccessException exception) {
                    throw new RuntimeException("Can`t invoke methods. Check invoked object", exception);
                }
                method.setAccessible(false);
            }
        }
        return listMethodsWithAnnotationRun;
    }

    public List<String> fillFieldsWithAnnotationInjectByObjectOfAnnotationClazz(Object object) {
        List<String> fieldsWithAnnotationInject = new ArrayList<>();
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            Inject annotation = field.getAnnotation(Inject.class);
            if (annotation != null) {
                fieldsWithAnnotationInject.add(field.getName());
                Class<?> objectClazz = annotation.clazz();
                Object newObject;
                try {
                    newObject = objectClazz.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException exception) {
                    throw new RuntimeException("Can`t create new object.Illegal access to class", exception);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Can`t create new object by constructor", e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Can`t create new object. Method is not exist", e);
                }
                field.setAccessible(true);
                try {
                    field.set(object, newObject);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Can`t set new object instead previous object", e);
                }
                field.setAccessible(false);
            }
        }
        return fieldsWithAnnotationInject;
    }
}

