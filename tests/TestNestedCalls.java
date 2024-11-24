package tests;

public class TestNestedCalls {

    private int x = 1; // Example values for conditions
    private int y = 5;
    private int z = 2;

    public void mainMethod() {
        if (x > 0) {
            if (y < 5) {
                b(); // Call inside nested if
            } else if (y == 5) {
                c(); // Call in else if
            } else {
                d(); // Call in else
            }
        } else {
            e(); // Call in outer else
        }

        switch (z) {
            case 1:
                if (x > 0) {
                    f(); // Call in if inside case
                } else {
                    g(); // Call in else inside case
                }
                break;
            case 2:
                h(); // Call in case 2
                break;
            default:
                // No method calls in default case
        }
    }

    public void b() {
        System.out.println("b()");
    }

    public void c() {
        System.out.println("c()");
    }

    public void d() {
        System.out.println("d()");
    }

    public void e() {
        System.out.println("e()");
    }

    public void f() {
        System.out.println("f()");
    }

    public void g() {
        System.out.println("g()");
    }

    public void h() {
        System.out.println("h()");
    }
}
