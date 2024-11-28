import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

public class CallGraph {

    private final Map<String, List<String>> callGraph = new LinkedHashMap<>();
    
    //Set<String> nodes = new HashSet<>();
    //Set<String> uniquePaths = new LinkedHashSet<>();

    //private final Stack<String> callStack = new Stack<>();
    
    //Stack<String> pathsStack = new Stack<>();
    ArrayList<String> paths = new ArrayList<>();

    public void generateCallGraph(CompilationUnit cu) {
        // Find all method declarations in the compilation unit
        cu.findAll(MethodDeclaration.class).forEach(method -> {
            //String currentPath = "";
            
            
            String methodName = method.getNameAsString();
            //nodes.add(methodName);

            ArrayList<String> currentPaths = new ArrayList<>();

            method.getBody().ifPresent(body -> traverseMethodBody(body, currentPaths));

            
            for (int i = 0; i < currentPaths.size(); i++) {
                if (!callGraph.containsKey(methodName)) {
                    callGraph.put(methodName, new ArrayList<>());
                }
                callGraph.get(methodName).add(currentPaths.get(i));    
            }

            System.out.println("----------- " + "Program Call Sequence" + " -----------");

            
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
        
    }

    /*
        public void printCallGraph() {
        System.out.println("Call Graph:");
        callGraph.forEach((method, calls) -> {
            System.out.println("Method: " + method);
            calls.forEach(call -> System.out.println("    " + call));
        });
    } 
     */
    

    public ArrayList<String> getPaths()
    {
        return paths;
    }
    public Map<String, List<String>> getCallGraph() {
        return callGraph;
    }
}


    