package com.barthezzko.cars;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.barthezzko.cars.CarPositionCalculator.Direction;
import com.barthezzko.cars.CarPositionCalculator.Position;
import com.barthezzko.cars.CarPositionCalculatorImpl.InvalidPositionException;
import com.barthezzko.web.Config;

public class CarPositionTest {

	private final CarPositionCalculator calc = new CarPositionCalculatorImpl(new Config(15));

	@Test
	public void testSimple() {
		assertEquals(Position.of(7, 7, Direction.NORTH), calc.calculate("5,5:RFLFRFLF"));
		assertEquals(Position.of(6, 6, Direction.EAST), calc.calculate("6,6:FFLFFLFFLFF"));
		assertEquals(Position.of(1, 4, Direction.WEST), calc.calculate("5,5:FLFLFFRFFF"));
	}
	
	@Test(expected = InvalidPositionException.class)
	public void testInvalidPos(){
		assertEquals(Position.of(16, 0, Direction.NORTH), calc.calculate("16,5:RFLFRFLF"));
	}
	@Test(expected = InvalidPositionException.class)
	public void testInvalidPos2(){
		assertEquals(Position.of(1, 134, Direction.NORTH), calc.calculate("5,-1:RFLFRFLF"));
	}
	@Test(expected = InvalidPositionException.class)
	public void testInvalidPos3(){
		assertEquals(Position.of(16, 0, Direction.NORTH), calc.calculate("5,10:FFFFFF"));
	}
}
