public class TestIfElse {

    public void mainMethod() {
        int x = 5;

        if (x > 0) {
            a();
        } else {
            b();
        }

        c();
    }

    public void a() {
        System.out.println("Method a");
    }

    public void b() {
        System.out.println("Method b");
    }

    public void c() {
        System.out.println("Method c");
    }
}

