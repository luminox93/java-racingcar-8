package racingcar.domain;

public enum AssertMessage {
	NAMES_NULL_IN_VALIDATE_LIST("names가 null입니다! validate() 메서드의 null 체크를 확인하세요."),
	NAMES_NULL_IN_VALIDATE_EACH_NAME("names가 null입니다! validate() 메서드의 null 체크를 확인하세요."),
	NAMES_NULL_IN_VALIDATE_LIST_NOT_EMPTY("names가 null입니다! validateList() 메서드를 확인하세요."),
	NAMES_NULL_IN_VALIDATE_NO_BLANK_NAME("names가 null입니다! validateList() 메서드를 확인하세요."),
	NAMES_NULL_IN_VALIDATE_NO_DUPLICATE_NAME("names가 null입니다! validateList() 메서드를 확인하세요."),
	NAME_NULL_IN_VALIDATE_LENGTH("name이 null입니다! validateEachName()의 forEach 로직을 확인하세요."),
	NAME_NULL_IN_VALIDATE_NO_WHITESPACE("name이 null입니다! validateEachName()의 forEach 로직을 확인하세요."),
	NAME_NULL_IN_VALIDATE_NO_SPECIAL_CHARACTERS("name이 null입니다! validateEachName()의 forEach 로직을 확인하세요.");

	private final String message;

	AssertMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
