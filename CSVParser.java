import java.io.IOException;

// Create the CSV Parser class
public class CSVParser {

    public CSVParser() throws IOException {
        System.out.println("Created!");
    }

    public static void main(String[] args) throws IOException {
        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            System.out.println("Please enter the name of the file that you wish to use.");
            System.exit(-1);
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];

        CSVParser parserObject = new CSVParser();
    }
}