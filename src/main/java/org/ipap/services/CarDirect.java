package org.ipap.services;

import lombok.EqualsAndHashCode;
import org.ipap.cdi.annotations.CustomComponent;
import org.ipap.cdi.annotations.CustomInject;

@CustomComponent
@EqualsAndHashCode
public class CarDirect implements CarInsuranceProvider {

    @CustomInject
    private SomeService someService;
}
