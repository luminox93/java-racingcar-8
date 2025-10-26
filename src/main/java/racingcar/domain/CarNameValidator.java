package racingcar.domain;

import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

public class CarNameValidator {
	private final int minNameLength;
	private final int maxNameLength;
	private final Pattern validNamePattern;

	public CarNameValidator() {
		this(1, 5);
	}

	public CarNameValidator(int minNameLength, int maxNameLength) {
		assert minNameLength > 0 : "minNameLength는 0보다 커야 합니다";
		assert maxNameLength > 0 : "maxNameLength는 0보다 커야 합니다";
		assert minNameLength <= maxNameLength : "minNameLength는 maxNameLength 이하여야 합니다";

		this.minNameLength = minNameLength;
		this.maxNameLength = maxNameLength;
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
		assert names != null : "names는 null일 수 없습니다";

		validateListNotEmpty(names);
		validateNoBlankName(names);
		validateNoDuplicateName(names);
	}

	private void validateEachName(List<String> names) {
		assert names != null : "names는 null일 수 없습니다";

		names.forEach(name -> {
			validateLength(name);
			validateNoWhitespace(name);
			validateNoSpecialCharacters(name);
		});
	}

	private void validateListNotEmpty(List<String> names) {
		assert names != null : "names는 null일 수 없습니다";

		if (names.isEmpty()) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}
	}

	private void validateNoBlankName(List<String> names) {
		assert names != null : "names는 null일 수 없습니다";

		if (names.stream().anyMatch(name -> name == null || name.trim().isEmpty())) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}
	}

	private void validateNoDuplicateName(List<String> names) {
		assert names != null : "names는 null일 수 없습니다";

		if (names.size() != new HashSet<>(names).size()) {
			throw new InvalidCarNameException(ErrorType.DUPLICATE);
		}
	}

	private void validateLength(String name) {
		assert name != null : "name은 null일 수 없습니다";

		if (name.length() < minNameLength || name.length() > maxNameLength) {
			throw new InvalidCarNameException(ErrorType.INVALID_LENGTH);
		}
	}

	private void validateNoWhitespace(String name) {
		assert name != null : "name은 null일 수 없습니다";

		if (name.contains(" ") || name.contains("\t")) {
			throw new InvalidCarNameException(ErrorType.WHITESPACE);
		}
	}

	private void validateNoSpecialCharacters(String name) {
		assert name != null : "name은 null일 수 없습니다";

		if (!validNamePattern.matcher(name).matches()) {
			throw new InvalidCarNameException(ErrorType.SPECIAL_CHAR);
		}
	}
}
