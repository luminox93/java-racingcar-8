package racingcar.controller;

import org.junit.jupiter.api.Test;
import racingcar.domain.MovementGenerator;
import racingcar.view.InputView;
import racingcar.view.OutputView;

import static org.assertj.core.api.Assertions.assertThat;

class RacingCarControllerTest {

	@Test
	void 컨트롤러_객체를_생성할_수_있다() {
		// given
		InputView inputView = new InputView();
		OutputView outputView = new OutputView();
		MovementGenerator generator = () -> true;

		// when
		RacingCarController controller = new RacingCarController(inputView, outputView, generator);

		// then
		assertThat(controller).isNotNull();
	}
}
