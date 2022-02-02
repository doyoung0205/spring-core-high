CGLIB

"CGLIB: Code Generator Library"
- CGLIB는 바이트코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리이다.
- CGLIB를 사용하면 인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어낼 수 있다.
- CGLIB는 원래 외부라이브러리 이지만, 스프링 프레임워크가 스프링 내부 소스 코드에 포함했다.
따라서 라이브러리를 추가하지 않아도 사용할 수 있다.

```
참고로 CGLIB를 직접 사용하는 경우는 거의 없다.
이후에 설명할 스프링의 `ProxyFactory` 라는 기술이 편리하게 사용하게 도와주기 때문에,
너무 깊이있게 파기 보다는 개념만 잡으면 된다.
```

JDK 동적 프록시에서 실행 로직을 위해 `InvocationHandler`를 제공했듯이, CGLIB는 `MethodInterceptor`를 제공한다.

성능상 method 보다 methodProxy 를 사용하는 것을 권장


### CGLIB 제약
- 클래스 기반 프록시는 상속을 사용하기 때문에 몇가지 제약이 있다.
  - 부모 클래스의 생성자를 체크해야 한다. -> CGLIB는 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다.
  - 클래스에 `final` 키워드가 붙으면 상속이 불가능 하다. -> CGLIB에서는 예외가 발생한다.
  - 메서드에 `final` 키워드가 붙으면 해당 메서드를 오버라이딩 할 수 없다. -> CGLIB에서는 프록시 로직이 동작하지 않는다.

### 참고

CGLIB 를 사용하면 인터페이스가 없는 V2 애플리케이션에 동적 프록시를 적용할 수 있다.
그런데 지금 당장 적용하기에는 몇가지 제약이 있다.
V2 애플리케이션에 기본 생성자를 추가하고, 의존관계를 'setter'를 사용해서 주입하면 CGLIB를 적용할 수 있다.
하지만 다음에 학습하는 'ProxyFactory'를 통해서 CGLIB를 적용하면 어떤 단점을 해결하고 또 더 편리하기 떄문에
애플리케이션에 CGLIB로 프록시를 적용하는 것은 조금 뒤에 알아보겠다.


### 남은 문제
- 인터페이스가 있는 경우 JDK 동적 프록시를 적용하고, 그렇지 않은 경우에는 CGLIB를 적용하려면 어떻게 해야 할까?
- 두 기술을 함께 사용할 때 부가 기능을 제공하기 위해 각각 JDK 동적프록시의 Hanlder 와 CGLIB 의 Interceptor 를 각각 중복으로 만들어야 할까?
- 특정 조건에 맞을 때 프록시 로직을 적용하는 기능도 공통으로 제공되었으면? 