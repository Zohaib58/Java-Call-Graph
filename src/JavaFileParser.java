import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.javaparser.ast.Node;


public class JavaFileParser {

    // Parse the Java file and return the AST
    public static CompilationUnit parseFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            return StaticJavaParser.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse the file: " + filePath);
        }
    }

    // Print all method declarations in the file
    public static void printMethodDeclarations(CompilationUnit cu) {
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(MethodDeclaration md, Void arg) {
                super.visit(md, arg);
                System.out.println("Method Found: " + md.getNameAsString());
            }
        }, null);
    }

    // Print all method calls in the file
    public static void printMethodCalls(CompilationUnit cu) {
        cu.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(MethodCallExpr mce, Void arg) {
                super.visit(mce, arg);
                System.out.println("Method Call Found: " + mce.getNameAsString());
            }
        }, null);
    }

    public static void printAstTree(Node node, String indent) {
        System.out.println(indent + node.getClass().getSimpleName() + ": " + node);
        for (Node child : node.getChildNodes()) {
            printAstTree(child, indent + "  ");
        }
    }
}
