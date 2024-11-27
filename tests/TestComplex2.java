public class TestComplex2 {
    public void mainMethod() {
        if (x > 0) {
            a(); // Method in 'if'
            if (y > 5) {
                b(); // Nested 'if' inside 'if'
            } else {
                c(); // Nested 'else' inside 'if'
            }
        } else {
            d(); // Method in 'else'
        }
        e(); // Method after 'if-else'
    }

    public void a() {}
    public void b() {}
    public void c() {}
    public void d() {}
    public void e() {}
}

