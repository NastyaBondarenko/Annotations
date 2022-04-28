package com.bondarenko.testannotations;

import com.bondarenko.annotations.AnnotationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnnotationTest {
    private AnnotationUtils annotationUtils = new AnnotationUtils();
    private RunAnnotation runAnnotation = new RunAnnotation();
    private InjectAnnotation injectAnnotation = new InjectAnnotation();

    @Test
    @DisplayName("test Invoke Methods With Annotation Run")
    public void testInvokeMethods_WithAnnotationRun() {
        List<String> methodsWithAnnotationRun = annotationUtils.invokeMethodsWithAnnotationRun(RunAnnotation.class, runAnnotation);

        assertTrue(methodsWithAnnotationRun.contains("method1"));
        assertTrue(methodsWithAnnotationRun.contains("method2"));
        assertFalse(methodsWithAnnotationRun.contains("method3"));

        assertEquals(2, methodsWithAnnotationRun.size());
    }

    @Test
    @DisplayName("test Fill Fields With Annotation Inject By Object Of Annotation Clazz")
    public void testFillFields_WithAnnotationInject_ByObject_OfAnnotationClazz() {
        List<String> fieldsWithAnnotationInject = annotationUtils.fillFieldsWithAnnotationInjectByObjectOfAnnotationClazz(injectAnnotation);

        assertTrue(fieldsWithAnnotationInject.contains("name"));
        assertTrue(fieldsWithAnnotationInject.contains("list1"));
        assertTrue(fieldsWithAnnotationInject.contains("list2"));

        assertEquals(3, fieldsWithAnnotationInject.size());
    }
}
