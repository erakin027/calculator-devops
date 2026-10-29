package com.spe.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
