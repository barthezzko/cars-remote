package com.barthezzko.cars;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barthezzko.cars.CarPositionCalculator.Position;

public class CarPositionTest {

	CarPositionCalculator calc = new CarPositionCalculatorImpl(15);

	@Test
	public void testSimple() {
		assertEquals(Position.of(7, 7), calc.calculate("5,5:RFLFRFLF"));
		assertEquals(Position.of(6, 6), calc.calculate("6,6:FFLFFLFFLFF"));
		assertEquals(Position.of(1, 4), calc.calculate("5,5:FLFLFFRFFF"));
	}

}