import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.*;

public class CallGraph {

    // Map to store the call graph
    private final Map<String, List<String>> callGraph = new HashMap<>();

    // Generate the call graph from the CompilationUnit
    public void generateCallGraph(CompilationUnit cu) {
        // Traverse the methods and populate the call graph
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            String methodName = method.getNameAsString();
            List<String> calls = new ArrayList<>();

            method.findAll(MethodCallExpr.class).forEach(call -> {
                String calledMethod = call.getNameAsString();
                calls.add(calledMethod);
    
                // Flag recursion directly in the graph (optional)
                if (methodName.equals(calledMethod)) {
                    // Mark the recursive call as "self-loop" in some way
                    calls.add(calledMethod + " (recursive)");
                }
            });
    
            

            callGraph.put(methodName, calls);
        });
    }

    // Print the call graph
    public void printCallGraph() {
        System.out.println("Call Graph:");
        callGraph.forEach((method, calls) -> {
            
            System.out.println(method + " -> " + calls);
        });
    }

    // Return the call graph as a Map
    public Map<String, List<String>> getCallGraph() {
        return callGraph;
    }
}
