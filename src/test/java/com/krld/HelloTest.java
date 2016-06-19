package com.krld;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelloTest {

    private Hello hello;

    @Before
    public void setUp() throws Exception {
        hello = new Hello();
        System.out.println("setup");
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("tear down");
    }

    @Test
    public void getHello() throws Exception {
        System.out.println("test hello class");
        String output = hello.getHello();
        System.out.println(output);
        assertTrue(output.equalsIgnoreCase("hello"));
    }

    @Test
    public void calcSum() throws Exception {
        System.out.println("test calc sum");
        assertEquals(hello.calcSum(10, 10), 20);

    }
}