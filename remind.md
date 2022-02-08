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

<details>
<summary>

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


<details>
<summary>
Q
</summary>
<div markdown="1">
<hr/>
A
</div>
</details>
