package tests;

public class TestComplex {

    public void mainMethod() {
        int x = 5;
        int y = 3;

        if (x > 0) {
            for (int i = 0; i < y; i++) {
                h();
                if (i % 2 == 0) {
                    c();
                } 
            }
        } else if (x > 0) {

            if (y == 0) {
                b();
            } else {
                c();
            }
            while (y > 0) {
                d();
                y--;
            }
        } else {
            do {
                f();
            } while (x < -5);
        }

        switch (x) {
            case 1:
                g();
                break;
            case 2:
                h();
                break;
            default:
                i();
        }
    }

    public void b() {
        c();
    }

    public void c() {
        System.out.println("In method c()");
    }

    public void d() {
        System.out.println("In method d()");
    }

    public void e() {
        System.out.println("In method e()");
    }

    public void f() {
        System.out.println("In method f()");
    }

    public void g() {
        System.out.println("In method g()");
    }

    public void h() {
        System.out.println("In method h()");
    }

    public void i() {
        System.out.println("In method i()");
    }
}

