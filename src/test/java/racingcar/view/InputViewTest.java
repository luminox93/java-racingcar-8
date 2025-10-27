package racingcar.view;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InputViewTest {

	@Test
	void InputView_객체를_생성할_수_있다() {
		// when
		InputView inputView = new InputView();

		// then
		assertThat(inputView).isNotNull();
	}
}
