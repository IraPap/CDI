package org.ipap.services;

import lombok.EqualsAndHashCode;
import org.ipap.cdi.annotations.CustomComponent;

import java.util.UUID;

@CustomComponent
@EqualsAndHashCode
public class SomeService {

    private String someServiceID = UUID.randomUUID().toString();
}
