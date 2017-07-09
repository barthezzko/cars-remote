package com.barthezzko.cars;

public interface CarPositionCalculator {

	Position calculate(String input);

	public static class Position {
		private int x;
		private int y;

		private Position() {
		}

		public static Position of(int x, int y) {
			Position pos = new Position();
			pos.x = x;
			pos.y = y;
			return pos;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		
		
	}

}
