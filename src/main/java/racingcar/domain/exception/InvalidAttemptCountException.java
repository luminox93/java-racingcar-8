package racingcar.domain.exception;

public class InvalidAttemptCountException extends IllegalArgumentException {
	public enum ErrorType {
		EMPTY("[ERROR] 시도 횟수를 입력해주세요."),
		NOT_NUMBER("[ERROR] 시도 횟수는 숫자여야 합니다."),
		OUT_OF_RANGE("[ERROR] 시도 횟수는 1 이상 20 이하여야 합니다.");

		private final String message;

		ErrorType(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	private final ErrorType errorType;

	public InvalidAttemptCountException(ErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
}
