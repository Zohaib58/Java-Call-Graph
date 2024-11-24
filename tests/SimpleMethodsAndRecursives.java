package tests;

public class SimpleMethodsAndRecursives {

    public void a() {
        a(); // Recursive call
        b();
    }

    public void b() {
        c();
    }

    public void c() {
        System.out.println("Hello");
    }
}

