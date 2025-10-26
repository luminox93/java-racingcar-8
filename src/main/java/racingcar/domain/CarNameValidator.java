package racingcar.domain;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

import static racingcar.domain.AssertMessage.*;

public class CarNameValidator {
	private final int maxNameLength;
	private final int minNameLength;
	private final Pattern validNamePattern;

	public CarNameValidator() {
		this(5, 1);
	}

	public CarNameValidator(int maxNameLength, int minNameLength) {
		this.maxNameLength = maxNameLength;
		this.minNameLength = minNameLength;
		this.validNamePattern = Pattern.compile("^[a-zA-Z가-힣0-9]+$");
	}

	public void validate(List<String> names) {
		if (names == null) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}

		validateList(names);
		validateEachName(names);
	}

	private void validateList(List<String> names) {
		assert names != null : NAMES_NULL_IN_VALIDATE_LIST.getMessage();

		validateListNotEmpty(names);
		validateNoBlankName(names);
		validateNoDuplicateName(names);
	}

	private void validateEachName(List<String> names) {
		assert names != null : NAMES_NULL_IN_VALIDATE_EACH_NAME.getMessage();

		names.forEach(name -> {
			validateLength(name);
			validateNoWhitespace(name);
			validateNoSpecialCharacters(name);
		});
	}

	private void validateListNotEmpty(List<String> names) {
		assert names != null : NAMES_NULL_IN_VALIDATE_LIST_NOT_EMPTY.getMessage();

		if (names.isEmpty()) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}
	}

	private void validateNoBlankName(List<String> names) {
		assert names != null : NAMES_NULL_IN_VALIDATE_NO_BLANK_NAME.getMessage();

		if (names.stream().anyMatch(name -> name == null || name.trim().isEmpty())) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}
	}

	private void validateNoDuplicateName(List<String> names) {
		assert names != null : NAMES_NULL_IN_VALIDATE_NO_DUPLICATE_NAME.getMessage();

		if (names.size() != new HashSet<>(names).size()) {
			throw new InvalidCarNameException(ErrorType.DUPLICATE);
		}
	}

	private void validateLength(String name) {
		assert name != null : NAME_NULL_IN_VALIDATE_LENGTH.getMessage();

		if (name.length() < minNameLength || name.length() > maxNameLength) {
			throw new InvalidCarNameException(ErrorType.INVALID_LENGTH);
		}
	}

	private void validateNoWhitespace(String name) {
		assert name != null : NAME_NULL_IN_VALIDATE_NO_WHITESPACE.getMessage();

		if (name.contains(" ") || name.contains("\t")) {
			throw new InvalidCarNameException(ErrorType.WHITESPACE);
		}
	}

	private void validateNoSpecialCharacters(String name) {
		assert name != null : NAME_NULL_IN_VALIDATE_NO_SPECIAL_CHARACTERS.getMessage();

		if (!validNamePattern.matcher(name).matches()) {
			throw new InvalidCarNameException(ErrorType.SPECIAL_CHAR);
		}
	}
}
