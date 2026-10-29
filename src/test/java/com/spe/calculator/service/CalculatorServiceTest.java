package com.spe.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculatorServiceTest {

    private CalculatorService service;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    @Test
    void addReturnsSum() {
        assertEquals(7.0, service.add(2, 5));
        assertEquals(-3.0, service.add(2, -5));
    }

    @Test
    void subtractReturnsDifference() {
        assertEquals(1.0, service.subtract(4, 3));
        assertEquals(7.0, service.subtract(4, -3));
    }

@Test
    void multiplyReturnsProduct() {
        assertEquals(6.0, service.multiply(2, 3));
        assertEquals(0.0, service.multiply(0, 77));
    }

    @Test
    void divideReturnsQuotient() {
        assertEquals(3.5, service.divide(7, 2));
        assertEquals(-2.0, service.divide(-4, 2));
    }

    @Test
    void divideByZeroThrows() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> service.divide(1, 0));
        assertEquals("Division by zero is not allowed", ex.getMessage());
    }
}
