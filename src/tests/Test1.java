package tests;

public class Test1 {
    public void a() {
        b();
        c();
    }

    public void b() {
        c();
    }

    public void c() {
        System.out.println("Hello");
    }
}
