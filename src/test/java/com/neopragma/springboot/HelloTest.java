package com.neopragma.springboot;
import org.junit.Test;

import com.neopragma.springboot.Hello;

import static org.junit.Assert.assertEquals;

public class HelloTest {    
    @Test
    public void itSaysHello() {
        Hello hello = new Hello();
        assertEquals("Hello, World!", hello.greet());
    }
}