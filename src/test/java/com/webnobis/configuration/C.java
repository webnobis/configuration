package com.webnobis.configuration;

import javax.inject.Inject;
import javax.inject.Named;

public class C {

    @Inject
    @Named("C.a")
    private Boolean a;

    private Object unset;

    public Boolean getA() {
		return a;
	}

	public Object getUnset() {
		return unset;
	}

	@Override
    public String toString() {
        return "C [a=" + a + ", unset=" + unset + "]";
    }

	public void overwriteA(Boolean a) {
		this.a = a;
	}

}
