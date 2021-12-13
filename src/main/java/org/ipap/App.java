package org.ipap;

import org.ipap.cdi.exceptions.CircularDependencyException;
import org.ipap.cdi.exceptions.IllegalCreationException;
import org.ipap.cdi.injector.CustomDI;
import org.ipap.services.AutoHelper;
import org.ipap.services.CarInsuranceProvider;
import org.ipap.services.MyService;
import org.ipap.services.OtherService;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IllegalCreationException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, CircularDependencyException {

        CustomDI customDI = new CustomDI("org.ipap.services");
        System.out.println(customDI.getDiObjects());

        AutoHelper autoHelper = (AutoHelper) customDI.getDiObjects().stream().filter(AutoHelper.class::isInstance).findFirst().orElse(null);
        MyService myService = (MyService) customDI.getDiObjects().stream().filter(MyService.class::isInstance).findFirst().orElse(null);
        OtherService otherService = (OtherService) customDI.getDiObjects().stream().filter(OtherService.class::isInstance).findFirst().orElse(null);

        System.out.println(autoHelper.getMyService().getMyServiceID() + " should equal " + myService.getMyServiceID());

        System.out.println(otherService.getOtherServiceID() + " should be different from " + myService.getOtherService().getOtherServiceID());

        Set<CarInsuranceProvider> providers = customDI.getDiObjects().stream().filter(CarInsuranceProvider.class::isInstance).map(CarInsuranceProvider.class::cast).collect(Collectors.toSet());
        System.out.println("There are " + providers.size() + " different card insurance providers");




    }
}
