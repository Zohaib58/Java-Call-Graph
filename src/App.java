public class App {
    public static void main(String[] args) throws Exception {
        
        try {
            // Parse the file
            String filePath = "tests/TestComplex.java";
            var compilationUnit = JavaFileParser.parseFile(filePath);
            
            //JavaFileParser.printAstTree(compilationUnit, filePath);
            CallGraph callGraph = new CallGraph();
            callGraph.generateCallGraph(compilationUnit);

            callGraph.printCallGraph();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
