전략 패턴은 변하지 않는 부분을 'Context' 라는 곳에 두고, 변하는 부분을 'Strategy' 라는 인터페이스를 만들고 해당 인터페이스를 구현하도록 해서 문제를 해결한다. 전략 패턴에서 'Context'는
변하지 않는 템플릿 역할을 하고, 'Strategy'는 변하는 알고리즘 역할을 한다.

GOF 디자인 패턴에서 정의한 전퍅 패턴의 의도는 다음과 같다.
> 알고리즘 제품군을 정의하고 각각을 캡슐화하여 상호 교환 가능하게 만들자. 전략을 사용하면 알고리즘을 사용하는 클라이언트와 독립적으로 알고리즘을 변경할 수 있다.


거대한 문맥은 변하지않고 strategy 를 통해 일부 전략이 변경된다.

'Context'는 내부에 'Strategy strategy' 필드를 가지고 있다.
이 필드에 변하는 부분인 'Strategy' 인터페이스에만 의존한다는 점이다.
덕분에 'Strategy'의 구현체를 변경하거나 새로 만들어도 'Context' 코드에는 영향을 주지 않는다.

바로 스프링에서 의존관게 주입에서 사용하는 방식이 바로 전략 패턴이다.


"정리"
지금까지 일반적으로 이야기하는 전략 패턴에 대해서 알아보았다.
변하지 않는 부분을 'Context'에 두고 변하는 부분을 'Strategy'를 구현해서 만든다.
그리고 'Context'와 'Strategy'를 실행 전에 원하는 모양으로 조립해두고, 그다음에 'Context'를 실행하는 선 조립, 후 실행 방식에서 매우 유용한다.


"선 조립, 후 실행"
여기서 이야기하고 싶은 부분은 'Context'의 내부 필드에 'Strategy'를 두고 사용하는 부분이다.
이 방식은 'Context'와 'Strategy'를 실행 전에 원하는 모양으로 조립해두고, 그 다음에 'Context'를 실행하는 선 조립, 후 실행 방식으로 매우 유용한다.
'Context'와 'Strategy'를 한번 조립하고 나면 이후로는 'Context'를 실행하기만 하면된다.
우리가 스프링 어플리케이션을 개발할 때 애플리케이션 로딩 시점에 의존관계 주입을 통해 필요한 의존관계를 모두 맺어두고 난 다음에 실제 요청을 처리하는 것 과 같은 원리이다.

이 방식은 단점은 'Context'와 'Strategy'를 조립한 이후에는 전략을 변경하기가 번거롭다는 점이다. 물론 'Context'에 'setter'를 제공해서 'Strategy'를 넘겨 받아 변경하면 되지만,
'Context'를 싱글톤으로 사용할 때는 동시성 이슈등 고려할 점이 많다.
그래서 전략을 실시간으로 변경해야 하면 이전에 개발한 테스트 코드 처럼 'Context'를 하나더 생성하거 그곳에 다른 'Strategy'를 주입하는 것이 더 나은 선택일 수 있다.



더 유연하게 전략 패턴을 사용하는 방법은 없을까?

> 필드에 주입을 해서 사용하는게 아니라 직접 파라미터로 전달해서 사용해보자

컨텍스트를 실핼 할 때마다 동적으로 조립하는 것.
이전 방식 보다 비교해서 더 유연하게 변경할 수 있다.


### 정리

- 'ContextV1'은 필드에 'Strategy'를 저장하는 방식으로 전략 패턴을 구사했다.
  - 선 조립, 후 실행 방법에 적합하다.
  - 'Context'를 실행하는 시점에는 이미 조립이 끝났기 때문에 전략을 신경쓰지 않고 단순히 실행만 하면 된다.

- 'ContextV2'는 파라미터에 'Strategy'를 전달받는 형식으로 전략 패턴을 구사했다.
  - 실행할 때 마다 전략을 유연하게 변경할 수 있다.
  - 단점 역시 실행할 때 마다 전략을 계속 지정해주어야 한다는 점이다.

**템플릿**
지금 우리가 해결하고 싶은 문제는 변하는 부분과 변하지 않는 부분을 분리하는 것이다.
변하지 않는 부분을 템플릿 이라고 하고, 그 템플릿 안에서 변하는 부분에 약간 다른 코드 조각을 넘겨서 실행하는 것이 목적이다.
'ContextV1', 'ContextV2' 두 가지 방식 다 문제를 해결할 수 있지만, 어떤 방식이 조금 더 나아 보이는가?
지금 우리가 원하는 것은 애플리케이션 의존 관계를 설정하는 것 처럼 선 조립, 후 실행이 아니다. 단순히 코드를 실행할 때 변하지 않는
템플릿이 있고, 그 템플릿 안에서 원하는 부분만 살짝 다른 코드를 실행하고 싶을 뿐이다.
따라서 우리가 고민하는 문제는 실행 시점에 유연하게 실행 코드 조각을 전달하는 'ContextV2'가 적합하다.



