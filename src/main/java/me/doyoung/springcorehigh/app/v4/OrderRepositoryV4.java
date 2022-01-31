package me.doyoung.springcorehigh.app.v4;

/*
    동시성 문제

    결과적으로 Thread-A 입장에서는 저장한 데이터와 조회한 데이터가 다른 문제가 발생한다.
    이처럼 여러 쓰레드가 동시에 같은 인스턴스의 필드에 접근해야 하기 때문에 트래픽이 적은 상황에서는
    확율상 잘 나타나지 않고, 트래픽이 점점 많아질 수 록 자주 발생한다.
    특히, 스프링 빈 처럼 싱글톤 객체의 필드를 변경하며 사용할 때 이러한 동시성 문제를 조심해야 한다.

    참고

    이런 동시성 문제는 지역 변수에서는 발생하지 않는다. 지역 변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.
    동시성 문제가 발생하는 곳은 같은 인스턴스의 필드(주로 싱글톤에서 자주 발생), 또는 static 같은 공용 필드에 접근할 때
    발생한다.
    동시성 문제는 값을 읽기만 하면 발생하지 않는다.
    어디선가 값을 변경하기 떄문에 발생한다.
*/

import lombok.RequiredArgsConstructor;
import me.doyoung.springcorehigh.trace.TraceStatus;
import me.doyoung.springcorehigh.trace.logtrace.LogTrace;
import me.doyoung.springcorehigh.trace.template.AbstractTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {
    private final LogTrace trace;

    public void save(String itemId) {

        final AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            public Void call() {
                if (itemId.equals("ex")) {
                    throw new IllegalStateException("예외 발생!");
                }
                sleep(1000);
                return null;
            }
        };
        template.execute("OrderRepository.save()");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
