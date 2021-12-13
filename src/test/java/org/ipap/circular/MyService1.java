package org.ipap.circular;

import org.ipap.cdi.annotations.CustomComponent;
import org.ipap.cdi.annotations.CustomInject;

@CustomComponent
public class MyService1 {

    @CustomInject
    private MyService2 myService2;
}
