import java.util.*;
import java.io.IOException;

// Create the CSV Parser class
public class CSVParser {
    // Class attributes
    // https://stackoverflow.com/questions/1493162/how-does-one-instantiate-an-array-of-maps-in-java
    List<Map<String,String>> listOfMaps = new ArrayList<Map<String,String>>();

    // Constructor that takes in a stream.
    public CSVParser(StreamClass stream) {
        System.out.println("Created!");
    }

    // Methods for class

    // Returns the next row without consuming it. If no more rows are available null is returned.
    // public Map<String,String> peekNextRow() {
    //     return;
    // }

    // Returns the next row and consumes it. If no more rows are available null is returned.
    // public Map<String,String> getNextRow() {
    //     return;
    // }

    // Print out the list that stores the maps
    // public void printMaps() {
    // }

    public static void main(String[] args) throws IOException {
        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            System.out.println("Please enter the name of the file that you wish to use.");
            System.exit(-1);
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];

        StreamClass stream = new StreamClass(inputtedFileName);
        CSVParser parserObject = new CSVParser(stream);
    }
}