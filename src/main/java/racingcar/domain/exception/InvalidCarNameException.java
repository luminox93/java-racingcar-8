package racingcar.domain.exception;

public class InvalidCarNameException extends IllegalArgumentException {
	public enum ErrorType {
		EMPTY,
		DUPLICATE,
		INVALID_LENGTH,
		WHITESPACE,
		SPECIAL_CHAR
	}

	private final ErrorType errorType;

	public InvalidCarNameException(ErrorType errorType) {
		super(errorType.name());
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
}
