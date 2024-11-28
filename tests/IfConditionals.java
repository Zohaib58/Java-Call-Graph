package tests;

public class IfConditionals {
    private int x = 1; 
    private int y = 2; 
    public void a() {
        a(); 
        if (x > 0) {
            b(); 
        } else if (x == 0) {
            c(); 
        } else {
            d(); 
        }

        switch (y) {
            case 1:
                e(); 
                break;
            case 2:
                f(); 
                break;
            default:
                g(); 
        }
    }
    public void b() {
        c(); 
    }
    public void c() {
        System.out.println("Hello"); // External library call
    }
}
