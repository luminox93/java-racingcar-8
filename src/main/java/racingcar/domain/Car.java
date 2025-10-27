package racingcar.domain;

import java.util.regex.Pattern;

import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

public class Car {
	private static final int MIN_NAME_LENGTH = 1;
	private static final int MAX_NAME_LENGTH = 5;
	private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9]+$");

	private final String name;
	private final int position;

	public Car(String name) {
		validateName(name);
		this.name = name;
		this.position = 0;
	}

	private Car(String name, int position) {
		assert name != null && !name.isEmpty() : "name 검증이 실패헀습니다.";
		assert position >= 0 : "position은 음수일 수 없습니다.";

		this.name = name;
		this.position = position;
	}

	private void validateName(String name) {
		validateNameNotNull(name);
		validateNameNotEmpty(name);
		validateNameLength(name);
		validateNoWhiteSpace(name);
		validateSpecialCharacter(name);
	}

	private void validateNameNotNull(String name) {
		if (name == null) {
			throw new InvalidCarNameException(ErrorType.NULL);
		}
	}

	private void validateNameNotEmpty(String name) {
		if (name.isEmpty()) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}
	}

	private void validateNameLength(String name) {
		if (name.length() < MIN_NAME_LENGTH
				|| name.length() > MAX_NAME_LENGTH) {
			throw new InvalidCarNameException(ErrorType.INVALID_LENGTH);
		}
	}

	private void validateNoWhiteSpace(String name) {
		if (name.contains(" ")) {
			throw new InvalidCarNameException(ErrorType.WHITE_SPACE);
		}
	}

	private void validateSpecialCharacter(String name) {
		if (!VALID_NAME_PATTERN.matcher(name).matches()) {
			throw new InvalidCarNameException(ErrorType.SPECIAL_CHARACTER);
		}
	}

	public Car move(boolean shouldMove) {
		if (shouldMove) {
			return new Car(name, position + 1);
		}
		return this;
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}
}
