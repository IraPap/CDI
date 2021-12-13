package org.ipap.services;

import lombok.EqualsAndHashCode;
import org.ipap.cdi.annotations.CustomInject;
import org.ipap.cdi.annotations.CustomQualifier;
import org.ipap.cdi.annotations.CustomSingletonComponent;

import java.util.UUID;

@CustomSingletonComponent
@EqualsAndHashCode
public class MyService {

    public String getMyServiceID() {
        return myServiceID;
    }

    public OtherService getOtherService() {
        return otherService;
    }

    public CarInsuranceProvider getCarInsuranceProvider() {
        return carInsuranceProvider;
    }

    private String myServiceID = UUID.randomUUID().toString();

    private OtherService otherService;

    private CarInsuranceProvider carInsuranceProvider;

    @CustomInject
    public MyService(OtherService otherService, @CustomQualifier(value = "Wheelzz") CarInsuranceProvider carInsuranceProvider) {
        this.otherService = otherService;
        this.carInsuranceProvider = carInsuranceProvider;
    }
}
