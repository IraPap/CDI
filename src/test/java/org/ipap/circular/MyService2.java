package org.ipap.circular;

import org.ipap.cdi.annotations.CustomComponent;
import org.ipap.cdi.annotations.CustomInject;

@CustomComponent
public class MyService2 {

    @CustomInject
    private MyService1 myService1;
}
