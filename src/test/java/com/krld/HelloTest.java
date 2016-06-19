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
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getHello() throws Exception {
        String output = hello.getHello();
        assertTrue(output.equalsIgnoreCase("hello"));
    }

    @Test
    public void calcSum() throws Exception {
        assertEquals(hello.calcSum(10, 10), 20);

    }
}