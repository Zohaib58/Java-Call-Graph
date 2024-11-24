## How to Run Tool from \Fj Calls>
Compile: javac -cp lib\javaparser-core-3.26.2.jar -d bin src\*.java
Run: java -cp "lib\javaparser-core-3.26.2.jar;bin" Main

Provide the file path where your source code resides else TestComplex.java 
under tests folder would be used asinput

## How is Nesting Shown in the output
Input Code:
public class TestComplex {

    public void main() {
        int x = 5;
        int y = 3;
        if (x > 0) {
            for (int i = 0; i < y; i++) {
                h();
                if (i % 2 == 0) {
                    c();
                } 
            }
        } else if (x == 0) {
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
    }\
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

Output:


![image](https://github.com/user-attachments/assets/64a99f89-2d58-48f5-8477-080f73976a24)

## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
