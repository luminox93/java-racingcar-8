package racingcar.domain;

import racingcar.domain.exception.InvalidAttemptCountException;
import racingcar.domain.exception.InvalidAttemptCountException.ErrorType;

public class AttemptCountValidator {
	private final int minAttemptCount;
	private final int maxAttemptCount;

	public AttemptCountValidator() {
		this(1, 20);
	}

	public AttemptCountValidator(int minAttemptCount, int maxAttemptCount) {
		assert minAttemptCount > 0 : "minAttemptCount는 0보다 커야 합니다";
		assert maxAttemptCount > 0 : "maxAttemptCount는 0보다 커야 합니다";
		assert minAttemptCount <= maxAttemptCount : "minAttemptCount는 maxAttemptCount 이하여야 합니다";

		this.minAttemptCount = minAttemptCount;
		this.maxAttemptCount = maxAttemptCount;
	}

	public int validate(String input) {
		validateNotEmpty(input);
		int count = validateNumeric(input);
		validateRange(count);
		return count;
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
		if (count < minAttemptCount || count > maxAttemptCount) {
			throw new InvalidAttemptCountException(ErrorType.OUT_OF_RANGE);
		}
	}
}
