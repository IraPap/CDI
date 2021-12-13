package org.ipap.services;

import lombok.EqualsAndHashCode;
import org.ipap.cdi.annotations.CustomDependentComponent;
import org.ipap.cdi.annotations.CustomInject;

import java.util.UUID;

@CustomDependentComponent
@EqualsAndHashCode
public class OtherService {

    public String getOtherServiceID() {
        return otherServiceID;
    }

    public CarInsuranceProvider getCarDirect() {
        return carDirect;
    }

    private String otherServiceID = UUID.randomUUID().toString();

    @CustomInject
    private CarInsuranceProvider carDirect;

}
