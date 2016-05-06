package com.webnobis.configuration;

import javax.inject.Inject;
import javax.inject.Named;

public class B {

    @Inject
    @Named("B.a")
    private int a;

    private final Integer b1;

    private final boolean b2;

    private final C b3;

    @Inject
    public B(@Named("B.b1") Integer b1, @Named("B.b2") boolean b2, C b3) {
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    public int getA() {
		return a;
	}

	public Integer getB1() {
		return b1;
	}

	public boolean isB2() {
		return b2;
	}

	public C getB3() {
		return b3;
	}

	@Override
    public String toString() {
        return "B [a=" + a + ", b1=" + b1 + ", b2=" + b2 + ", b3=" + b3 + "]";
    }

}
