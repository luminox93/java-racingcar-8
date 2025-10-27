package racingcar.domain;

import racingcar.domain.exception.InvalidAttemptCountException;
import racingcar.domain.exception.InvalidAttemptCountException.ErrorType;

public class AttemptCount {
	private static final int MIN_ATTEMPT_COUNT = 1;
	private static final int MAX_ATTEMPT_COUNT = 20;

	private final int value;

	public AttemptCount(String input) {
		validateNotEmpty(input);
		int count = validateNumeric(input);
		validateRange(count);
		this.value = count;
	}

	private void validateNotEmpty(String input) {
		if (input == null || input.trim().isEmpty()) {
			throw new InvalidAttemptCountException(ErrorType.EMPTY);
		}
	}

	private int validateNumeric(String input) {
		assert input != null : "input은 null일 수 없습니다";

		try {
			return Integer.parseInt(input.trim());
		} catch (NumberFormatException e) {
			throw new InvalidAttemptCountException(ErrorType.NOT_NUMBER);
		}
	}

	private void validateRange(int count) {
		if (count < MIN_ATTEMPT_COUNT || count > MAX_ATTEMPT_COUNT) {
			throw new InvalidAttemptCountException(ErrorType.OUT_OF_RANGE);
		}
	}

	public int getValue() {
		return value;
	}
}
