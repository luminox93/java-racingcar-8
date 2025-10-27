<div align="center">

![header](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6&height=280&section=header&text=Racing%20Car&fontSize=80&fontAlignY=35&desc=자동차%20경주%20게임&descAlignY=58&descSize=25)

</div>

<br>

## 📋 프로젝트 개요

### 🎯 목표
주어진 횟수 동안 n대의 자동차가 전진 또는 멈추는 경주 게임을 구현하며, **객체지향 설계 원칙**과 **테스트 가능한 코드**를 작성한다.

### 🏗️ 설계 의도

**1. 계층 분리 (MVC 패턴)**
- `View`: 사용자 입출력만 담당 (`InputView`, `OutputView`)
- `Controller`: 전체 흐름 제어 및 계층 간 연결 (`RacingGameController`)
- `Domain`: 핵심 비즈니스 로직 (`Car`, `Cars`, `RacingGame`, `Validator`)

**2. 단일 책임 원칙 (SRP)**
- `CarNameValidator`: 자동차 이름 검증만 담당 (검증 로직 분리)
- `AttemptCountValidator`: 시도 횟수 검증만 담당
- `MovementGenerator`: 전진 여부 결정만 담당
- `InputView`: 사용자 입력만 담당 (검증은 Validator에 위임)
- `OutputView`: 출력 형식만 담당
- 각 클래스는 **하나의 변경 이유**만 가짐

**3. 일급 컬렉션 활용**
- `Cars` 클래스로 자동차 목록을 감싸서 관리
- 컬렉션 관련 비즈니스 로직(우승자 찾기, 이동 처리)을 `Cars`에 집중
- 외부에서 내부 컬렉션을 직접 조작하지 못하도록 캡슐화

**4. 불변 객체 설계**
- `Car` 객체는 이동 시 새로운 객체를 반환 (기존 객체 상태 변경 없음)
- 모든 필드를 `final`로 선언하여 불변성 보장
- 예측 가능한 동작과 부작용(side effect) 방지

**5. 테스트 가능한 구조**
- `MovementGenerator`를 인터페이스로 분리하여 랜덤값 테스트 가능
- 각 계층을 독립적으로 테스트 가능
- 검증 로직을 Validator로 분리하여 단위 테스트 용이
- `CarNameValidator`는 JUnit 5와 AssertJ로 테스트 완료

**6. 커스텀 예외와 타입 안전성**
- `InvalidCarNameException`은 `IllegalArgumentException`을 상속 (요구사항 충족)
- `ErrorType` enum으로 예외 유형을 타입 안전하게 분류 (EMPTY, DUPLICATE, INVALID_LENGTH, WHITESPACE, SPECIAL_CHAR)
- `ErrorMessage`에서 예외 타입에 따라 사용자 친화적 메시지 제공
- 예외 발생 지점과 메시지 표시 로직 분리
- 커스텀 예외 사용으로 도메인 의도를 명확히 표현

### ⚙️ 실행 흐름
```
1. 사용자 입력
   ├─ 자동차 이름 입력 (쉼표 구분)
   └─ 시도 횟수 입력

2. 입력 검증
   ├─ CarNameValidator: 이름 검증 (null, 빈 리스트, 길이, 빈 문자열, 공백, 특수문자, 중복)
   │  └─ 검증 실패 시 InvalidCarNameException 발생 (ErrorType으로 분류)
   └─ AttemptCountValidator: 횟수 검증 (빈 입력, 숫자 형식, 범위)

3. 게임 초기화
   ├─ Cars 일급 컬렉션 생성
   └─ RacingGame 객체 생성 (히스토리 관리)

4. 경주 진행 (라운드 반복)
   ├─ 각 자동차마다 랜덤값 생성 (0~9)
   ├─ 4 이상이면 전진 (새 Car 객체 반환)
   ├─ 라운드 결과를 히스토리에 저장
   └─ 현재 라운드 상태 출력

5. 우승자 결정
   ├─ Cars에서 최대 위치 찾기
   ├─ 최대 위치와 같은 자동차들 필터링
   └─ 단독/공동 우승자 출력
```

<br>

## ✨ 기능 요구 사항

> **게임 규칙**
> - 주어진 횟수 동안 n대의 자동차는 전진 또는 멈출 수 있다.
> - 각 자동차에 이름을 부여할 수 있다. 전진하는 자동차를 출력할 때 자동차 이름을 같이 출력한다.
> - 자동차 이름은 쉼표(`,`)를 기준으로 구분하며 이름은 5자 이하만 가능하다.
> - 사용자는 몇 번의 이동을 할 것인지를 입력할 수 있어야 한다.
> - 전진하는 조건은 0에서 9 사이에서 무작위 값을 구한 후 무작위 값이 4 이상일 경우이다.
> - 자동차 경주 게임을 완료한 후 누가 우승했는지를 알려준다. 우승자는 한 명 이상일 수 있다.
> - 우승자가 여러 명일 경우 쉼표(`,`)를 이용하여 구분한다.

> **예외 처리**
>
> **자동차 이름 입력 검증** (`InvalidCarNameException` 발생)
> - **null 입력**: `null` 입력 시 `ErrorType.EMPTY`
>   - 메시지: `[ERROR] 자동차 이름을 입력해주세요.`
> - **빈 리스트**: 빈 리스트인 경우 `ErrorType.EMPTY`
>   - 메시지: `[ERROR] 자동차 이름을 입력해주세요.`
> - **빈 문자열**: 빈 문자열이 포함된 경우 (예: `"pobi,,jun"`) `ErrorType.EMPTY`
>   - 메시지: `[ERROR] 자동차 이름을 입력해주세요.`
> - **이름 길이**: 이름이 1자 미만 또는 5자를 초과하는 경우 `ErrorType.INVALID_LENGTH`
>   - 메시지: `[ERROR] 자동차 이름은 1자 이상 5자 이하여야 합니다.`
> - **공백 포함**: 이름에 공백(띄어쓰기, 탭)이 포함된 경우 (예: `"po bi"`) `ErrorType.WHITESPACE`
>   - 메시지: `[ERROR] 자동차 이름에 공백을 포함할 수 없습니다.`
> - **특수문자 포함**: 한글, 영문, 숫자를 제외한 특수문자가 포함된 경우 `ErrorType.SPECIAL_CHAR`
>   - 메시지: `[ERROR] 자동차 이름은 한글, 영문, 숫자만 사용 가능합니다.`
> - **중복 이름**: 중복된 자동차 이름이 있는 경우 (대소문자 구분) `ErrorType.DUPLICATE`
>   - 메시지: `[ERROR] 중복된 자동차 이름이 있습니다.`
>
> **시도 횟수 입력 검증** (`IllegalArgumentException` 발생)
> - **빈 입력**: 아무것도 입력하지 않은 경우
>   - 메시지: `[ERROR] 시도 횟수를 입력해주세요.`
> - **숫자가 아닌 값**: 시도 횟수에 숫자가 아닌 값이 입력된 경우
>   - 메시지: `[ERROR] 시도 횟수는 숫자여야 합니다.`
> - **0 이하의 값**: 0 이하의 값이 입력된 경우 `IllegalArgumentException` 발생
> - **상한값 초과**: 20을 초과하는 값이 입력된 경우 `IllegalArgumentException` 발생
>
> ⚠️ 예외 발생 시 애플리케이션은 종료되어야 한다.

<br>

---

## 🎮 게임 로직 플로우

### 📊 전체 게임 플로우차트

```mermaid
flowchart TD
    Start([게임 시작]) --> Input1[자동차 이름 입력 요청]
    Input1 --> GetNames[사용자 입력 받기]
    GetNames --> ValidateNames{이름 검증}

    ValidateNames -->|빈 입력| Error1[IllegalArgumentException:<br/>입력 오류]
    ValidateNames -->|5자 초과| Error1
    ValidateNames -->|빈 문자열 포함| Error1
    ValidateNames -->|공백 포함| Error1
    ValidateNames -->|특수문자 포함| Error1
    ValidateNames -->|중복 이름| Error1
    ValidateNames -->|통과| Input2[시도 횟수 입력 요청]

    Input2 --> GetAttempts[사용자 입력 받기]
    GetAttempts --> ValidateAttempts{횟수 검증}

    ValidateAttempts -->|빈 입력| Error2[IllegalArgumentException:<br/>입력 오류]
    ValidateAttempts -->|숫자 아님| Error2
    ValidateAttempts -->|0 이하| Error2
    ValidateAttempts -->|20 초과| Error2
    ValidateAttempts -->|통과| Init[게임 초기화]

    Init --> CreateCars[자동차 객체 생성<br/>Cars 일급 컬렉션]
    CreateCars --> RaceStart[경주 시작<br/>실행 결과 출력]

    RaceStart --> RoundCheck{모든 라운드<br/>완료?}
    RoundCheck -->|아니오| RoundStart[라운드 시작]

    RoundStart --> CarLoop[각 자동차 순회]
    CarLoop --> GenerateRandom[랜덤값 생성<br/>0-9 사이]
    GenerateRandom --> MoveCheck{값이<br/>4 이상?}

    MoveCheck -->|예| MoveForward[전진<br/>위치 +1]
    MoveCheck -->|아니오| Stay[정지<br/>위치 유지]

    MoveForward --> NextCar{다음<br/>자동차?}
    Stay --> NextCar

    NextCar -->|있음| CarLoop
    NextCar -->|없음| PrintRound[라운드 결과 출력<br/>각 자동차 위치]
    PrintRound --> RoundCheck

    RoundCheck -->|예| FindWinner[우승자 결정<br/>최대 위치 찾기]
    FindWinner --> CheckWinners{우승자<br/>수}

    CheckWinners -->|1명| PrintSingle[단독 우승자 출력]
    CheckWinners -->|2명 이상| PrintMultiple[공동 우승자 출력<br/>쉼표로 구분]

    PrintSingle --> End([게임 종료])
    PrintMultiple --> End

    Error1 --> Terminate([프로그램 종료])
    Error2 --> Terminate

    style Start fill:#e1f5e1
    style End fill:#e1f5e1
    style Terminate fill:#ffe1e1
    style Error1 fill:#ffcccc
    style Error2 fill:#ffcccc
    style MoveForward fill:#cce5ff
    style Stay fill:#f0f0f0
```

### 🔄 계층별 상호작용 (시퀀스 다이어그램)

```mermaid
sequenceDiagram
    actor User as 사용자
    participant InputView as InputView
    participant Controller as RacingGameController
    participant NameValidator as CarNameValidator
    participant CountValidator as AttemptCountValidator
    participant Cars as Cars
    participant Car as Car
    participant Game as RacingGame
    participant Generator as MovementGenerator
    participant OutputView as OutputView

    User->>InputView: 자동차 이름 입력
    InputView->>Controller: 이름 문자열 전달

    Controller->>NameValidator: 이름 검증 (빈 입력)
    Controller->>NameValidator: 이름 검증 (길이 5자 이하)
    Controller->>NameValidator: 이름 검증 (공백 포함)
    Controller->>NameValidator: 이름 검증 (특수문자 포함)
    Controller->>NameValidator: 이름 검증 (중복 체크)

    alt 검증 실패
        NameValidator-->>Controller: IllegalArgumentException
        Controller-->>User: 프로그램 종료
    else 검증 성공
        Controller->>Cars: 자동차 목록 생성
        Cars->>Car: 각 자동차 생성
    end

    User->>InputView: 시도 횟수 입력
    InputView->>Controller: 횟수 전달

    Controller->>CountValidator: 횟수 검증 (빈 입력)
    Controller->>CountValidator: 횟수 검증 (숫자 형식)
    Controller->>CountValidator: 횟수 검증 (1-20 범위)

    alt 검증 실패
        CountValidator-->>Controller: IllegalArgumentException
        Controller-->>User: 프로그램 종료
    else 검증 성공
        Controller->>Game: 게임 초기화
    end

    OutputView->>User: 실행 결과 헤더 출력

    loop 각 라운드
        Controller->>Game: 라운드 진행
        loop 각 자동차
            Game->>Generator: 랜덤값 생성
            Generator-->>Game: 0-9 사이 값
            Game->>Car: 이동 여부 결정 (4 이상)
            Car-->>Game: 새로운 Car 객체 반환
        end
        Game-->>Controller: 라운드 결과
        Controller->>OutputView: 결과 출력 요청
        OutputView->>User: 각 자동차 위치 표시
    end

    Controller->>Game: 우승자 결정
    Game->>Cars: 최대 위치 찾기
    Cars-->>Game: 우승자 목록
    Game-->>Controller: 우승자 반환
    Controller->>OutputView: 우승자 출력 요청
    OutputView->>User: 최종 우승자 표시
```

### 🏎️ 자동차 이동 결정 로직

```mermaid
flowchart LR
    Start([자동차]) --> Generate[랜덤값 생성<br/>Randoms.pickNumberInRange 0, 9]
    Generate --> Check{값 >= 4?}
    Check -->|예<br/>4,5,6,7,8,9| Forward[전진<br/>position + 1]
    Check -->|아니오<br/>0,1,2,3| Stay[정지<br/>position 유지]
    Forward --> NewCar[새 Car 객체 생성<br/>불변성 유지]
    Stay --> Return[기존 Car 반환]
    NewCar --> End([반환])
    Return --> End

    style Forward fill:#cce5ff
    style Stay fill:#f0f0f0
    style NewCar fill:#ffffcc
```

### 🏆 우승자 결정 로직

```mermaid
flowchart TD
    Start([모든 자동차]) --> FindMax[최대 위치값 찾기<br/>max position]
    FindMax --> Filter[최대 위치와 같은<br/>자동차들 필터링]
    Filter --> Count{우승자 수}

    Count -->|1명| Single[단독 우승자]
    Count -->|2명 이상| Multiple[공동 우승자]

    Single --> FormatSingle[최종 우승자 : pobi]
    Multiple --> FormatMultiple[최종 우승자 : pobi, jun<br/>쉼표 + 공백으로 구분]

    FormatSingle --> End([출력])
    FormatMultiple --> End

    style Single fill:#ffd700
    style Multiple fill:#ffd700
```

<br>

---

## 🏗️ 프로젝트 구조 설계

### 📦 패키지 구조
```
racingcar/
├── Application.java           (메인 실행)
├── controller/                (흐름 제어)
│   └── RacingGameController.java  (전체 실행 흐름 제어)
├── domain/                    (비즈니스 로직)
│   ├── Car.java               (개별 자동차)
│   ├── RacingGame.java        (경주 게임 전체 관리)
│   ├── Cars.java              (자동차 일급 컬렉션)
│   ├── CarNameValidator.java (자동차 이름 검증)
│   ├── CarNameConstants.java (자동차 이름 상수)
│   ├── AttemptCountValidator.java (시도 횟수 검증)
│   ├── MovementGenerator.java (전진 여부 결정)
│   └── exception/             (예외 클래스)
│       ├── InvalidCarNameException.java (자동차 이름 예외)
│       ├── InvalidAttemptCountException.java (시도 횟수 예외)
│       └── ErrorType.java     (예외 타입 분류)
└── view/                      (입출력)
    ├── InputView.java         (사용자 입력 처리)
    ├── OutputView.java        (결과 출력 처리)
    └── messages/              (메시지 상수)
        ├── InputMessage.java  (입력 메시지)
        ├── OutputMessage.java (출력 메시지)
        └── ErrorMessage.java  (에러 메시지)
```

### 🎯 클래스 역할 및 책임

**\`Application\`**
- 프로그램의 시작점
- Controller를 생성하고 실행

**\`controller/RacingGameController\`**
- 전체 실행 흐름 제어 (의존성 관리)
- View와 Domain 계층 연결
- 입력 → 게임 초기화 → 경주 진행 → 우승자 결정 → 출력 흐름 관리

**\`domain/Car\`**
- 개별 자동차의 상태 관리 (이름, 위치)
- 전진 로직 수행
- 불변성 유지 (위치 변경 시 새 객체 반환)
- 이름 검증 (빈 문자열, 길이 체크)
- assertion을 통한 내부 상태 검증

**\`domain/CarNameConstants\`**
- 자동차 이름 관련 상수 관리
- MIN_NAME_LENGTH (최소 이름 길이: 1)
- MAX_NAME_LENGTH (최대 이름 길이: 5)
- 유틸리티 클래스로 인스턴스화 방지

**\`domain/RacingGame\`**
- 경주 게임 전체 진행 로직
- 라운드별 자동차 이동 처리
- 라운드별 상태 히스토리 관리
- 우승자 결정

**\`domain/Cars\`**
- 자동차 목록 일급 컬렉션
- 자동차 생성 및 관리
- 우승자 필터링

**\`domain/CarNameValidator\`**
- 자동차 이름 유효성 검증
- 이름 길이 체크 (1~5자, 커스텀 설정 가능)
- null 입력, 빈 리스트 체크
- 빈 문자열, 공백 포함 체크 (공백, 탭)
- 특수문자 포함 체크 (한글/영문/숫자만 허용)
- 중복 이름 체크 (대소문자 구분)
- 생성자 파라미터 검증으로 방어적 프로그래밍 적용
- `InvalidCarNameException` 예외 발생
- `CarNameConstants`를 통한 상수 관리

**\`domain/exception/InvalidCarNameException\`**
- 자동차 이름 검증 실패 시 발생하는 커스텀 예외
- `ErrorType` enum으로 예외 유형 분류 (EMPTY, DUPLICATE, INVALID_LENGTH, WHITESPACE, SPECIAL_CHAR)
- `IllegalArgumentException` 상속

**\`domain/exception/InvalidAttemptCountException\`**
- 시도 횟수 검증 실패 시 발생하는 커스텀 예외
- `ErrorType` enum으로 예외 유형 분류 (EMPTY, NOT_NUMBER, OUT_OF_RANGE)
- `IllegalArgumentException` 상속

**\`domain/AttemptCountValidator\`**
- 시도 횟수 유효성 검증
- 빈 입력 체크 (null, 빈 문자열)
- 숫자 형식 검증 (NumberFormatException 처리)
- 범위 검증 (1 이상 20 이하, 커스텀 설정 가능)
- 생성자 파라미터 검증으로 방어적 프로그래밍 적용
- `InvalidAttemptCountException` 예외 발생

**\`domain/MovementGenerator\`**
- 전진 여부 결정 로직
- \`Randoms.pickNumberInRange(0, 9)\` 활용
- 4 이상일 때 전진

**\`view/InputView\`**
- 자동차 이름 입력 안내 및 받기 (`readCarNames()`)
- 시도 횟수 입력 안내 및 받기 (`readAttemptCount()`)
- \`Console.readLine()\`을 통한 입력 처리
- 입력만 담당, 검증은 Validator에 위임

**\`view/OutputView\`**
- 실행 결과 헤더 출력 (`printResultHeader()`)
- 개별 자동차 위치 출력 (`printCarPosition()`)
- 라운드 전체 결과 출력 (`printRoundResult()`)
- 라운드 구분선 출력 (`printRoundSeparator()`)
- 최종 우승자 출력 (`printWinners()`)
- 형식: `자동차이름 : ---`, 우승자: `최종 우승자 : pobi, jun`

**\`view/messages/InputMessage\`**
- 입력 관련 메시지 상수 관리
- REQUEST_CAR_NAMES, REQUEST_ATTEMPT_COUNT

**\`view/messages/OutputMessage\`**
- 출력 관련 메시지 상수 관리
- RESULT_HEADER, WINNERS_PREFIX

**\`view/messages/ErrorMessage\`**
- 에러 메시지 상수 중앙 관리
- `InvalidCarNameException`과 매핑하여 사용자 친화적 메시지 제공
- `[ERROR]` 접두사 자동 추가
- `from()` 메서드로 예외 타입에 따른 메시지 반환

<br>

---

## 📝 구현할 기능 목록

### 1️⃣ 입력 처리 (\`InputView\`)
- [x] "경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)" 출력
- [x] 자동차 이름 문자열 입력 받기 (\`Console.readLine()\` 사용)
- [x] "시도할 횟수는 몇 회인가요?" 출력
- [x] 시도 횟수 입력 받기 (\`Console.readLine()\` 사용)

### 2️⃣ 자동차 이름 검증 (\`CarNameValidator\`)
- [x] null 입력 검증
- [x] null 입력 시 \`InvalidCarNameException\` 발생
- [x] 빈 리스트 검증 (아무것도 입력하지 않은 경우)
- [x] 빈 리스트 시 \`InvalidCarNameException\` 발생
- [x] 자동차 이름이 1~5자 범위인지 검증
- [x] 이름이 범위를 벗어나면 \`InvalidCarNameException\` 발생
- [x] 빈 문자열 이름 검증 (예: "pobi,,jun")
- [x] 빈 문자열이 있으면 \`InvalidCarNameException\` 발생
- [x] 이름에 공백(띄어쓰기, 탭) 포함 검증
- [x] 공백이 포함되면 \`InvalidCarNameException\` 발생
- [x] 특수문자 포함 검증 (한글/영문/숫자만 허용)
- [x] 특수문자가 포함되면 \`InvalidCarNameException\` 발생
- [x] 중복된 자동차 이름 검증 (대소문자 구분)
- [x] 중복 이름이 있으면 \`InvalidCarNameException\` 발생
- [x] 커스텀 설정 지원 (최대/최소 길이 설정 가능)
- [x] 테스트 코드 작성 완료 (CarNameValidatorTest.java)

### 3️⃣ 자동차 생성 및 관리 (\`Cars\`, \`Car\`)
- [x] 쉼표(\`,\`)를 기준으로 자동차 이름 분리
- [x] 각 이름으로 \`Car\` 객체 생성
- [x] 자동차 초기 위치는 0
- [x] 불변 객체로 설계 (final 필드)
- [x] 전진 시 새로운 Car 객체 반환
- [x] 생성자에서 이름 검증 수행
- [x] private 생성자로 내부 상태 보장
- [ ] 자동차 목록을 일급 컬렉션으로 관리

### 4️⃣ 시도 횟수 검증 (\`AttemptCountValidator\`)
- [x] 빈 입력 검증 (아무것도 입력하지 않은 경우)
- [x] 빈 입력 시 \`InvalidAttemptCountException\` 발생
- [x] 입력값이 숫자인지 검증
- [x] 숫자가 아니면 \`InvalidAttemptCountException\` 발생
- [x] 범위 검증 (1 이상 20 이하)
- [x] 범위를 벗어나면 \`InvalidAttemptCountException\` 발생
- [x] 커스텀 설정 지원 (최소/최대 범위 설정 가능)
- [x] 생성자 파라미터 검증 (방어적 프로그래밍)

### 5️⃣ 경주 진행 (\`RacingGame\`, \`MovementGenerator\`)
- [ ] 주어진 횟수만큼 라운드 반복
- [x] 각 라운드마다 모든 자동차에 대해:
  - [x] 0~9 사이의 무작위 값 생성 (\`Randoms.pickNumberInRange(0, 9)\`)
  - [x] 값이 4 이상이면 전진
  - [x] 값이 4 미만이면 정지
- [ ] 각 라운드 결과를 반환

### 6️⃣ 우승자 결정 (\`RacingGame\`, \`Cars\`)
- [ ] 모든 자동차 중 최대 이동 거리 찾기
- [ ] 최대 이동 거리를 가진 자동차들을 우승자로 결정
- [ ] 우승자가 여러 명일 경우 리스트로 반환

### 7️⃣ 출력 처리 (\`OutputView\`)
- [x] 빈 줄 출력 후 "실행 결과" 출력
- [x] 각 라운드별 실행 결과 출력
  - [x] 형식: \`자동차이름 : -\` (이동 거리만큼 \`-\` 출력)
  - [x] 각 자동차마다 한 줄씩 출력
  - [x] 라운드 사이에 빈 줄 추가
- [x] 최종 우승자 출력
  - [x] 단독 우승: \`최종 우승자 : pobi\`
  - [x] 공동 우승: \`최종 우승자 : pobi, jun\` (쉼표와 공백으로 구분)

### 8️⃣ 전체 흐름 (\`RacingGameController\`)
- [ ] 자동차 이름 입력 받기
- [ ] 자동차 이름 유효성 검증
- [ ] 자동차 객체 생성
- [ ] 시도 횟수 입력 받기
- [ ] 시도 횟수 유효성 검증
- [ ] 경주 게임 초기화
- [ ] 경주 진행 및 각 라운드 결과 출력
- [ ] 우승자 결정 및 출력
- [ ] 예외 발생 시 애플리케이션 종료

<br>

---

## 🚀 보다 구체화된 구현 목록 (선택)

<details>
<summary><b>1️⃣ 불변 객체 설계</b></summary>

**Car 불변성**
- [ ] Car 필드를 \`final\`로 선언
- [ ] 위치 변경 시 새로운 Car 객체 반환
- [ ] getter만 제공 (setter 없음)

**Cars 불변성**
- [ ] 내부 리스트를 불변 리스트로 관리
- [ ] 방어적 복사 적용

</details>

<details>
<summary><b>2️⃣ 테스트 코드 작성</b></summary>

**Car 테스트**
- [ ] 자동차 생성 테스트
- [ ] 전진 테스트
- [ ] 위치 조회 테스트

**Cars 테스트**
- [ ] 자동차 이름 목록으로 생성 테스트
- [ ] 우승자 필터링 테스트
- [ ] 빈 이름 예외 테스트
- [ ] 이름 길이 초과 예외 테스트

**RacingGame 테스트**
- [ ] 경주 진행 테스트
- [ ] 우승자 결정 테스트 (단독)
- [ ] 우승자 결정 테스트 (공동)

**CarNameValidator 테스트**
- [x] 유효한 이름 검증 테스트 (영문, 한글, 숫자 조합)
- [x] 5자 초과 예외 테스트 (`ErrorType.INVALID_LENGTH`)
- [x] 빈 리스트 예외 테스트 (`ErrorType.EMPTY`)
- [x] null 입력 예외 테스트 (`ErrorType.EMPTY`)
- [x] 빈 문자열 예외 테스트 (`ErrorType.EMPTY`)
- [x] 공백 포함 예외 테스트 (`ErrorType.WHITESPACE`) - 파라미터화 테스트
- [x] 특수문자 포함 예외 테스트 (`ErrorType.SPECIAL_CHAR`) - 파라미터화 테스트
- [x] 중복 이름 예외 테스트 (`ErrorType.DUPLICATE`)
- [x] 커스텀 설정 테스트 (최대/최소 길이 설정)

**MovementGenerator 테스트**
- [ ] 전진 조건 테스트 (4 이상)
- [ ] 정지 조건 테스트 (4 미만)

</details>

<details>
<summary><b>3️⃣ 일급 컬렉션 활용</b></summary>

- [ ] Cars 클래스로 자동차 목록 래핑
- [ ] 컬렉션 관련 비즈니스 로직 집중
- [ ] 불변성 보장
- [ ] 의미 있는 메서드 제공 (우승자 필터링 등)

</details>

<details>
<summary><b>4️⃣ 랜덤 값 테스트 전략</b></summary>

- [ ] \`MovementGenerator\` 인터페이스화
- [ ] 테스트용 고정값 생성기 구현
- [ ] 프로덕션용 랜덤 생성기 구현
- [ ] 의존성 주입을 통한 테스트 용이성 확보

</details>

<br>

---

## 💻 실행 결과 예시

### ✅ 정상 실행 (단독 우승)
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,woni,jun
시도할 횟수는 몇 회인가요?
5

실행 결과
pobi : -
woni :
jun : -

pobi : --
woni : -
jun : --

pobi : ---
woni : --
jun : ---

pobi : ----
woni : ---
jun : ----

pobi : -----
woni : ----
jun : -----

최종 우승자 : pobi
```

### ✅ 정상 실행 (공동 우승)
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,woni,jun
시도할 횟수는 몇 회인가요?
5

실행 결과
pobi : -
woni :
jun : -

pobi : --
woni : -
jun : --

pobi : ---
woni : --
jun : ---

pobi : ----
woni : ---
jun : ----

pobi : -----
woni : ----
jun : -----

최종 우승자 : pobi, jun
```

### ❌ 예외 발생 (이름 길이 초과)
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,javaji,jun
Exception in thread "main" racingcar.domain.exception.InvalidCarNameException: INVALID_LENGTH
	at racingcar.domain.CarNameValidator.validateLength(CarNameValidator.java:82)
	...
[ERROR] 자동차 이름은 1자 이상 5자 이하여야 합니다.
```

### ❌ 예외 발생 (중복 이름)
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,jun,pobi
Exception in thread "main" racingcar.domain.exception.InvalidCarNameException: DUPLICATE
	at racingcar.domain.CarNameValidator.validateNoDuplicateName(CarNameValidator.java:74)
	...
[ERROR] 중복된 자동차 이름이 있습니다.
```

### ❌ 예외 발생 (특수문자 포함)
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,jun@
Exception in thread "main" racingcar.domain.exception.InvalidCarNameException: SPECIAL_CHAR
	at racingcar.domain.CarNameValidator.validateNoSpecialCharacters(CarNameValidator.java:98)
	...
[ERROR] 자동차 이름은 한글, 영문, 숫자만 사용 가능합니다.
```

### ❌ 예외 발생 (잘못된 시도 횟수)
```
경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)
pobi,woni,jun
시도할 횟수는 몇 회인가요?
0
Exception in thread "main" java.lang.IllegalArgumentException: 시도 횟수는 1 이상이어야 합니다.
```

<br>

---

## ⚙️ 프로그래밍 요구 사항

| 항목 | 요구 사항 |
|------|-----------|
| **JDK 버전** | JDK 21 |
| **시작점** | \`Application\`의 \`main()\` |
| **빌드 설정** | \`build.gradle\` 변경 금지 |
| **외부 라이브러리** | 제공된 라이브러리 외 사용 금지 |
| **종료 처리** | \`System.exit()\` 사용 금지 |
| **코드 스타일** | Java Style Guide |
| **들여쓰기** | depth 3 이하 (2까지만 허용) |
| **3항 연산자** | 사용 금지 |
| **함수 길이** | 한 가지 일만 하도록 최대한 작게 |
| **입력 처리** | \`camp.nextstep.edu.missionutils.Console.readLine()\` 사용 |
| **랜덤 값** | \`camp.nextstep.edu.missionutils.Randoms.pickNumberInRange(0, 9)\` 사용 |
| **테스트** | JUnit 5와 AssertJ 사용 |

<br>


<div align="center">

![footer](https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6&height=150&section=footer)

</div>
