package com.bondarenko.testannotations;

import com.bondarenko.annotations.Run;

public class RunAnnotation {
    @Run
    public void method1() {
        System.out.println("run");
    }

    @Run
    public void method2() {
        System.out.println("call");
    }

    public void method3() {
        System.out.println("delete");
    }
}
