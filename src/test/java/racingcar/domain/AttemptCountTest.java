package racingcar.domain;

import org.junit.jupiter.api.Test;
import racingcar.domain.exception.InvalidAttemptCountException;
import racingcar.domain.exception.InvalidAttemptCountException.ErrorType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AttemptCountTest {

	@Test
	void 유효한_숫자로_시도_횟수를_생성할_수_있다() {
		// given
		String input = "5";

		// when
		AttemptCount attemptCount = new AttemptCount(input);

		// then
		assertThat(attemptCount.getValue()).isEqualTo(5);
	}

	@Test
	void 최소값_1로_시도_횟수를_생성할_수_있다() {
		// given
		String input = "1";

		// when
		AttemptCount attemptCount = new AttemptCount(input);

		// then
		assertThat(attemptCount.getValue()).isEqualTo(1);
	}

	@Test
	void 최대값_20으로_시도_횟수를_생성할_수_있다() {
		// given
		String input = "20";

		// when
		AttemptCount attemptCount = new AttemptCount(input);

		// then
		assertThat(attemptCount.getValue()).isEqualTo(20);
	}

	@Test
	void 앞뒤_공백이_있어도_정상_처리된다() {
		// given
		String input = "  5  ";

		// when
		AttemptCount attemptCount = new AttemptCount(input);

		// then
		assertThat(attemptCount.getValue()).isEqualTo(5);
	}

	@Test
	void null_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount(null))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.EMPTY.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.EMPTY);
				});
	}

	@Test
	void 빈_문자열_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount(""))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.EMPTY.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.EMPTY);
				});
	}

	@Test
	void 공백만_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("   "))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.EMPTY.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.EMPTY);
				});
	}

	@Test
	void 숫자가_아닌_문자_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("abc"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.NOT_NUMBER.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_NUMBER);
				});
	}

	@Test
	void 특수문자_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("5!"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.NOT_NUMBER.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_NUMBER);
				});
	}

	@Test
	void 실수_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("5.5"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.NOT_NUMBER.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_NUMBER);
				});
	}

	@Test
	void 최소값보다_작은_값_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("0"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.OUT_OF_RANGE.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.OUT_OF_RANGE);
				});
	}

	@Test
	void 음수_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("-1"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.OUT_OF_RANGE.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.OUT_OF_RANGE);
				});
	}

	@Test
	void 최대값보다_큰_값_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("21"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.OUT_OF_RANGE.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.OUT_OF_RANGE);
				});
	}

	@Test
	void 매우_큰_값_입력시_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> new AttemptCount("100"))
				.isInstanceOf(InvalidAttemptCountException.class)
				.hasMessage(ErrorType.OUT_OF_RANGE.getMessage())
				.satisfies(throwable -> {
					InvalidAttemptCountException exception = (InvalidAttemptCountException) throwable;
					assertThat(exception.getErrorType()).isEqualTo(ErrorType.OUT_OF_RANGE);
				});
	}
}
