package racingcar.view;

import racingcar.domain.Car;
import racingcar.domain.Cars;
import racingcar.view.messages.OutputMessage;

import java.util.List;

public class OutputView {
	private static final String POSITION_DELIMITER = " : ";
	private static final String POSITION_MARK = "-";
	private static final String NAME_SEPARATOR = ", ";

	public void printResultHeader() {
		System.out.println();
		System.out.println(OutputMessage.RESULT_HEADER.getMessage());
	}

	public void printRoundResult(Cars cars) {
		List<Car> carList = cars.getCars();
		for (Car car : carList) {
			printCarPosition(car.getName(), car.getPosition());
		}
		printRoundSeparator();
	}

	public void printCarPosition(String carName, int position) {
		System.out.println(carName + POSITION_DELIMITER + POSITION_MARK.repeat(position));
	}

	public void printRoundSeparator() {
		System.out.println();
	}

	public void printWinners(List<String> winners) {
		System.out.println();
		System.out.print(OutputMessage.WINNERS_PREFIX.getMessage());
		System.out.println(String.join(NAME_SEPARATOR, winners));
	}
}
