package racingcar.domain;

import org.junit.jupiter.api.Test;
import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
				.satisfies(throwable -> {
					InvalidCarNameException exception = (InvalidCarNameException) throwable;
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
				.satisfies(throwable -> {
					InvalidCarNameException exception = (InvalidCarNameException) throwable;
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
				.satisfies(throwable -> {
					InvalidCarNameException exception = (InvalidCarNameException) throwable;
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
		assertAll(
				() -> assertThat(carList.get(0).getPosition()).isEqualTo(1),
				() -> assertThat(carList.get(1).getPosition()).isEqualTo(1)
		);
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
		assertAll(
				() -> assertThat(cars).isNotSameAs(movedCars),
				() -> assertThat(cars.getCars().get(0).getPosition()).isZero(),
				() -> assertThat(movedCars.getCars().get(0).getPosition()).isEqualTo(1)
		);
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
		assertAll(
				() -> assertThat(winners).hasSize(2),
				() -> assertThat(winners.get(0).getName()).isEqualTo("pobi"),
				() -> assertThat(winners.get(1).getName()).isEqualTo("jun"),
				() -> assertThat(winners.get(0).getPosition()).isEqualTo(3)
		);
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

	@Test
	void 자동차가_1대만_있는_경우() {
		// given
		List<String> names = Arrays.asList("pobi");

		// when
		Cars cars = new Cars(names);

		// then
		assertAll(
				() -> assertThat(cars.getCars()).hasSize(1),
				() -> assertThat(cars.getCars().get(0).getName()).isEqualTo("pobi")
		);
	}

	@Test
	void 자동차가_1대일_때_우승자는_1명이다() {
		// given
		List<String> names = Arrays.asList("pobi");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> true;

		// when
		Cars movedCars = cars.moveAll(generator);
		List<String> winnerNames = movedCars.getWinnerNames();

		// then
		assertAll(
				() -> assertThat(winnerNames).hasSize(1),
				() -> assertThat(winnerNames).containsExactly("pobi")
		);
	}

	@Test
	void 자동차가_많은_경우_모두_정상_생성된다() {
		// given
		List<String> names = Arrays.asList("car1", "car2", "car3", "car4", "car5",
				"car6", "car7", "car8", "car9", "car10");

		// when
		Cars cars = new Cars(names);

		// then
		assertThat(cars.getCars()).hasSize(10);
	}

	@Test
	void 모든_자동차가_이동하지_않으면_모두_위치_0이다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni", "jun");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> false;

		// when
		Cars movedCars = cars.moveAll(generator).moveAll(generator).moveAll(generator);

		// then
		List<Car> carList = movedCars.getCars();
		assertAll(
				() -> assertThat(carList.get(0).getPosition()).isZero(),
				() -> assertThat(carList.get(1).getPosition()).isZero(),
				() -> assertThat(carList.get(2).getPosition()).isZero()
		);
	}

	@Test
	void 모든_자동차가_이동하지_않으면_모두_우승자다() {
		// given
		List<String> names = Arrays.asList("pobi", "woni", "jun");
		Cars cars = new Cars(names);
		MovementGenerator generator = () -> false;

		// when
		Cars movedCars = cars.moveAll(generator);
		List<String> winnerNames = movedCars.getWinnerNames();

		// then
		assertAll(
				() -> assertThat(winnerNames).hasSize(3),
				() -> assertThat(winnerNames).containsExactly("pobi", "woni", "jun")
		);
	}
}
