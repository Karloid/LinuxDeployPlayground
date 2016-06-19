package com.krld;

import com.krld.example.Calculator;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void multiply() throws Exception {
        int a = 10;
        int b = 10;
        assertEquals(new Calculator().multiply(a, b), a * b);
    }
}