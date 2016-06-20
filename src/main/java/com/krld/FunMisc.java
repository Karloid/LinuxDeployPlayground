package com.krld;

public class FunMisc {
    public static void main(String[] args) {
        backspaceFun();
        caretFun();
    }


    private static void backspaceFun() {
        System.out.print("%|=>");
        for (int i = 0; i < 70; i++) {
            System.out.print("\b=>");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    private static void caretFun() {
        try {
            System.out.print("\r1");
            Thread.sleep(1000);
            System.out.print("\r2");
            Thread.sleep(1000);
            System.out.print("\r3");
            Thread.sleep(1000);
            System.out.print("\r4");
            Thread.sleep(1000);
            System.out.print("\r5");
            Thread.sleep(1000);
            System.out.print("\r6");
            Thread.sleep(5000);
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}