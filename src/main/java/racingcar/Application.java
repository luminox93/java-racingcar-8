package racingcar;

import racingcar.controller.RacingCarController;
import racingcar.domain.RandomMovementGenerator;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class Application {
    public static void main(String[] args) {
        RacingCarController controller = new RacingCarController(
                new InputView(),
                new OutputView(),
                new RandomMovementGenerator()
        );
        controller.run();
    }
}
