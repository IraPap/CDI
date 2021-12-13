package org.ipap.services;

import lombok.EqualsAndHashCode;
import org.ipap.cdi.annotations.CustomComponent;
import org.ipap.cdi.annotations.CustomInject;

@CustomComponent
@EqualsAndHashCode
public class AutoHelper implements CarInsuranceProvider {

    public MyService getMyService() {
        return myService;
    }

    @CustomInject
    private MyService myService;
}
