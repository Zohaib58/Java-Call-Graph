package tests;

public class IfConditionals {

    private int x = 1; // Example value: x can be 1, 0, or -1 for testing conditionals
    private int y = 2; // Example value: y can be 1, 2, or something else for switch cases

    
    public void a() {
        a(); // Simple recursive call
        if (x > 0) {
            b(); // Conditional call when x > 0
        } else if (x == 0) {
            a(); // Recursive call in if when x == 0
        } else {
            c(); // Conditional call when x < 0
        }

        switch (y) {
            case 1:
                b(); // Conditional call in switch case 1
                break;
            case 2:
                c(); // Conditional call in switch case 2
                break;
            default:
                a(); // Recursive call in default case
        }
    }

    public void b() {
        c(); // Simple call
    }

    public void c() {
        System.out.println("Hello"); // External library call
    }
}
