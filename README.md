## How to Run Tool from \Fj Calls>
Compile: javac -cp lib\javaparser-core-3.26.2.jar -d bin src\*.java
Run: java -cp "lib\javaparser-core-3.26.2.jar;bin" Main

Provide the file path where your source code resides else TestComplex.java 
under tests folder would be used asinput

## How is Nesting Shown in the output
Input Code:
public class Example {

    public void mainMethod() {
        if (x > 0) {
            for (int i = 0; i < 3; i++) {
                a();
                if (i % 2 == 0) {
                    b();
                } else {
                    c();
                }
            }
        } else {
            d();
        }
    }

    public void a() {}
    public void b() {}
    public void c() {}
    public void d() {}
}

Output Code:
Call Graph:
Method: mainMethod
    a (if: x > 0 ->
        loop: for i < 3)
    b (if: x > 0 ->
        loop: for i < 3 ->
        if: i % 2 == 0)
    c (if: x > 0 ->
        loop: for i < 3 ->
        else)
    d (else)
Method: a
Method: b
Method: c
Method: d

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
