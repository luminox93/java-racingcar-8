package racingcar.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

public class Cars {
	private final List<Car> cars;

	public Cars(List<String> names) {
		validateNames(names);
		validateNoDuplicates(names);
		this.cars = names.stream()
				.map(Car::new)
				.toList();
	}

	private Cars(List<Car> carList, boolean isCarList) {
		assert carList != null && !carList.isEmpty() : "cars는 null이거나 비어있을 수 없습니다";
		this.cars = new ArrayList<>(carList);
	}

	private void validateNames(List<String> names) {
		if (names == null || names.isEmpty()) {
			throw new InvalidCarNameException(ErrorType.EMPTY);
		}
	}

	private void validateNoDuplicates(List<String> names) {
		assert names != null : "names는 null일 수 없습니다";

		if (names.size() != new HashSet<>(names).size()) {
			throw new InvalidCarNameException(ErrorType.DUPLICATE);
		}
	}

	public List<Car> getCars() {
		return Collections.unmodifiableList(cars);
	}

	public List<Car> getWinners() {
		int maxPosition = getMaxPosition();
		return cars.stream()
				.filter(car -> car.getPosition() == maxPosition)
				.toList();
	}

	private int getMaxPosition() {
		return cars.stream()
				.mapToInt(Car::getPosition)
				.max()
				.orElse(0);
	}

	public Cars moveAll(MovementGenerator generator) {
		List<Car> movedCars = cars.stream()
				.map(car -> car.move(generator.isMovable()))
				.toList();
		return new Cars(movedCars, true);
	}
}
