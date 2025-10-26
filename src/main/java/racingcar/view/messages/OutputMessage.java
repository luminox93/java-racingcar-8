package racingcar.view.messages;

public enum OutputMessage {
	RESULT_HEADER("실행 결과"),
	WINNERS_PREFIX("최종 우승자 : ");

	private final String message;

	OutputMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
