package racingcar.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import racingcar.domain.Car;
import racingcar.domain.Cars;
import racingcar.domain.MovementGenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OutputViewTest {

	private OutputView outputView;
	private ByteArrayOutputStream outputStream;
	private PrintStream originalOut;

	@BeforeEach
	void setUp() {
		outputView = new OutputView();
		outputStream = new ByteArrayOutputStream();
		originalOut = System.out;
		System.setOut(new PrintStream(outputStream));
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
	}

	@Test
	void 결과_헤더를_출력한다() {
		// when
		outputView.printResultHeader();

		// then
		String output = outputStream.toString();
		assertThat(output).contains("실행 결과");
	}

	@Test
	void 자동차_위치를_출력한다() {
		// when
		outputView.printCarPosition("pobi", 3);

		// then
		String output = outputStream.toString();
		assertThat(output).contains("pobi : ---");
	}

	@Test
	void 위치가_0인_자동차를_출력한다() {
		// when
		outputView.printCarPosition("pobi", 0);

		// then
		String output = outputStream.toString();
		assertThat(output).contains("pobi :");
		assertThat(output).doesNotContain("-");
	}

	@Test
	void 라운드_구분자를_출력한다() {
		// when
		outputView.printRoundSeparator();

		// then
		String output = outputStream.toString();
		assertThat(output).isEqualTo(System.lineSeparator());
	}

	@Test
	void 라운드_결과를_출력한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> true;
		Cars movedCars = cars.moveAll(generator).moveAll(generator);

		// when
		outputView.printRoundResult(movedCars);

		// then
		String output = outputStream.toString();
		assertThat(output).contains("pobi : --");
		assertThat(output).contains("woni : --");
	}

	@Test
	void 우승자를_출력한다() {
		// given
		List<String> winners = Arrays.asList("pobi", "woni");

		// when
		outputView.printWinners(winners);

		// then
		String output = outputStream.toString();
		assertThat(output).contains("최종 우승자 : pobi, woni");
	}

	@Test
	void 단일_우승자를_출력한다() {
		// given
		List<String> winners = Arrays.asList("pobi");

		// when
		outputView.printWinners(winners);

		// then
		String output = outputStream.toString();
		assertThat(output).contains("최종 우승자 : pobi");
	}

	@Test
	void 여러_우승자를_쉼표로_구분하여_출력한다() {
		// given
		List<String> winners = Arrays.asList("pobi", "woni", "jun");

		// when
		outputView.printWinners(winners);

		// then
		String output = outputStream.toString();
		assertThat(output).contains("최종 우승자 : pobi, woni, jun");
	}
}
