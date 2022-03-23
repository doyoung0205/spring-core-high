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
빈 후처리기가 무엇인가?
</summary>
<div markdown="1">
<hr/>
스프링이 빈 저장소에 등록할 목적으로 생성한 객체를 빈 저장소에 등록하기 직전에 조작하고 싶다면 빈후처리기를 사용하면 된다.

`BeanPostProcessor` 를 구현하여 빈 후처리기를 만들 수 있다.
</div>
</details>



<details>
<summary>
프록시를 빈 후처리기로 처리했을 때 문제점은 ? 
</summary>
<div markdown="1">
<hr/>

1. ProxyFactoryConfig 가 많아진다.
2. 컴포넌트 스캔에 등록된 빈들은 자동으로 등록하기 때문에 프록시 적용이 불가능 하다

</div>
</details>



<details>
<summary>
자동 프록시 생성기 - AutoProxyCreator 는 무엇인가?
</summary>
<div markdown="1">
<hr/>

`AnnotationAwareAspectJAutoProxyCreator` 라는 빈 후처리기가 스프링 빈에 자동 등록된다.
`Advisor` 또는 `@Aspect` 등을 자동으로 인식해서 프록시가 필요한 곳에 자동으로 등록한다.



자동 프록시 생성기를 사용하려면
```    
implementation 'org.springframework.boot:spring-boot-starter-aop' 
```
의존성 주입을 받아야한다.
그리고, `@EnableAspectJAutoProxy` 를 직접 사용해야하지만 스프링부트에서는 자동으로 처리해준다.




</div>
</details>


### 7장

<details>
<summary>
@Aspect 프록시 란 무엇인가?
</summary>
<div markdown="1">
<hr/>

포인트컷과 어드바이스로 구성되어 있는 어드바이저 생성 기능을 지원하는 어노테이션이다.


#### 참고 

- `aop/hello.aop.pointcut.AtAnnotationTest.java`

</div>
</details>


### 8장


<details>
<summary>
AOP 란 무엇인가?
</summary>
<div markdown="1">
<hr/>

부가 기능을 핵심 기능에서 분리하고 한 곳에서 관리할 수 있도록
해당 부가 기능을 어디에 적용할지 선택하고 어떤 부가 기능을 적용할지 정의한 것.

</div>
</details>



<details>
<summary>
AOP 적용 방식 3가지가 무엇인가?
</summary>
<div markdown="1">
<hr/>

1. 컴파일 시점

.java 에서 .class 하는 과정에서 AspectJ에서 제공하는 특별한 컴파일을 사용해서 위빙을 사용하여 적용한다.

- 컴파일 시점에 부가 기능을 적용하려면 특별한 컴파일러도 필요하고 복잡하다. 
2. 클래스 로딩 시점

.class 파일을 JVM 내부의 클래스 로더에 보관하는데 
중간에 java Instrumentation 를 활용하여 클래스 로더에 조작하여 보관한다. 

- 로드 타임 위빙은 자바를 실행할 때 특별한 옵션( java -javaagent )을 통해 클래스 로더 조작기를 지정해야 하는데, 이 부분이 번거롭고 운영하기 어렵다. 

3. 런타임 시점(프록시)

런타임 시점은 컴파일도 다 끝나고, 클래스 로더에 클래스도 다 올라가서 이미 자바가 실행되고 난 다음을 의미한다.

따라서 스프링과 같은 컨테이너의 도움을 받고 프록시와 DI, 빈 포스트 프로세서 같은 개념들을 총 동원해야 한다.

이렇게 하면 최종적으로 프록시를 통해 스프링 빈에 부가 기능을 적용할 수 있다.


</div>
</details>



<details>
<summary>
조인 포인트 란 무엇인가?
</summary>
<div markdown="1">
<hr/>

- 어드바이스가 적용될 수 있는 위치, 메소드 실행, 생성자 호출, 필드 값 접근, static 메서드 접근 같은 프로그램 실행 중 지점
- AOP를 적용할 수 있는 모든 지점
- 스프링 AOP 는 프록시 방식을 사용하므로 조인 포인트는 항상 메소드 실행 지점으로 제한된다.

</div>
</details>


<details>
<summary>
포인트컷 이란 무엇인가?
</summary>
<div markdown="1">
<hr/>

- 조인 포인트 중에서 어드바이스가 적용될 위치를 선별하는 기능
- 주로 AspectJ 표현식을 사용해서 지정
- 프록시를 사용하는 스프링 AOP는 메서드 실행 지점만 포인트컷으로 선별 가능

</div>
</details>


<details>
<summary>
AOP의 타겟(Target) 이란 무엇인가
</summary>
<div markdown="1">
<hr/>

어드바이스를 받는 객체, 포인트컷으로 결정

</div>
</details>


<details>
<summary>
어드바이스란 무엇인가?
</summary>
<div markdown="1">
<hr/>

- 부가 기능
- 특정 조인 포인트에서 Aspect에 의해 취해지는 조치
- Around(주변), Before(전), After(후)와 같은 다양한 종류의 어드바이스가 있음

</div>
</details>


<details>
<summary>
에스펙트란 무엇인가?
</summary>
<div markdown="1">
<hr/>

- 어드바이스 + 포인트컷을 모듈화 한 것
- @Aspect 를 생각하면 됨
- 여러 어드바이스와 포인트 컷이 함께 존재

</div>
</details>


<details>
<summary>
어드바이저(Advisor) 란 무엇인가?
</summary>
<div markdown="1">
<hr/>

- 하나의 어드바이스와 하나의 포인트 컷으로 구성
- 스프링 AOP 에서만 사용되는 특별한 용어

</div>
</details>



<details>
<summary>
위빙 (Weaving) 이란 무엇인가?
</summary>
<div markdown="1">
<hr/>

- 포인트컷으로 결정한 타겟의 조인 포인트에 어드바이스를 적용하는 것
- 위빙을 통해 핵심 기능 코드에 영향을 주지 않고 부가 기능을 추가 할 수 있음
- AOP 적용을 위해 애스펙트를 객체에 연결한 상태
  - 컴파일 타임(AspectJ compiler)
  - 로드 타임
  - 런타임, 스프링 AOP는 런타임, 프록시 방식
  
</div>
</details>


### 9장

<details>
<summary>
어드바이스의 순서를 보장 받고 싶다면 어떻게 해야하는가?
</summary>
<div markdown="1">
<hr/>

어드바이스 단위가 아니라 클래스 단위로 분리를 해서 `@Order` 어노테이션을 통해 순서를 보장할 수 있다.

</div>
</details>



<details>
<summary>
스프링 AOP 의 어드바이스 종류는 무엇이 있는가? 
</summary>
<div markdown="1">
<hr/>

- @Around: 메서드 호출 전후에 수행, **조인 포인트 실행 여부 선택**, 반환 값 변환, 예외 변환 등이 가능
- @Before: 조인 포인트 실행 이전에 실행
- @AfterReturning: 조인 포인트가 정상 완료후 실행
- @AfterThrowing: 메서드가 예외를 던지는 경우 실행
- @After: 조인 포인트가 정상 또는 예외에 관곙벗이 실행(finally)


</div>
</details>



<details>
<summary>
@Around vs 나머지 어드바이스의 joinpoint 차이점
</summary>
<div markdown="1">
<hr/>

@Around 는 ProceedingJoinPoint 을 사용해야 한다. proceed()

</div>
</details>


### 10장

<details>
<summary>
포인트컷 중 execution 의 문법이 무엇인가?
</summary>
<div markdown="1">
<hr/>

execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)

- 메소드 실행 조인 포인트를 매칭한다.
- ?는 생략 할 수 있다.
- `*` 같은 패턴을 지정할 수 있다.


```
execution(* *(..)
execution(public String hello.aop.member.MemberServiceImpl.hello(String))
```

부모 타입 허용

```
execution(* hello.aop.member.MemberService.*(..))
```

</div>
</details>



<details>
<summary>
다음 포인트컷 지시자는 단독으로 사용하면 안되는 이유는 무엇인가? 

`args, @args, @target`

</summary>
<div markdown="1">
<hr/>

`args, @args, @target` 는 실제 객체 인스턴스가 생성되고 실행될 때 어드바이스 적용 여부를 확인할 수 있다.

실행 시점에 일어나는 포인트컷 적용 여부도 결국 프록시가 있어야 실행 시점에 판단할 수 있다.
<br/>
프록시가 없다면 판단 자체가 불가능하다. 그런데 스프링 컨테이너가 프록시를 생성하는 시점은 스프링 컨테이너가 만들어지는 
애플리케이션 로딩 시점에 적용할 수 있다.

따라서 `args, @args, @target` 같은 포인트컷 지시자가 있으면 스프링은 모든 스프링 빈에 AOP를 적용하려고 시도한다.
앞서 설명한 것 처럼 프록시가 없으면 실행 시점에 판단 자체가 불가능하다.

문제는 이렇게 모든 스프링 빈에 AOP 프록시를 적용하려고 하면 스프링이 내부에서 사용하는 빈 중에는 

`final`로 지정된 빈들도 있기 때문에 오류가 발생할 수 있다.

> 따라서 이러한 표현식은 최대한 프록시 적용 대상을 축소하는 표현식과 함께 사용해야 한다.



</div>
</details>


### 11장 (중요)

<details>
<summary>
프록시와 내부 호출 문제에 대해서 설명하시오.
</summary>
<div markdown="1">
<hr/>

AOP를 적용하려면 항상 프록시를 통해서 대상 객체(Target)을 호출해야 한다.

이렇게 해야 프록시에서 먼저 어드바이스를 호출하고, 이후에 대상 객체를 호출한다.

하지만, 대상 객체의 내부에서 메서드 호출이 발생하면 프록시를 거치지 않고 대상 객체를 직접 호출 하는 문제가 발생한다.



</div>
</details>



<details>
<summary>
프록시와 내부 호출 문제 대안을 설명하시오.
</summary>
<div markdown="1">
<hr/>

1. 자기 자신 주입

```java
/**
* 참고: 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
*/
  @Slf4j
  @Component
  public class CallServiceV1 {
    private CallServiceV1 callServiceV1;
  }

```

스프링 부트 2.6부터는 순환 참조를 기본적으로 금지하도록 정책이 변경되었다.
따라서 
> spring.main.allow-circular-references=true 설정이 필수다.


2. 대안2 지연 조회

```
private final ObjectProvider<CallServiceV2> callServiceProvider;
```

스프링 컨테이너에서 조회하는 것을 스프링 빈 생성 시점이 아니라 실제 객체를 사용하는 지점으로 지연할 수 있다.
따라서 순환 사이클이 발생하지 않으면서 프록시 객체를 사용할 수 있다.


3. 대안3 구조 변경

가장 나은 대안은 내부 호출이 발생하지 않도록 구조를 변경하는 것이다.
즉, 기존에 내부 메서드 호출을 클래스를 따로 만들어 외부 메서드 호출로 변경하는 것


</div>
</details>



<details>
<summary>
public 메서드에만 AOP가 적용되는 이유는?
</summary>
<div markdown="1">
<hr/>

AOP는 주로 트랜잭션 적용이나 주요 컴포넌트의 로그 출력 기능에 사용된다.
쉽게 이야기해서 인터페이스에 메서드가 나올 정도의 규모에 AOP를 적용하는 것이 적당하다.

더 풀어서 이야기하자면 AOP는 `public` 메서드에만 적용한다.

AOP 적용을 위해 `private` 메서드를 외부 클래스로 변경하고 `public` 으로 변경하는 일은 거의 없다.

`public` 메서드에서 `public` 메서드를 내부 호출하는 경우에는 문제가 발생한다.

따라서 AOP 가 잘 적용되지 않으면 내부 호출을 의심해보자.

</div>
</details>



<details>
<summary>
JDK 동적 프록시 한계가 무엇인가?
</summary>
<div markdown="1">
<hr/>

인터페이스 기반으로 프록시를 생성하는 JDK 동적 프록시는 구체 클래스로 타입 캐스팅이 불가능한 한계가 있다.

JDK 동적 Proxy 에서는 interface 타입이 타겟 클래스 타입이고 이를 실제 interface 를 구현하는 

구현체 타입으로 변경하는데 있어서 예외가 발생한다.

반면에 CGLIB 프록시는 타겟 클래스 타입이 실제 구현체 타입과 같아 캐스팅의 문제가 없다.


</div>
</details>



<details>
<summary>
CGLIB 프록시의 한계 를 스프링은 어떻게 개선 하였는가?
</summary>
<div markdown="1">
<hr/>

- 기본 생성자 필수 
  - -> 스프링 4.0부터 objenesis 라이브러리를 통해 기본 생성자 없어도 생성 가능하게 해줌
- 생성자 2번 호출 (1. 실제 target 객체 생성시, 2. target 객체 생성시 target 부모 클래스 생성자 호출)
  - -> 스프링 4.0부터 objenesis 라이브러리 덕분에 가능
- final 키워드 클래스, 메서드 사용 불가
  - 일반적인 웹 어플리케이션을 개발할 때는 final 을 잘 사용하지 않기 때문에 특별한 문제가 되지 않음.

</div>
</details>


