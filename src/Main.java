import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            String filePath;

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the file path of the Java source code:");
            filePath = scanner.nextLine().trim();
            scanner.close();

            if (filePath.isBlank() || !isValidFile(filePath)) {
                filePath = "tests/TestComplex.java"; // Default file path
                System.out.println("Invalid or no file path provided. Using default path: " + filePath);
            }

            // Parse the file
            var compilationUnit = JavaFileParser.parseFile(filePath);

            // Generate and print the call graph
            CallGraph callGraph = new CallGraph();
            callGraph.generateCallGraph(compilationUnit);
            callGraph.printCallGraph();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Util
    private static boolean isValidFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }
}