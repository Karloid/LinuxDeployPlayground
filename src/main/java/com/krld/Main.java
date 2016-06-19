package com.krld;

public class Main {
    public static void main(String[] args) {
        System.out.println("*** starting app ***");
        System.out.println(new Hello().getHello());
        System.out.println(new Calculator().multiply(5, 5));
    }
}
