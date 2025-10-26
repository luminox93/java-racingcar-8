package racingcar.view.messages;

public enum ErrorMessage {
	CAR_NAME_EMPTY("자동차 이름을 입력해주세요."),
	CAR_NAME_TOO_LONG("자동차 이름은 5자 이하여야 합니다."),
	CAR_NAME_CONTAINS_WHITESPACE("자동차 이름에 공백을 포함할 수 없습니다."),
	CAR_NAME_INVALID_CHAR("자동차 이름은 한글, 영문, 숫자만 사용 가능합니다."),
	CAR_NAME_DUPLICATE("중복된 자동차 이름이 있습니다."),
	ATTEMPT_COUNT_EMPTY("시도 횟수를 입력해주세요."),
	ATTEMPT_COUNT_NOT_NUMBER("시도 횟수는 숫자여야 합니다."),
	ATTEMPT_COUNT_OUT_OF_RANGE("시도 횟수는 1 이상 20 이하여야 합니다.");

	private static final String ERROR_PREFIX = "[ERROR] ";
	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return ERROR_PREFIX + message;
	}
}
