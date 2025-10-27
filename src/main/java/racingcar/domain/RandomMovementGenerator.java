package racingcar.domain;

import camp.nextstep.edu.missionutils.Randoms;

public class RandomMovementGenerator implements MovementGenerator {
	private static final int MIN_RANDOM_VALUE = 0;
	private static final int MAX_RANDOM_VALUE = 9;
	private static final int MOVE_THRESHOLD = 4;

	@Override
	public boolean isMovable() {
		int randomValue = Randoms.pickNumberInRange(MIN_RANDOM_VALUE, MAX_RANDOM_VALUE);
		return randomValue >= MOVE_THRESHOLD;
	}
}
