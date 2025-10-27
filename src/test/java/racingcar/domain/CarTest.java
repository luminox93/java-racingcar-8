package racingcar.domain;

import org.junit.jupiter.api.Test;
import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarTest {

	@Test
	void 유효한_이름으로_자동차를_생성할_수_있다() {
		// given
		String name = "pobi";

		// when
		Car car = new Car(name);

		// then
		assertThat(car.getName()).isEqualTo(name);
		assertThat(car.getPosition()).isZero();
	}

	@Test
	void 한글_이름으로_자동차를_생성할_수_있다() {
		// given
		String name = "포비";

		// when
		Car car = new Car(name);

		// then
		assertThat(car.getName()).isEqualTo(name);
	}

	@Test
	void 숫자_이름으로_자동차를_생성할_수_있다() {
		// given
		String name = "12345";

		// when
		Car car = new Car(name);

		// then
		assertThat(car.getName()).isEqualTo(name);
	}

	@Test
	void null_이름으로_자동차를_생성하면_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new Car(null))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.NULL.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.NULL);
				});
	}

	@Test
	void 빈_이름으로_자동차를_생성하면_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new Car(""))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.EMPTY.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.EMPTY);
				});
	}

	@Test
	void 이름이_5자를_초과하면_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new Car("abcdef"))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.INVALID_LENGTH.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.INVALID_LENGTH);
				});
	}

	@Test
	void 이름에_공백이_포함되면_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new Car("po bi"))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.WHITE_SPACE.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.WHITE_SPACE);
				});
	}

	@Test
	void 이름에_특수문자가_포함되면_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new Car("car!"))
				.isInstanceOf(InvalidCarNameException.class)
				.hasMessage(ErrorType.SPECIAL_CHARACTER.getMessage())
				.satisfies(e -> {
					InvalidCarNameException exception = (InvalidCarNameException) e;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.SPECIAL_CHARACTER);
				});
	}

	@Test
	void 이동_조건이_true이면_자동차가_전진한다() {
		// given
		Car car = new Car("pobi");
		int initialPosition = car.getPosition();

		// when
		Car movedCar = car.move(true);

		// then
		assertThat(movedCar.getPosition()).isEqualTo(initialPosition + 1);
	}

	@Test
	void 이동_조건이_false이면_자동차가_정지한다() {
		// given
		Car car = new Car("pobi");
		int initialPosition = car.getPosition();

		// when
		Car movedCar = car.move(false);

		// then
		assertThat(movedCar.getPosition()).isEqualTo(initialPosition);
	}

	@Test
	void 자동차를_여러_번_전진시킬_수_있다() {
		// given
		Car car = new Car("pobi");

		// when
		Car movedCar = car.move(true).move(true).move(false).move(true);

		// then
		assertThat(movedCar.getPosition()).isEqualTo(3);
	}

	@Test
	void move는_불변_객체를_반환한다() {
		// given
		Car originalCar = new Car("pobi");
		int originalPosition = originalCar.getPosition();

		// when
		Car movedCar = originalCar.move(true);

		// then
		assertThat(originalCar.getPosition()).isEqualTo(originalPosition);
		assertThat(movedCar.getPosition()).isEqualTo(originalPosition + 1);
	}

	@Test
	void 새로_생성된_자동차의_초기_위치는_0이다() {
		// given & when
		Car car = new Car("pobi");

		// then
		assertThat(car.getPosition()).isZero();
	}
}
