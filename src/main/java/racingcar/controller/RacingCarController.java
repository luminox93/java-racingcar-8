package racingcar.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import racingcar.domain.AttemptCount;
import racingcar.domain.Car;
import racingcar.domain.Cars;
import racingcar.domain.MovementGenerator;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class RacingCarController {
	private final InputView inputView;
	private final OutputView outputView;
	private final MovementGenerator movementGenerator;

	public RacingCarController(InputView inputView, OutputView outputView, MovementGenerator movementGenerator) {
		this.inputView = inputView;
		this.outputView = outputView;
		this.movementGenerator = movementGenerator;
	}

	public void run() {
		Cars cars = createCars();
		AttemptCount attemptCount = readAttemptCount();

		playRace(cars, attemptCount);
		announceWinners(cars);
	}

	private Cars createCars() {
		String input = inputView.readCarNames();
		List<String> carNames = parseCarNames(input);
		return new Cars(carNames);
	}

	private List<String> parseCarNames(String input) {
		return Arrays.stream(input.split(","))
				.map(String::trim)
				.collect(Collectors.toList());
	}

	private AttemptCount readAttemptCount() {
		String input = inputView.readAttemptCount();
		return new AttemptCount(input);
	}

	private void playRace(Cars cars, AttemptCount attemptCount) {
		outputView.printResultHeader();

		Cars currentCars = cars;
		for (int i = 0; i < attemptCount.getValue(); i++) {
			currentCars = currentCars.moveAll(movementGenerator);
			printRoundResult(currentCars);
		}
	}

	private void printRoundResult(Cars cars) {
		List<Car> carList = cars.getCars();
		for (Car car : carList) {
			outputView.printCarPosition(car.getName(), car.getPosition());
		}
		outputView.printRoundSeparator();
	}

	private void announceWinners(Cars cars) {
		List<String> winnerNames = cars.getWinners().stream()
				.map(Car::getName)
				.collect(Collectors.toList());
		outputView.printWinners(winnerNames);
	}
}
