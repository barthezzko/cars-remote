package com.barthezzko.cars;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.barthezzko.cars.CarPositionCalculator.Direction;

public class DirectionTest {
	
	@Test
	public void test(){
		assertEquals(Direction.NORTH, Direction.WEST.clockwise());
		assertEquals(Direction.WEST, Direction.NORTH.counterClockwise());
		assertEquals(Direction.WEST, Direction.SOUTH.clockwise());
		assertEquals(Direction.WEST, Direction.EAST.counterClockwise().counterClockwise());
		assertEquals(Direction.NORTH, Direction.NORTH.counterClockwise().clockwise());
	}

}
