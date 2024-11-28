import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

public class CallGraph {

    private final Map<String, List<String>> callGraph = new LinkedHashMap<>();
    
    Set<String> nodes = new HashSet<>();
    Set<String> uniquePaths = new LinkedHashSet<>();

    //private final Stack<String> callStack = new Stack<>();
    
    Stack<String> pathsStack = new Stack<>();

    public void generateCallGraph(CompilationUnit cu) {
        // Find all method declarations in the compilation unit
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            //String currentPath = "";
            
            
            String methodName = method.getNameAsString();
            nodes.add(methodName);

            ArrayList<String> currentPaths = new ArrayList<>();

            method.getBody().ifPresent(body -> traverseMethodBody(body, currentPaths));

            System.out.println("----------- " + methodName + " -----------");
            
            for (String path: currentPaths) {
                System.out.println(path);
            }
            System.out.println();
            
        });
    }
    private void traverseMethodBody(Node node, ArrayList<String> currentPaths) {
        if (node == null) return;

        if (node instanceof MethodCallExpr) {
            MethodCallExpr callExpr = (MethodCallExpr) node;
            String call = callExpr.getNameAsString();

            if (currentPaths.isEmpty()) {
                currentPaths.add(call); 
            } else {
                for (int i = 0; i < currentPaths.size(); i++) {
                    String updatedPath = currentPaths.get(i) + " -> " + call;
                    currentPaths.set(i, updatedPath); 
                }
            }
        }  

        // Handle IfStmt
        else if (node instanceof IfStmt) {
            IfStmt ifStmt = (IfStmt) node;
        
            ArrayList<String> thenPaths = new ArrayList<>(currentPaths);
            traverseMethodBody(ifStmt.getThenStmt(), thenPaths); 
        
            // Handle 'else' branch if it exists
            ifStmt.getElseStmt().ifPresent(elseStmt -> {
                ArrayList<String> elsePaths = new ArrayList<>(currentPaths); 
                traverseMethodBody(elseStmt, elsePaths);

                currentPaths.clear(); 

                currentPaths.addAll(elsePaths); // Merge 'else' paths into current paths
            });
        
            currentPaths.addAll(thenPaths);    

            //System.out.println("After If-Else Handling: " + currentPaths);

            return;
        }
        
        // Handle SwitchStmt
        else if (node instanceof SwitchStmt) {
            SwitchStmt switchStmt = (SwitchStmt) node;
        
            ArrayList<String> mergedPaths = new ArrayList<>();
            
            switchStmt.getEntries().forEach(entry -> {
                ArrayList<String> casePaths = new ArrayList<>(currentPaths);
        
                entry.getStatements().forEach(statement -> traverseMethodBody(statement, casePaths));
        
                // Add this case's paths to the merged result
                mergedPaths.addAll(casePaths);
            });
        
            currentPaths.clear();
            currentPaths.addAll(mergedPaths);
        
            return;
        }
        
        // Recursively process all child nodes
        node.getChildNodes().forEach(child -> traverseMethodBody(child, currentPaths));
        /*
        if (node instanceof ForStmt) {
            ForStmt forStmt = (ForStmt) node;
            String loopContext = "loop: for " + (forStmt.getCompare().isPresent() ? forStmt.getCompare().get().toString() : "(no condition)");
            // contextStack.push(loopContext);
            traverseMethodBody(forStmt.getBody(), calls);
            // contextStack.pop();
            return;
        } 
         */
        // Handle ForStmt
        

        /*
        if (node instanceof WhileStmt) {
            WhileStmt whileStmt = (WhileStmt) node;
            String loopContext = "loop: while " + whileStmt.getCondition().toString();
            // contextStack.push(loopContext);
            traverseMethodBody(whileStmt.getBody(), calls);
            // contextStack.pop();
            return;
        } 
         */
        // Handle WhileStmt
        

        /* 
        // Handle DoStmt
        if (node instanceof DoStmt) {
            DoStmt doStmt = (DoStmt) node;
            String loopContext = "loop: do-while " + doStmt.getCondition().toString();
            // contextStack.push(loopContext);
            traverseMethodBody(doStmt.getBody(), calls);
            // contextStack.pop();
            return;
        }
        */

        // Handle MethodCallExpr
        
        
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
        

