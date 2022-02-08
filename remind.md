## 빠르게 되짚어 보기.

---


### 1장
<details>
<summary>
인스턴스에 있는 필드를 여러 쓰레드에서 동시에 접근 하게 되면
동시성 문제가 발생한다.
<br>
인스턴스의 필드 (주로 싱글톤 에서 자주 발생), 또는 static 같은 공용필드에 접근할 떄
<br>
특히, 스프링 빈 처럼 싱글톤 객체의 필드를 변경하며 사용할 떄 발생한다.
<br>
 <h4> 이럴 때 사용할 수 있는것이 어떤 것인가? </h4>
</summary>
<div markdown="1">

<hr/>

`ThreadLocal`

쓰레드 로컬을 사용하면 각 쓰레드마다 별도의 내부 저장소를 제공한다. 

따라서 같은 인스턴스의 쓰레드 로컬 필드에 접근해도 문제 없다.

```
private ThreadLocal<String> nameStore = new ThreadLocal<>()
```

> 참고
> 이런 동시성 문제는 지역 변수에서는 발생하지 않는다.
> 지역 변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.


</div>
</details>



<details>
<summary>
스레드 로컬의 주의사항은 어떤 것이 있는가?
</summary>
<div markdown="1">
<hr/>
쓰레드 로컬의 값을 사용 후 제거하지 않고 두면 
WAS(톰캣) 처럼 쓰레드 풀을 사용하는 경우에 심각한 문제를 발생할 수 있다.


요청이 끝나고도 쓰레드 로컬의 값을 삭제 하지 않으면,
다른 요청이 해당 쓰레드 로컬의 값을 사용 하는 위험이 있다.
이렇게 되면 userA 가 저장한 정보를 userB가 사용할 수 있는 것이다.

따라서 요청이 끝날 떄 쓰레드 로컬의 값을 `ThreadLocal.remove()`를 통해서 꼭 제거해야 한다.

</div>
</details>


### 2장
<details>
<summary>
템플릿 메서드 패턴의 목적은 무엇인가요?
</summary>
<div markdown="1">
<hr/>
<h4>변하는 것과 변하지 않는 것을 분리</h4>

템플릿이라는 틀에 변하지 않는 부분을 몰아둔다. 
그리고 일부 변하는 부분을 별도로 호출해서 해결

예를 들어, 시간을 측정과 비즈니스 로직이 있었을 때
비즈니스 로직은 변하는 부분이라 -> 추상 클래스에 추상메서드로 추상화
시간을 측정하는 것은 변하지 않는 부분 -> 위의 추상메서드에서 알고리즘의 골격을 정의


#### 참고 
- `me.doyo ng.springcorehigh.trace.template.code.AbstractTemplate.java` 
- `me.doyoung.springcorehigh.trace.template.code.TemplateMethodTest.java` 


1. 추상 클래스를 상속받는 클래스 생성해서 사용 
2. 추상 클래스를 익명클래스 바로 구현해서 사용

</div>
</details>


<details>
<summary>
템플릿 메서드 패턴의 단점은 무엇인가요?
</summary>
<div markdown="1">
<hr/>
상속을 사용한다는 점.

특히 자식 클래스가 부모 클래스와 컴파일 시점에 강하게 결합되는 의존관계 문자게 있다.

자식 클래스 입장에서는 부모 클래스의 기능을 전혀 사용하지 않는다.

상속을 받는 다는 것은 특정 부모 클래스를 의존하고 있다는 것인데,
부모 클래스의 기능을 사용하지 않는 상태의 자식클래스는 
부모 클래스를 강하게 의존하게 된다.

자식 클래스 입장에서는 부모 클래스의 기능을 전혀 사용하지 않는데,
부모 클래스를 알아야하기에 좋은 설계가 아니다.



</div>
</details>


<details>
<summary>
전략 패턴의 목적은 무엇인가요?
</summary>
<div markdown="1">
<hr/>
전략 패턴은 변하지 않느 부분을 `Context` 라는 곳에 두고, 변하는 부분을 `Strategy` 라는 
<br>
<b>인터페이스</b>를 만들고 해당 인터페이스를 구현하도록 해서 문제를 해결한다.

상속이 아니라 위임으로 문제를 해결하는 것이다.

전략을 사용하면 알고리즘을 사용하는 클라이언트와 독립적으로 알고리즘을 변경할 수 있다.
<br>
-> (선 조립, 후 실행)
-> 실행 시점에서 조립을 하고 싶다면 람다를 이용할 수 있다.


#### 참고
- `advanced/me.doyoung.springcorehigh.trace.strategy`


cf). 스프링의 의존관계 주입도 이런 방식이다.

</div>
</details>


<details>
<summary>
템플릿 콜백 패턴이란 무엇인가?
</summary>
<div markdown="1">
<hr/>
스프링에서는 ContextV2 와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이라 한다.

전략 패턴에서 Context 가 템플릿 역할을 하고, Strategy 부분이 콜백으로 넘어온다 생각하면 된다.

참고로 템플릿 콜백 패턴은 GOF 패턴은 아니고, 스프링 내부에서 이런 방식을 자주 사용하기 때문에, 스프링 안에서만 이렇게 부른다. 

전략 패턴에서 템플릿과 콜백 부분이 강조된 패턴이라 생각하면 된다.

스프링에서는 JdbcTemplate , RestTemplate , TransactionTemplate , RedisTemplate 처럼 다양한 템플릿 콜백 패턴이 사용된다.

스프링에서 이름에 XxxTemplate 가 있다면 템플릿 콜백 패턴으로 만들어져 있다 생각하면 된다.
</div>
</details>



### 3장
<details>
<summary>
프록시가 무엇인가?
</summary>
<div markdown="1">
<hr/>

클라이언트가 서버로 요청을 할 때,
중간에 대리자처럼 클라이언트의 요청이나 서버의 요청 결과를 조작할 수 있다.

- 접근 제어
  - 캐싱
  - 권한에 따른 접근 차단
  - 지연로딩
- 부가 기능을 추가
  - ex1. 요청 값이나, 응답 값을 중간에 변형 
  - 실행 시간을 측정해서 추가 로그를 남긴다.
- 프록시 체인

프록시는 서버와 같은 인터페이스를 사용해서
서버 객체를 프록시 객체로 변경해도 클라이언트 코드를 변경하지 않고 동작 할 수 있어야 한다.




</div>
</details>


<details>
<summary>
프록시를 이용하는 프록시 패턴의 목적은 무엇인가?
</summary>
<div markdown="1">
<hr/>
다른 개체에 대한 접근을 제어하기 위해 대리자를 제공

### 참고
- `proxy/hello.proxy.pureproxy.proxy`
</div>
</details>


<details>
<summary>
프록시를 이용하는 데코레이터 패턴의 목적은 무엇인가?
</summary>
<div markdown="1">
<hr/>
객체에 추가 책임(기능)을 동적으로 추가하고, 기능 확장을 위한 유연한 대안 제공

### 참고
-  `proxy/hello.proxy.pureproxy.decorator`

</div>
</details>



<details>
<summary>
클래스 기반의 프록시의 단점이 무엇인가?
</summary>
<div markdown="1">
<hr/>

자바 기본 문법에 의해 자식 클래스를 생성할 때는 
항상 `super()`로 부모 클래스의 생성자를 호출해야한다.

이 부분을 생략하면 기본 생성자가 자동으로 호출 되는데,
만약 부모클래스의 기본 생성자가 없다면 생성자에서 파라미터 1개를 필수로 받는다.

만약 부모의 기능을 사용하지 않을 경우 `super(null)` 을 입력해도 되지만
인터페이스 기반 프록시는 이런 고민을 하지 않아도 된다.

또한, 클래스의 final 키워드나 메소드의 final 키워드가 붙으면 상속의 어려움이 있다.

#### 참고
- `OrderControllerConcreteProxy`
</div>
</details>



<details>
<summary>
인터페이스 기반의 프록시의 단점은 무엇인가?
</summary>
<div markdown="1">
<hr/>

[//]: # (// TODO)
</div>
</details>


### 4장

<details>
<summary>
동적 프록시가 무엇인가?
</summary>
<div markdown="1">
<hr/>

프록시를 적용하기 위해서는 적용대상 만큼의 프록시 클래스를 만들어 주는 어려움이 있는데,
동적 프록시 기술은 개발자가 직접 프록시 클래스를 만들지 않아도 된다.
프록시 객체를 동적으로 개발자 대신 만들어준다.

대표적인 예시로 JDK 동적 프록시, CGLIB 가 있다. 

</div>
</details>



<details>
<summary>
JDK 동작 프록시는 무엇인가?
</summary>
<div markdown="1">
<hr/>

JDK 동작 프록시는 인터페이스를 기반으로 프록시를 동적으로 만들어준다. 따라서 인터페이스가 필수이다.
`InvocationHandler` 라는 인터페이스를 구현해서 작성하면 된다.

</div>
</details>



<details>
<summary>
JDK 동작 프록시 한계는 무엇인가?
</summary>
<div markdown="1">
<hr/>
인터페이스가 필수적이다 보니 클래스만 있는 경우에는 적용하기 어렵다.

따라서  CGLIB 라는 바이트코드를 조작하는 특별한 라이브러리를 사용해야 한다.
</div>
</details>



<details>
<summary>
CGLIB 가 무엇인가?
</summary>
<div markdown="1">
<hr/>

바이트 코드를 조작해서 동적으로 클래스를 생성하는 기술을 제공하는 라이브러리.

인터페이스가 없어도 구체 클래스만 가지고 동적 프록시를 만들어 낼 수 있다.
단, 클래스 기반 프록시는 상속을 사용하기에 몇가지 제약이 있다.

- 부모 클래스의 생성자를 체크해야한다. -> 기본 생성자 필요
- 클래스나 클래스 의 final 키워드가 붙으면 상속, 오버라이딩 불가능 하다.


#### 참고
- `proxy/hello.proxy.cglib` 

</div>
</details>


### 5장

<details>
<summary>
프록시 팩토리가 무엇인가?
</summary>
<div markdown="1">
<hr/>

스프링은 유사한 구체적인 기술들이 있을 때, 그것들을 통합해서 일관성 있게 접근할 수 있고,
더욱 편리하게 사용할 수 있는 추상화된 기술을 제공한다.

스프링은 동적 프록시를 통합해서 편리하게 만들어주는 프록시 팩토리(ProxyFactory) 라는 기능을 제공한다.

- 인터페이스가 있으면 JDK 동적 프록시 사용 (MethodInterceptor 상속)
- 구체 클래스 라면 CGLIB 를 사용 (MethodInterceptor 구현)

`org.aopalliance.intercet.MethodInterceptor` 를 구현한 `Advice` 를 작성한다.

</div>
</details>



<details>
<summary>
포인트컷이란 무엇인가?
</summary>
<div markdown="1">
<hr/>
어디에 부가 기능을 적용할지, 않할지를 판단하는 필터링 조직이다.
<br/>
이름 그대로 어떤 포인트(Point)에 기능을 적용할지 않을지 잘라서(cut) 구분하는 것이다.
</div>
</details>



<details>
<summary>
어드바이스란 무엇인가?
</summary>
<div markdown="1">
<hr/>
프록시가 호출하는 부가 기능이다. 프록시 로직으로 말할 수 있다.
</div>
</details>



<details>
<summary>
어드바이저란 무엇인가?
</summary>
<div markdown="1">
<hr/>
포인트컷 하나와 어드바이스 하나를 묶는 것을 의미한다.
</div>
</details>

### 6장

<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>



<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>



<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>



<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>



<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>



<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>



<details>
<summary>

</summary>
<div markdown="1">
<hr/>

</div>
</details>

