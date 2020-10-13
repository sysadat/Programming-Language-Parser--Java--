import java.util.*;
import java.io.IOException;

// Create the CSV Parser class
public class CSVParser {
    // Class attributes
    // https://stackoverflow.com/questions/1493162/how-does-one-instantiate-an-array-of-maps-in-java
    List<Map<String,String>> listOfMaps;
    StreamClass streamObject;
    int headerColumns;
    Map<Integer, String> mapOfColumnNames;

    // Constructor that takes in a stream.
    public CSVParser(StreamClass stream) {
        streamObject = stream;
        listOfMaps = new ArrayList<Map<String,String>>();
        mapOfColumnNames = new HashMap<Integer, String>();
        headerColumns = getColumns();
        getColumnNames();
    }

    // Methods for class

    // Check how many columns there are in a row
    public int getColumns() {
        int numCols = 0;
        while (streamObject.getNextChar()!= -1) {
            // The amount of commas will tell us how many columns there are 
            if ((char)streamObject.peekNextChar() == ',') {
                streamObject.getNextChar();
                numCols++;
            // If we reach a new line that means that we found the final column and we are done with the header row
            } else if ((char)streamObject.peekNextChar() == '\n' ) {
                streamObject.getNextChar();
                numCols++;
                break;
            }
        }
        return numCols;
    }

    // Get the column names from the header row
    public void getColumnNames() {
        int currentColumn = 0;
        int oldStreamCurrentIndex = streamObject.currentIndex;
        StringBuilder columnNames = new StringBuilder();
        streamObject.currentIndex = 0;

        char currChar;
        while (streamObject.peekNextChar()!= -1) {
            currChar = (char)streamObject.peekNextChar();
            // System.out.println("THE CURRENT CHAR IS : " + currChar);
            if (currChar == ',') {
                mapOfColumnNames.put(currentColumn, columnNames.toString());
                columnNames.setLength(0);
                currentColumn++;
            } else if (currChar == '\n') {
                mapOfColumnNames.put(currentColumn, columnNames.toString());
                columnNames.setLength(0);
                currentColumn++;
                break;
            }

            if (currChar != ',') {
                columnNames.append(currChar);
            }
            streamObject.getNextChar();
        }

        System.out.println("THE MAP OF COLUMN NAMES IS : " + mapOfColumnNames);
        streamObject.currentIndex = oldStreamCurrentIndex;
    }

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

        ourSecondHashMap.put("First", "OOOO"); 
        ourSecondHashMap.put("Second", "BAG"); 

        return ourHashMap;
    }

    // Print each map in the list on its own line
    public void printListOfMaps () {
        for (int i = 0; i < listOfMaps.size(); i++) {
            System.out.println(listOfMaps.get(i)); 
        }
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
        stream.close();
    }
}