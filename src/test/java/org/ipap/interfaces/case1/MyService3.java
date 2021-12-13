package org.ipap.interfaces.case1;

import org.ipap.cdi.annotations.CustomComponent;
import org.ipap.cdi.annotations.CustomInject;

@CustomComponent
public class MyService3 {

    @CustomInject
    private SadInterface sadInterface;
}
