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
Q
</summary>
<div markdown="1">
<hr/>
A
</div>
</details>


<details>
<summary>
Q
</summary>
<div markdown="1">
<hr/>
A
</div>
</details>


<details>
<summary>
Q
</summary>
<div markdown="1">
<hr/>
A
</div>
</details>
