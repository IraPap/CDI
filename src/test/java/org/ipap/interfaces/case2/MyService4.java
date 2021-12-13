package org.ipap.interfaces.case2;

import org.ipap.cdi.annotations.CustomComponent;
import org.ipap.cdi.annotations.CustomInject;

@CustomComponent
public class MyService4 {

    @CustomInject
    private PopularInterface popularInterface;
}
