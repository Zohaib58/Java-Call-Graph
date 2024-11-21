public class App {
    public static void main(String[] args) throws Exception {
        
        try {
            // Parse the file
            String filePath = "src/tests/Test1.java";
            var compilationUnit = JavaFileParser.parseFile(filePath);

            // JavaFileParser.printAstTree(compilationUnit, "");
            // Print method declarations
            System.out.println("Extracting method declarations...");
            // JavaFileParser.printMethodDeclarations(compilationUnit);

            // Print method calls
            // System.out.println("Extracting method calls...");
            // JavaFileParser.printMethodCalls(compilationUnit);

            CallGraph callGraph = new CallGraph();
            callGraph.generateCallGraph(compilationUnit);

            // Print the call graph
            callGraph.printCallGraph();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
