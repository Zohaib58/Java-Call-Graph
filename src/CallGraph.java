import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

public class CallGraph {

    private final Map<String, List<String>> callGraph = new LinkedHashMap<>();

    public void generateCallGraph(CompilationUnit cu) {
        // Find all method declarations in the compilation unit
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            String methodName = method.getNameAsString();
            List<String> calls = new ArrayList<>(); // Ordered storage for method calls
    
            Stack<String> contextStack = new Stack<>();

            // Traverse the method body, starting with no initial context
            method.getBody().ifPresent(body -> traverseMethodBody(body, contextStack, calls));
    
            // Add the method's calls to the call graph
            callGraph.put(methodName, calls);
        });
    }
    
    private void traverseMethodBody(Node node, Stack<String> contextStack, List<String> calls) {
        if (node == null) return;
    
        // Handle IfStmt
        if (node instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) node;
    
            // Add context for the 'if' condition
            contextStack.push("if: " + ifStmt.getCondition().toString());
            traverseMethodBody(ifStmt.getThenStmt(), contextStack, calls); // Traverse 'then' block
            contextStack.pop();
    
            // Handle 'else' branch
            ifStmt.getElseStmt().ifPresent(elseStmt -> {
                /*
                Self Note:
                 Else If is represented as If in the Else block
                 */ //else if
                if (elseStmt instanceof IfStmt) {
                    // Handle 'else if'
                    IfStmt nestedIfStmt = (IfStmt) elseStmt;
                    contextStack.push("else if: " + nestedIfStmt.getCondition().toString());
                    traverseMethodBody(nestedIfStmt.getThenStmt(), contextStack, calls); // Traverse 'then' of 'else if'
                    contextStack.pop();
    
                    // else when else if
                    nestedIfStmt.getElseStmt().ifPresent(nestedElse -> {
                        contextStack.push("else");
                        traverseMethodBody(nestedElse, contextStack, calls);
                        contextStack.pop();
                    });
                } else {
                    // Regular 'else' block when no else if
                    contextStack.push("else");
                    traverseMethodBody(elseStmt, contextStack, calls);
                    contextStack.pop();
                }
            });
            return;
        }
    
        // Handle SwitchStmt
        if (node instanceof SwitchStmt) {
            SwitchStmt switchStmt = (SwitchStmt) node;
    
            switchStmt.getEntries().forEach(entry -> {
                String context = entry.getLabels().isEmpty() ? "case: default" : "case: " + entry.getLabels();
                contextStack.push(context);
                entry.getStatements().forEach(statement -> traverseMethodBody(statement, contextStack, calls));
                contextStack.pop();
            });
            return;
        }
        
        // Handle ForStmt
        if (node instanceof ForStmt) {
            ForStmt forStmt = (ForStmt) node;
            String loopContext = "loop: for " + (forStmt.getCompare().isPresent() ? forStmt.getCompare().get().toString() : "(no condition)");
            contextStack.push(loopContext);
            traverseMethodBody(forStmt.getBody(), contextStack, calls);
            contextStack.pop();
            return;
        }

        // Handle WhileStmt
        if (node instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) node;
            String loopContext = "loop: while " + whileStmt.getCondition().toString();
            contextStack.push(loopContext);
            traverseMethodBody(whileStmt.getBody(), contextStack, calls);
            contextStack.pop();
            return;
        }

        // Handle DoStmt
        if (node instanceof DoStmt) {
            DoStmt doStmt = (DoStmt) node;
            String loopContext = "loop: do-while " + doStmt.getCondition().toString();
            contextStack.push(loopContext);
            traverseMethodBody(doStmt.getBody(), contextStack, calls);
            contextStack.pop();
            return;
        }
    
        // Handle MethodCallExpr
        if (node instanceof MethodCallExpr) {
            MethodCallExpr callExpr = (MethodCallExpr) node;
            String call = callExpr.getNameAsString();
    
            // Add contexts to the method call
            String fullContext = String.join(" -> \n\t", contextStack);
            calls.add(call + (fullContext.isEmpty() ? "" : " (" + fullContext + ")"));
        }
    
        // Recursively process all child nodes
        node.getChildNodes().forEach(child -> traverseMethodBody(child, contextStack, calls));
    }
    
    public void printCallGraph() {
        System.out.println("Call Graph:");
        callGraph.forEach((method, calls) -> {
            System.out.println("Method: " + method);
            calls.forEach(call -> System.out.println("    " + call));
        });
    }

    public Map<String, List<String>> getCallGraph() {
        return callGraph;
    }
}


    /*
         private void traverseMethodBody(Node node, Stack<String> contextStack, Set<String> calls) {
        if (node == null) return;
    
        // Handle IfStmt
        if (node instanceof IfStmt) {
            traversingIfBlock(node, contextStack, calls); 
        }
        
        // Handle SwitchStmt
        if (node instanceof SwitchStmt) {
            traversingSwitchBlock(node, contextStack, calls);
        }

        // Handle ForStmt
        if (node instanceof ForStmt) {
            traversingForBlock(node, contextStack, calls);
        }
        
        // Handle WhileStmt
        if (node instanceof WhileStmt) {
            traversingWhileBlock(node, contextStack, calls);
        }

        // Handle DoStmt
        if (node instanceof DoStmt) {
            traversingDoBlock(node, contextStack, calls);
        }
    
        // Handle MethodCallExpr
        if (node instanceof MethodCallExpr) {
            traversingMethodCallBlock(node, contextStack, calls);
        }
    
        // Recursively process all child nodes
        node.getChildNodes().forEach(child -> traverseMethodBody(child, contextStack, calls));
    }
    
    private void traversingIfBlock(Node node, Stack<String> contextStack, Set<String> calls){
        IfStmt ifStmt = (IfStmt) node;
    
            // Add context for the 'if' condition
            contextStack.push("if: " + ifStmt.getCondition().toString());
            traverseMethodBody(ifStmt.getThenStmt(), contextStack, calls); // Traverse 'then' block
            contextStack.pop();
    
            // Handle 'else' branch
            ifStmt.getElseStmt().ifPresent(elseStmt -> {
                if (elseStmt instanceof IfStmt) {
                    // Handle 'else if'
                    IfStmt nestedIfStmt = (IfStmt) elseStmt;
                    contextStack.push("else if: " + nestedIfStmt.getCondition().toString());
                    traverseMethodBody(nestedIfStmt.getThenStmt(), contextStack, calls); // Traverse 'then' of 'else if'
                    contextStack.pop();
    
                    // Handle nested 'else' inside 'else if'
                    nestedIfStmt.getElseStmt().ifPresent(nestedElse -> {
                        contextStack.push("else");
                        traverseMethodBody(nestedElse, contextStack, calls);
                        contextStack.pop();
                    });
                } else {
                    // Regular 'else' block
                    contextStack.push("else");
                    traverseMethodBody(elseStmt, contextStack, calls);
                    contextStack.pop();
                }
            });
            return;
        }
    
    private void traversingSwitchBlock(Node node, Stack<String> contextStack, Set<String> calls){
        SwitchStmt switchStmt = (SwitchStmt) node;
    
            switchStmt.getEntries().forEach(entry -> {
                String context = entry.getLabels().isEmpty() ? "case: default" : "case: " + entry.getLabels();
                contextStack.push(context);
                entry.getStatements().forEach(statement -> traverseMethodBody(statement, contextStack, calls));
                contextStack.pop();
            });
            return;
    }

    private void traversingForBlock(Node node, Stack<String> contextStack, Set<String> calls){

        ForStmt forStmt = (ForStmt) node;
        String loopContext = "loop: for " + (forStmt.getCompare().isPresent() ? forStmt.getCompare().get().toString() : "(no condition)");
        contextStack.push(loopContext);
        traverseMethodBody(forStmt.getBody(), contextStack, calls);
        contextStack.pop();

    }

    private void traversingWhileBlock(Node node, Stack<String> contextStack, Set<String> calls){
    WhileStmt whileStmt = (WhileStmt) node;
            String loopContext = "loop: while " + whileStmt.getCondition().toString();
            contextStack.push(loopContext);
            traverseMethodBody(whileStmt.getBody(), contextStack, calls);
            contextStack.pop();
            return;
    }

    private void traversingDoBlock(Node node, Stack<String> contextStack, Set<String> calls){
        DoStmt doStmt = (DoStmt) node;
            String loopContext = "loop: do-while " + doStmt.getCondition().toString();
            contextStack.push(loopContext);
            traverseMethodBody(doStmt.getBody(), contextStack, calls);
            contextStack.pop();
            return;

    }

    private void traversingMethodCallBlock(Node node, Stack<String> contextStack, Set<String> calls){
        MethodCallExpr callExpr = (MethodCallExpr) node;
        String call = callExpr.getNameAsString();

        // Add contexts to the method call
        /*
         Self Note: For nested context ONLY
         */

         /*
          * 
          
        String fullContext = String.join(" -> ", contextStack);
        calls.add(call + (fullContext.isEmpty() ? "" : " (" + fullContext + ")"));
    }

    
 
     */
        

