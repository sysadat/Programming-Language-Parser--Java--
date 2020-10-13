import java.util.*;
import java.io.IOException;

// Create the CSV Parser class
public class CSVParser {
    // Class attributes
    // https://stackoverflow.com/questions/1493162/how-does-one-instantiate-an-array-of-maps-in-java
    List<Map<String,String>> listOfMaps = new ArrayList<Map<String,String>>();
    StreamClass streamObject;

    // Constructor that takes in a stream.
    public CSVParser(StreamClass stream) {
        streamObject = stream;
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

    public Map<String,String> getMaps() {
        Map<String, String> ourHashMap = new HashMap<String, String>();
        Map<String, String> ourSecondHashMap = new HashMap<String, String>();

        ourHashMap.put("First", "LOL"); 
        ourHashMap.put("Second", "TEST"); 
        ourHashMap.put("Third", "TESTINGGGG");

        ourSecondHashMap.put("First", "OOOO"); 
        ourSecondHashMap.put("Second", "BAG"); 
        ourSecondHashMap.put("Third", "HEHE");

        return ourHashMap;
    }


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


        // Map<String, String> printMap = new HashMap<String, String>();
        // printMap = parserObject.getMaps();
        // System.out.println(printMap);


        Map<String, String> ourHashMap = new HashMap<String, String>();
        Map<String, String> ourSecondHashMap = new HashMap<String, String>();

        ourHashMap.put("First", "LOL"); 
        ourHashMap.put("Second", "TEST"); 
        ourHashMap.put("Third", "TESTINGGGG");

        ourSecondHashMap.put("First", "OOOO"); 
        ourSecondHashMap.put("Second", "BAG"); 
        ourSecondHashMap.put("Third", "HEHE");

        List<Map<String,String>> listOfMaps = new ArrayList<Map<String,String>>();
        listOfMaps.add(ourHashMap);
        listOfMaps.add(ourSecondHashMap);

        for (int i = 0; i < listOfMaps.size(); i++) {
            System.out.println(listOfMaps.get(i)); 
        }

    }
}