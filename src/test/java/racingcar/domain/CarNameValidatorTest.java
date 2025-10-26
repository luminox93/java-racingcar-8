package racingcar.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import racingcar.domain.exception.InvalidCarNameException;
import racingcar.domain.exception.InvalidCarNameException.ErrorType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CarNameValidator 테스트")
public class CarNameValidatorTest {

	private CarNameValidator validator;

	@BeforeEach
	void setUp() {
		validator = new CarNameValidator();
	}

	@Test
	@DisplayName("정상적인 자동차 이름은 예외가 발생하지 않는다")
	void 정상적인_자동차_이름은_예외가_발생하지_않는다() {
		// given
		List<String> names = Arrays.asList("pobi", "kiki", "soob");

		// when & then
		assertThatCode(() -> validator.validate(names))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("자동차 이름이 5글자 초과면 예외가 발생한다")
	void 자동차_이름이_5글자_초과면_예외가_발생한다() {
		// given
		List<String> names = Arrays.asList("toolong", "soob");

		// when & then
		assertThatThrownBy(() -> validator.validate(names))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.INVALID_LENGTH);
	}

	@Test
	@DisplayName("빈 입력은 예외가 발생한다")
	void 빈_입력은_예외가_발생한다() {
		// given
		List<String> names = Collections.emptyList();

		// when & then
		assertThatThrownBy(() -> validator.validate(names))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.EMPTY);
	}

	@Test
	@DisplayName("null 입력은 예외가 발생한다")
	void null_입력은_예외가_발생한다() {
		// when & then
		assertThatThrownBy(() -> validator.validate(null))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.EMPTY);
	}

	@ParameterizedTest
	@ValueSource(strings = {"po bi", "ki\tki", "j un"})
	@DisplayName("공백이 포함된 이름은 예외가 발생한다")
	void 공백이_포함된_이름은_예외가_발생한다(String invalidName) {
		// given
		List<String> names = Arrays.asList("pobi", invalidName);

		// when & then
		assertThatThrownBy(() -> validator.validate(names))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.WHITESPACE);
	}

	@ParameterizedTest
	@ValueSource(strings = {"jun@", "po#bi", "ki!ki", "test*"})
	@DisplayName("특수문자가 포함된 이름은 예외가 발생한다")
	void 특수문자가_포함된_이름은_예외가_발생한다(String invalidName) {
		// given
		List<String> names = Arrays.asList("pobi", invalidName);

		// when & then
		assertThatThrownBy(() -> validator.validate(names))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.SPECIAL_CHAR);
	}

	@Test
	@DisplayName("중복된 이름은 예외가 발생한다")
	void 중복된_이름은_예외가_발생한다() {
		// given
		List<String> names = Arrays.asList("pobi", "jun", "pobi");

		// when & then
		assertThatThrownBy(() -> validator.validate(names))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.DUPLICATE);
	}

	@Test
	@DisplayName("빈 문자열이 포함되면 예외가 발생한다")
	void 빈_문자열이_포함되면_예외가_발생한다() {
		// given
		List<String> names = Arrays.asList("pobi", "", "jun");

		// when & then
		assertThatThrownBy(() -> validator.validate(names))
			.isInstanceOf(InvalidCarNameException.class)
			.hasFieldOrPropertyWithValue("errorType", ErrorType.EMPTY);
	}

	@Test
	@DisplayName("한글 이름은 정상 처리된다")
	void 한글_이름은_정상_처리된다() {
		// given
		List<String> names = Arrays.asList("포비", "준", "브라운");

		// when & then
		assertThatCode(() -> validator.validate(names))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("숫자가 포함된 이름은 정상 처리된다")
	void 숫자가_포함된_이름은_정상_처리된다() {
		// given
		List<String> names = Arrays.asList("car1", "car2", "car3");

		// when & then
		assertThatCode(() -> validator.validate(names))
			.doesNotThrowAnyException();
	}

	@Test
	@DisplayName("커스텀 설정으로 Validator를 생성할 수 있다")
	void 커스텀_설정으로_Validator를_생성할_수_있다() {
		// given
		CarNameValidator customValidator = new CarNameValidator(10, 1);
		List<String> names = Arrays.asList("longnameok", "short");

		// when & then
		assertThatCode(() -> customValidator.validate(names))
			.doesNotThrowAnyException();
	}
}
