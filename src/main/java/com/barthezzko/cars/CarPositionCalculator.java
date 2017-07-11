package com.barthezzko.cars;

import java.util.function.BiConsumer;

import com.barthezzko.web.Config;

public interface CarPositionCalculator {

	Position calculate(String input);
	
	public static class Position {
		private int x;
		private int y;
		private Direction direction = Direction.NORTH;

		private Position() {
		}

		public static Position of(int x, int y, Direction dir) {
			Position pos = new Position();
			pos.x = x;
			pos.y = y;
			pos.direction = dir;
			return pos;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public void moveBy(int augment) {
			direction.accept(this, augment);
		}

		public void rotateLeft() {
			direction = direction.counterClockwise();
		}
		public void rotateRight() {
			direction = direction.clockwise();
		}

		@Override
		public String toString() {
			return "Position [x=" + x + ", y=" + y + ", direction=" + direction + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((direction == null) ? 0 : direction.hashCode());
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Position other = (Position) obj;
			if (direction != other.direction)
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
	}

	public enum Direction implements BiConsumer<Position, Integer>{

		NORTH(0,(pos, augment) -> {
			pos.y+=augment;
		}), EAST(1,(pos, augment) -> {
			pos.x+=augment;
		}), SOUTH(2,(pos, augment) -> {
			pos.y-=augment;
		}), WEST(3,(pos, augment) -> {
			pos.x-=augment;
		});
		private final int order;
		private final BiConsumer<Position, Integer> consumer;

		private Direction(final int order, final BiConsumer<Position, Integer> consumer) {
			this.order = order;
			this.consumer = consumer;
		}

		public Direction clockwise(){
			return rotate(1);
		}
		public Direction counterClockwise(){
			return rotate(-1);
		}
		private Direction rotate(int augment) {
			return forOrder(order + augment);
		}
		public Direction forOrder(int orderParam) {
			switch (orderParam) {
			case -1:
				return WEST;
			case 4:
				return NORTH;
			default:
				for (Direction dir : values()) {
					if (dir.order == orderParam) {
						return dir;
					}
				}
			}
			throw new RuntimeException("Impossible direction order reached [orderParam=" + orderParam+ "], please check you'r exposed API or serve such situation properly");
		}

		@Override
		public void accept(Position t, Integer augment) {
			consumer.accept(t, augment);
		}
	}

	Config getConfig();

}
