package org.ipap.services;

import lombok.EqualsAndHashCode;
import org.ipap.cdi.annotations.CustomComponent;

@CustomComponent
@EqualsAndHashCode
public class Wheelzz implements CarInsuranceProvider {

    private String wheelzzType;
}
