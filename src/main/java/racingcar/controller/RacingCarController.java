package racingcar.controller;

import java.util.List;

import racingcar.domain.AttemptCount;
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

		Cars finalCars = playRace(cars, attemptCount);
		announceWinners(finalCars);
	}

	private Cars createCars() {
		List<String> carNames = inputView.readCarNames();
		return new Cars(carNames);
	}

	private AttemptCount readAttemptCount() {
		String input = inputView.readAttemptCount();
		return new AttemptCount(input);
	}

	private Cars playRace(Cars cars, AttemptCount attemptCount) {
		outputView.printResultHeader();

		Cars currentCars = cars;
		for (int round = 0; round < attemptCount.getValue(); round++) {
			currentCars = currentCars.moveAll(movementGenerator);
			outputView.printRoundResult(currentCars);
		}
		return currentCars;
	}

	private void announceWinners(Cars cars) {
		outputView.printWinners(cars.getWinnerNames());
	}
}
