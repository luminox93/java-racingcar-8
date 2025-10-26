package racingcar.domain;

public class Car {
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
		validateNameNotEmpty(name);
		validateNameLength(name);
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
