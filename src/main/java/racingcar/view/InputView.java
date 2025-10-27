package racingcar.view;

import camp.nextstep.edu.missionutils.Console;
import racingcar.view.messages.InputMessage;

public class InputView {

	public String readCarNames() {
		System.out.println(InputMessage.REQUEST_CAR_NAMES.getMessage());
		return Console.readLine();
	}

	public String readAttemptCount() {
		System.out.println(InputMessage.REQUEST_ATTEMPT_COUNT.getMessage());
		return Console.readLine();
	}
}
