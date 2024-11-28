package tests;

public class IfConditionals {
    private int x = 1; 
    private int y = 2; 
    public void a() {
        a(); 
        if (x > 0) {
            b(); 
        } else if (x == 0) {
            a(); 
        } else {
            c(); 
        }

        switch (y) {
            case 1:
                b(); 
                break;
            case 2:
                c(); 
                break;
            default:
                a(); 
        }
    }
    public void b() {
        c(); 
    }
    public void c() {
        System.out.println("Hello"); // External library call
    }
}
