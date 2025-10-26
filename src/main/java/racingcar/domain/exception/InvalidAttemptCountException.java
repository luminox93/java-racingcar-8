package racingcar.domain.exception;

public class InvalidAttemptCountException extends IllegalArgumentException {
	public enum ErrorType {
		EMPTY,
		NOT_NUMBER,
		OUT_OF_RANGE
	}

	private final ErrorType errorType;

	public InvalidAttemptCountException(ErrorType errorType) {
		super(errorType.name());
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
}
