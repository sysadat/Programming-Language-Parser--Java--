import java.util.*;
import java.io.IOException;

// Create the CSV Parser class
public class CSVParser {
    // Class attributes
    // https://stackoverflow.com/questions/1493162/how-does-one-instantiate-an-array-of-maps-in-java
    List<Map<String,String>> listOfMaps;
    Map<Integer, String> mapOfColumnNames;
    StreamClass streamObject;
    int headerColumns;

    // Constructor that takes in a stream.
    public CSVParser(StreamClass stream) {
        streamObject = stream;
        listOfMaps = new ArrayList<Map<String,String>>();
        mapOfColumnNames = new HashMap<Integer, String>();
        headerColumns = getHeaderColumns();
        getColumnNames();
    }

    // Methods for class

    // Check how many columns there are in a row
    public int getHeaderColumns() {
        int numCols = 0;
        while (streamObject.getNextChar()!= -1) {
            // System.out.println("Current index here is: " + streamObject.currentIndex);

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
        // System.out.println("numCols is: " + numCols);
        return numCols;
    }

    // TODO
    public int getColumns() {
        int numColumnsSeen = 0;
        int aheadIndex = 0;
        while ((char)streamObject.peekAheadChar(aheadIndex)!= '\n' && streamObject.peekAheadChar(aheadIndex)!= -1) {
            // System.out.println("Looking at: " + (char)streamObject.peekAheadChar(aheadIndex));
            // System.out.println("Int version is  at: " + streamObject.peekAheadChar(aheadIndex));
            // System.out.println("aheadIndex is: " + aheadIndex);
            // The amount of commas will tell us how many columns there are 
            if ((char)streamObject.peekAheadChar(aheadIndex) == ',') {
                numColumnsSeen++;
                // System.out.println("numColumnsSeen is: " + numColumnsSeen);
            }
            aheadIndex++;
        }
        if ((char)streamObject.peekAheadChar(aheadIndex) == '\n' || streamObject.peekAheadChar(aheadIndex) == -1 ){
            numColumnsSeen++;
        }
        System.out.println("commasSeen is before return is: " + numColumnsSeen);
        return numColumnsSeen;
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
            // https://stackoverflow.com/questions/5192512/how-can-i-clear-or-empty-a-stringbuilder
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
        streamObject.currentIndex = oldStreamCurrentIndex;
    }

    public void testing() {
        // System.out.println("Current index is: " + streamObject.currentIndex);
        // System.out.println("contentOfFileLength is: " + streamObject.contentOfFileLength);
    }

    // Returns the next row without consuming it. If no more rows are available null is returned.
    public Map<String,String> peekNextRow() {
        // TODO
        System.out.println("WE HERE");

        int res = getColumns();
        System.out.println("Res is: " + res);
        if (res > headerColumns || res == -1) {
            System.out.println("Error, there are more columns in the data row than the header row. Please fix this then try again.");
            System.exit(-1);
        }
        Map<String, String> returnRow = new HashMap<String, String>();
        StringBuilder itemName = new StringBuilder();
        int currentColumn = 0;
        char currChar;
        while (streamObject.peekNextChar()!= -1) {
            currChar = (char)streamObject.peekNextChar();
            // System.out.println("currChar is: " + currChar);
            if (currChar == ',') {
                returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
                itemName.setLength(0);
                // listOfMaps.add(returnRow);
                System.out.println("ADDED HERE");
                currentColumn++;
            } else if (currChar == '\n') {
                returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
                itemName.setLength(0);
                currentColumn++;
                streamObject.getNextChar();
                // listOfMaps.add(returnRow);
                System.out.println("ADDED LOL");
                break;
            }
            if (currChar != ',') {
                itemName.append(currChar);
            }
            streamObject.getNextChar();
            // System.out.println("Built string is: " + itemName);


            // System.out.println("Current column is: " + currentColumn);
            // System.out.println("returnRow inside is: " + returnRow);

        }

        // If the row we are peaking is the last row of the CSV
        if (streamObject.peekNextChar()== -1) {
            returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
            itemName.setLength(0);
            currentColumn++;
            // listOfMaps.add(returnRow);
            System.out.println("ADDED O_____O");
        }
        // returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
        // System.out.println("Built string is: " + itemName);

        // listOfMaps.add(returnRow);
        System.out.println("returnRow is: " + returnRow);
        listOfMaps.add(returnRow);
        printListOfMaps();
        return returnRow;
    }

    // Returns the next row and consumes it. If no more rows are available null is returned.
    // public Map<String,String> getNextRow() {
    //     return;
    // }

    // Print each map in the list on its own line
    public void printListOfMaps () {
        System.out.println("SIZE IS : " + listOfMaps.size()); 
        for (int i = 0; i < listOfMaps.size(); i++) {
            System.out.println("Printing"); 
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
        parserObject.testing();
        parserObject.peekNextRow();
        parserObject.peekNextRow();

    }
}