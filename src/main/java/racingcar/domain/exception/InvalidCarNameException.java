package racingcar.domain.exception;

public class InvalidCarNameException extends IllegalArgumentException {
	public enum ErrorType {
		NULL("[ERROR] 자동차 이름을 입력해주세요."),
		EMPTY("[ERROR] 자동차 이름을 입력해주세요."),
		INVALID_LENGTH("[ERROR] 자동차 이름의 길이가 올바르지 않습니다."),
		WHITE_SPACE("[ERROR] 자동차 이름에 공백을 포함할 수 없습니다."),
		SPECIAL_CHARACTER("[ERROR] 자동차 이름은 한글, 영문, 숫자만 사용 가능합니다."),
		DUPLICATE("[ERROR] 중복된 자동차 이름이 있습니다.");

		private final String message;

		ErrorType(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	private final ErrorType errorType;

	public InvalidCarNameException(ErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}
}
