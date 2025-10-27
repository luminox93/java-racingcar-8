package racingcar.domain;

import org.junit.jupiter.api.Test;
import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarsTest {

	@Test
	void 여러_대의_자동차를_생성할_수_있다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni", "jun");

		// when
		Cars cars = new Cars(names);

		// then
		assertThat(cars.getCars()).hasSize(3);
	}

	@Test
	void null_목록으로_자동차를_생성하면_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new Cars(null))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.EMPTY.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.EMPTY);
				});
	}

	@Test
	void 빈_목록으로_자동차를_생성하면_예외가_발생한다() {
		// given
		List<String> names = Arrays.asList();

		// when & then
		assertThatThrownBy(() -> new Cars(names))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.EMPTY.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.EMPTY);
				});
	}

	@Test
	void 중복된_이름이_있으면_예외가_발생한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni", "pobi");

		// when & then
		assertThatThrownBy(() -> new Cars(names))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.DUPLICATE.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.DUPLICATE);
				});
	}

	@Test
	void getCars는_불변_리스트를_반환한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni");
		Cars cars = new Cars(names);

		// when
		List<Car> carList = cars.getCars();

		// then
		assertThatThrownBy(() -> carList.add(new Car("jun")))
				.isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	void moveAll을_호출하면_모든_자동차가_이동한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> true;

		// when
		Cars movedCars = cars.moveAll(generator);

		// then
		List<Car> carList = movedCars.getCars();
		assertThat(carList.get(0).getPosition()).isEqualTo(1);
		assertThat(carList.get(1).getPosition()).isEqualTo(1);
	}

	@Test
	void moveAll은_새로운_Cars_객체를_반환한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> true;

		// when
		Cars movedCars = cars.moveAll(generator);

		// then
		assertThat(cars).isNotSameAs(movedCars);
		assertThat(cars.getCars().get(0).getPosition()).isZero();
		assertThat(movedCars.getCars().get(0).getPosition()).isEqualTo(1);
	}

	@Test
	void getWinners는_최대_위치의_자동차들을_반환한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni", "jun");
		Cars cars = new Cars(names);

		// 각 자동차가 다른 횟수로 이동: pobi 3번, woni 1번, jun 3번
		Car pobi = cars.getCars().get(0).move(true).move(true).move(true);
		Car woni = cars.getCars().get(1).move(true);
		Car jun = cars.getCars().get(2).move(true).move(true).move(true);

		// when
		List<Car> winners = Arrays.asList(pobi, woni, jun).stream()
				.filter(car -> car.getPosition() == 3)
				.toList();

		// then
		assertThat(winners).hasSize(2);
		assertThat(winners.get(0).getName()).isEqualTo("pobi");
		assertThat(winners.get(1).getName()).isEqualTo("jun");
		assertThat(winners.get(0).getPosition()).isEqualTo(3);
	}

	@Test
	void getWinnerNames는_우승자_이름_목록을_반환한다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> true;
		Cars movedCars = cars.moveAll(generator);

		// when
		List<String> winnerNames = movedCars.getWinnerNames();

		// then
		assertThat(winnerNames).containsExactly("pobi", "woni");
	}

	@Test
	void 우승자가_한_명일_경우() {
		// given
		List<String> names = Arrays.asList("pobi", "woni");
		Cars cars = new Cars(names);
		MovementGenerator generator = new MovementGenerator() {
			private int callCount = 0;

			@Override
			public boolean isMovable() {
				return callCount++ == 0;  // 첫 번째만 true
			}
		};

		// when
		Cars movedCars = cars.moveAll(generator);
		List<String> winnerNames = movedCars.getWinnerNames();

		// then
		assertThat(winnerNames).hasSize(1);
		assertThat(winnerNames).containsExactly("pobi");
	}

	@Test
	void 모든_자동차가_같은_위치면_모두_우승자다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni", "jun");
		Cars cars = new Cars(names);

		// when
		List<String> winnerNames = cars.getWinnerNames();

		// then
		assertThat(winnerNames).hasSize(3);
		assertThat(winnerNames).containsExactly("pobi", "woni", "jun");
	}
}
