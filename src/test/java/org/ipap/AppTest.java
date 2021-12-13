package org.ipap;

import org.ipap.cdi.exceptions.CircularDependencyException;
import org.ipap.cdi.exceptions.IllegalCreationException;
import org.ipap.cdi.injector.CustomDI;
import org.ipap.services.AutoHelper;
import org.ipap.services.CarInsuranceProvider;
import org.ipap.services.MyService;
import org.ipap.services.OtherService;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void shouldThrowCircularDependencyException() {

        assertThrows(CircularDependencyException.class, () -> {
           new CustomDI("org.ipap.circular");
        });



    }

    @Test
    public void testSingletonObjects() throws IllegalCreationException, ClassNotFoundException, InvocationTargetException, CircularDependencyException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        CustomDI customDI = new CustomDI("org.ipap.services");
        AutoHelper autoHelper = (AutoHelper) customDI.getDiObjects().stream().filter(AutoHelper.class::isInstance).findFirst().orElse(null);
        MyService myService = (MyService) customDI.getDiObjects().stream().filter(MyService.class::isInstance).findFirst().orElse(null);

        assertEquals(myService, autoHelper.getMyService());


    }


    @Test
    public void testDependentObjects() throws IllegalCreationException, ClassNotFoundException, InvocationTargetException, CircularDependencyException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        CustomDI customDI = new CustomDI("org.ipap.services");
        List<OtherService> otherServices = customDI.getDiObjects().stream().filter(OtherService.class::isInstance).map(OtherService.class::cast).collect(Collectors.toList());

        assertEquals(2, otherServices.size());

        OtherService otherService1 = otherServices.get(0);
        OtherService otherService2 = otherServices.get(1);

        assertNotEquals(otherService1, otherService2);


    }

    @Test
    public void testInterfaceImpl() throws IllegalCreationException, ClassNotFoundException, InvocationTargetException, CircularDependencyException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        CustomDI customDI = new CustomDI("org.ipap.services");
        List<CarInsuranceProvider> providers = customDI.getDiObjects().stream().filter(CarInsuranceProvider.class::isInstance).map(CarInsuranceProvider.class::cast).collect(Collectors.toList());

        assertEquals(3, providers.size());

        CarInsuranceProvider car1 = providers.get(0);
        CarInsuranceProvider car2 = providers.get(1);
        CarInsuranceProvider car3 = providers.get(2);

        assertNotEquals(car1, car2);
        assertNotEquals(car2, car3);
        assertNotEquals(car3, car1);


    }

    @Test
    public void shouldThrowIllegalCreationException1() {

        assertThrows(IllegalCreationException.class, () -> {
            new CustomDI("org.ipap.interfaces.case1");
        });



    }

    @Test
    public void shouldThrowIllegalCreationException2() {

        assertThrows(IllegalCreationException.class, () -> {
            new CustomDI("org.ipap.interfaces.case2");
        });



    }

    @Test
    public void shouldThrowIllegalCreationException3() {

        assertThrows(IllegalCreationException.class, () -> {
            new CustomDI("org.ipap.interfaces.case3");
        });



    }

    @Test
    public void shouldThrowIllegalCreationException4() {

        assertThrows(IllegalCreationException.class, () -> {
            new CustomDI("org.ipap.interfaces.case4");
        });



    }


}
