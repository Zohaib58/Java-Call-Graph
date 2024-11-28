import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

public class CallGraph {

    private final LinkedHashMap<String, List<String>> callGraph = new LinkedHashMap<>();
    
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
            
        });
    
        this.markRecursivePaths();
    
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


    private void markRecursivePaths() {
        for (String key : callGraph.keySet()) {
            List<String> updatedValues = new ArrayList<>();
    
            for (String value : callGraph.get(key)) {
                String[] elements = value.split(" -> ");
    
                for (int i = 0; i < elements.length; i++) {
                    if (elements[i].equals(key)) {
                        elements[i] = key + "(r)";
                    }
                }
    
                String updatedValue = String.join(" -> ", elements);
                updatedValues.add(updatedValue);
            }
    
            callGraph.put(key, updatedValues);
        }
    }
        
    private Map<String, List<String>> memo = new HashMap<>(); // Cache for memoization

    public List<String> generateSequencePaths() {
        String startKey = "";

        // Determine the start key
        if (callGraph.containsKey("main")) {
            startKey = "main";
        } else {
            startKey = callGraph.keySet().iterator().next();
        }

        List<String> allPaths = new ArrayList<>();

        
        for (String value : callGraph.get(startKey)) {
            String[] nodes = value.split(" -> ");

            for (int i = 0; i < nodes.length; i++) {
                // get sequence paths for each node
                List<String> nodePaths = getSequencePaths(nodes[i]);

                if (!nodePaths.isEmpty()) {
                    nodes[i] = nodes[i] + " -> " + String.join(", ", nodePaths);
                }
            }
            allPaths.add(String.join(" -> ", nodes));
        }

        return allPaths;
    }

    private List<String> getSequencePaths(String node) {
        // Check if the result for this node is already memoized
        if (memo.containsKey(node)) {
            return memo.get(node);
        }

        List<String> result = new ArrayList<>(); 

        // Base Case
        if (!callGraph.containsKey(node)) {
            return result;
        }

        List<String> nodeSequencePaths = callGraph.get(node);

        for (String nodeSequencePath : nodeSequencePaths) {
            String[] nodes = nodeSequencePath.split(" -> ");

            List<String> currentPaths = new ArrayList<>();
            currentPaths.add(nodeSequencePath);

            for (String internalNode : nodes) {
                // Recursive call to get deeper path
                List<String> deeperPaths = getSequencePaths(internalNode);

                // Concatenate current paths with deeper paths
                List<String> newPaths = new ArrayList<>();
                for (String currentPath : currentPaths) {
                    if (deeperPaths.isEmpty()) {
                        newPaths.add(currentPath); // If no deeper paths, keep the current path
                    } else {
                        for (String deeperPath : deeperPaths) {
                            newPaths.add(currentPath + " -> " + deeperPath);
                        }
                    }
                }
                currentPaths = newPaths;
            }

            result.addAll(currentPaths);
        }

        memo.put(node, result);

        return result;
    }

    /*
     public ArrayList<String> getPaths()
    {
        return paths;
    }
     */
    
    public Map<String, List<String>> getCallGraph() {
        return callGraph;
    }
}


    