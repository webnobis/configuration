package com.webnobis.configuration;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.inject.Named;

public class A {

    @Inject
    @Named("A.a")
    private int a;

    @Inject
    @Named("A.b")
    private boolean b;

    private final String c1;

    private final LocalDate c2;

    private final B c3;

    @Inject
    public A(@Named("A.c1") String c1, @Named("A.c2") LocalDate c2, B c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }

    public int getA() {
		return a;
	}

	public boolean isB() {
		return b;
	}

	public String getC1() {
		return c1;
	}

	public LocalDate getC2() {
		return c2;
	}

	public B getC3() {
		return c3;
	}

	@Override
    public String toString() {
        return "A [a=" + a + ", b=" + b + ", c1=" + c1 + ", c2=" + c2 + ", c3=" + c3 + "]";
    }

}
