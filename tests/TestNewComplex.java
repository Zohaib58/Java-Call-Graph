package tests;

public class TestNewComplex {

    public void mainMethod() {
        int x = 5;
        int y = 3;

        a();

        if (x > 0) {
            b();

            for (int i = 0; i < y; i++) {
                c();

                if (i % 2 == 0) {
                    d();
                } else {
                    e();
                }
            }
        } else if (x == 0) {
            while (y > 0) {
                f();
                y--;
            }
        } else {
            do {
                g();
            } while (x < -5);
        }

        switch (x) {
            case 1:
                h();
                break;
            case 2:
                i();
                break;
            default:
                j();
        }

        k();
    }

    public void a() {
        System.out.println("In method a()");
    }

    public void b() {
        System.out.println("In method b()");
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

    public void j() {
        System.out.println("In method j()");
    }

    public void k() {
        System.out.println("In method k()");
    }
}
