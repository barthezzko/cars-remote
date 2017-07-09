package com.barthezzko.cars;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

public class CarPositionCalculatorImpl implements CarPositionCalculator {

	private final int gridSize;
	private static final String PARTS_DLMTR = ":";
	private static final String POS_DLMTR = ",";
	private Logger logger = Logger.getLogger(CarPositionCalculatorImpl.class);
	
	public CarPositionCalculatorImpl(int gridSize) {
		this.gridSize = gridSize;
	}

	public Position calculate(String input) {
		Objects.requireNonNull(input, "Initial position and instruction set should be set");
		Position initial = extractInitialPosition(input);
		return applyActions(initial, input);
	}

	private Position applyActions(Position position, String input) {
		logger.info("Applying actions to initial position: " + position);
		logger.info("Supported codes: " + Arrays.asList(ActionCode.values()));
		String actionString = input.substring(input.indexOf(PARTS_DLMTR) + 1);
		for (char code : actionString.toCharArray()) {
			logger.debug(code);
			try{
				ActionCode at = ActionCode.valueOf(String.valueOf(code));
				at.accept(position);
				logger.info("After applying [" + code +"] to position: " + position);
			} catch(IllegalArgumentException ex) {
				String errorMsg = "Illegal action type, code=" + code + ", supported are: " + Arrays.asList(ActionCode.values());
				logger.error(errorMsg);
				throw new IllegalArgumentException(errorMsg);
			}
		}
		return position;
	}

	private Position extractInitialPosition(String input) {
		int initialPosStringEnd = input.indexOf(PARTS_DLMTR);
		if (initialPosStringEnd < 1) {
			throw new IllegalArgumentException("Initial position should be set. Valid format is X,Y:[F,L,R]*");
		}
		String initialPosString = input.substring(0, initialPosStringEnd);
		String[] coordinates = initialPosString.split(POS_DLMTR);
		if (coordinates.length != 2) {
			throw new IllegalArgumentException(
					"Initial position should be set with exactly 2 numbers splitted by comma corresponding to coordinates, for ex. [4,5]");
		}
		try {
			return Position.of(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Initial position position definition is not valid: " + initialPosString);
		}
	}

	enum ActionCode implements Consumer<Position> {
		L((pos) -> {
			pos.changeX(-1);
		}), F((pos) -> {
			pos.changeY(1);
		}), R((pos) -> {
			pos.changeX(1);
		});

		private final Consumer<Position> consumer;

		private ActionCode(final Consumer<Position> consumer) {
			this.consumer = consumer;
		}
		
		@Override
		public void accept(Position t) {
			consumer.accept(t);
		}

	}
}
