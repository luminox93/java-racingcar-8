package racingcar.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import camp.nextstep.edu.missionutils.Console;
import racingcar.view.messages.InputMessage;

public class InputView {

	public List<String> readCarNames() {
		System.out.println(InputMessage.REQUEST_CAR_NAMES.getMessage());
		String input = Console.readLine();
		return parseCarNames(input);
	}

	private List<String> parseCarNames(String input) {
		return Arrays.stream(input.split(","))
				.map(String::trim)
				.collect(Collectors.toList());
	}

	public String readAttemptCount() {
		System.out.println(InputMessage.REQUEST_ATTEMPT_COUNT.getMessage());
		return Console.readLine();
	}
}
