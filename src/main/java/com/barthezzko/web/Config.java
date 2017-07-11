package com.barthezzko.web;

public class Config {
	
	private final int gridSize;
	
	public Config(int gridSize) {
		this.gridSize = gridSize;
	}

	public int getGridSize() {
		return gridSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gridSize;
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
		Config other = (Config) obj;
		if (gridSize != other.gridSize)
			return false;
		return true;
	}

	
}
