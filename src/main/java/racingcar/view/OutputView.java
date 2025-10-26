package racingcar.view;

import racingcar.view.messages.OutputMessage;

import java.util.List;
import java.util.Map;

public class OutputView {
	private static final String POSITION_DELIMITER = " : ";
	private static final String POSITION_MARK = "-";
	private static final String NAME_SEPARATOR = ", ";

	public void printResultHeader() {
		System.out.println();
		System.out.println(OutputMessage.RESULT_HEADER.getMessage());
	}

	public void printCarPosition(String carName, int position) {
		System.out.println(carName + POSITION_DELIMITER + POSITION_MARK.repeat(position));
	}

	public void printRoundResult(Map<String, Integer> carPositions) {
		carPositions.forEach(this::printCarPosition);
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
