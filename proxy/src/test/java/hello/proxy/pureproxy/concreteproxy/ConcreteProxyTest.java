package hello.proxy.pureproxy.concreteproxy;

import hello.proxy.pureproxy.concreteproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteLogic;
import hello.proxy.pureproxy.concreteproxy.code.ConcreteTimeProxy;
import org.junit.jupiter.api.Test;

public class ConcreteProxyTest {
    @Test
    void noProxy() {
        final ConcreteLogic concreteLogic = new ConcreteLogic();
        final ConcreteClient client = new ConcreteClient(concreteLogic);
        client.execute();
    }

    @Test
    void proxy() {
        final ConcreteLogic concreteLogic = new ConcreteLogic();
        final ConcreteTimeProxy proxy = new ConcreteTimeProxy(concreteLogic);
        final ConcreteClient client = new ConcreteClient(proxy);
        client.execute();
    }
}
