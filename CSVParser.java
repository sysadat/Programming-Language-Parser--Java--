import java.util.*;
import java.io.IOException;

// Create the CSV Parser class
public class CSVParser {
    // Class attributes
    // https://stackoverflow.com/questions/1493162/how-does-one-instantiate-an-array-of-maps-in-java
    List<Map<String,String>> listOfMaps;
    Map<Integer, String> mapOfColumnNames;
    Set<String> setOfColumnNames;
    StreamClass streamObject;
    int headerColumns;

    // Constructor that takes in a stream.
    public CSVParser(StreamClass stream) {
        streamObject = stream;
        listOfMaps = new ArrayList<Map<String,String>>();
        mapOfColumnNames = new HashMap<Integer, String>();
        setOfColumnNames = new HashSet<String>(); 
        headerColumns = getHeaderColumns();
        getColumnNames();
    }

    // Methods for class

    public void hasHeaderRow() {
        char currentChar;
        boolean newLineSeen = false;
        int ahead = 0;
        while (streamObject.peekAheadChar(ahead)!= -1) {
            currentChar = (char)(streamObject.peekAheadChar(ahead));
            if (currentChar == '\n') {
                newLineSeen = true;
                break;
            }
            ahead++;
        }
        // If we haven't seen a new line character when we are checking the header then throw an error
        if (newLineSeen == false) {
            throw new IllegalArgumentException("CSV files must have a header row.");
        }
    }

    public void isHeaderRepeated(String columnName) {
        boolean isRepeatName = setOfColumnNames.contains(columnName);
        if (isRepeatName) {
            throw new IllegalArgumentException("No column in the header may be repeated.");
        }
    }

    // Check how many columns there are in a row
    public int getHeaderColumns() {
        hasHeaderRow();
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

    public int getColumns() {
        int numColumnsSeen = 0;
        int aheadIndex = 0;
        while ((char)streamObject.peekAheadChar(aheadIndex)!= '\n' && streamObject.peekAheadChar(aheadIndex)!= -1) {
            // The amount of commas will tell us how many columns there are 
            if ((char)streamObject.peekAheadChar(aheadIndex) == ',') {
                numColumnsSeen++;
            }
            aheadIndex++;
        }
        if ((char)streamObject.peekAheadChar(aheadIndex) == '\n' || streamObject.peekAheadChar(aheadIndex) == -1 ){
            numColumnsSeen++;
        }
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
                if ((char)streamObject.peekAheadChar(1) == ',' || (char)streamObject.peekAheadChar(1) == '\n') {
                    throw new IllegalArgumentException("No column in the header row may be empty.");
                }
                mapOfColumnNames.put(currentColumn, columnNames.toString());
                isHeaderRepeated(columnNames.toString());
                setOfColumnNames.add(columnNames.toString());
                columnNames.setLength(0);
                currentColumn++;
            } else if (currChar == '\n') {
                mapOfColumnNames.put(currentColumn, columnNames.toString());
                isHeaderRepeated(columnNames.toString());
                setOfColumnNames.add(columnNames.toString());
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
    
    public int getNewLineIndex () {
        int newLineIndex = 0;
        int aheadIndex = 0;
        while ((char)streamObject.peekAheadChar(aheadIndex)!= '\n' && streamObject.peekAheadChar(aheadIndex)!= -1) {
            if ((char)streamObject.peekAheadChar(aheadIndex) == '"' && (char)streamObject.peekAheadChar(aheadIndex + 1) == '\n') {
                aheadIndex = aheadIndex + 2;
            }
            newLineIndex++;
            aheadIndex++;
        }
        return newLineIndex;
    }

    // Returns the next row without consuming it. If no more rows are available null is returned.
    public Map<String,String> peekNextRow() {
        int res = getColumns();
        if (res > headerColumns) {
            throw new IllegalArgumentException("Error, there are more columns in the data row than the header row. Please fix this then try again.");
        }
        Map<String, String> returnRow = new HashMap<String, String>();
        StringBuilder itemName = new StringBuilder();
        int currentColumn = 0;
        int aheadIndex = 0;
        char currChar;
        boolean doubleQuotesSeen = false;
        int newLineIndex = 0;

        // If row we are on does not exist
        if (streamObject.peekAheadChar(aheadIndex) == -1) {
            return null; 
        }
        newLineIndex = getNewLineIndex();
        while (streamObject.peekAheadChar(aheadIndex)!= -1) {
            currChar = (char)streamObject.peekAheadChar(aheadIndex);
            if (currChar == '"') {
                doubleQuotesSeen = true;
            } else if ((currChar == ' ' || currChar == '\t') && !doubleQuotesSeen) {
                throw new IllegalArgumentException("Any whitespace character that is part of a column must be a double quoted column.");
            } else if (currChar == ',') {
                returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
                itemName.setLength(0);
                currentColumn++;
            } else if (currChar == '\n') {
                returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
                itemName.setLength(0);
                currentColumn++;
                aheadIndex++;
                break;
            }
            if (currChar != ',') {
                itemName.append(currChar);
            }
            aheadIndex++;
        }

        // If the row we are peaking is the last row of the CSV
        if (streamObject.peekAheadChar(aheadIndex) == -1) {
            returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
            itemName.setLength(0);
            currentColumn++;
        }

        return returnRow;
    }

    // Returns the next row and consumes it. If no more rows are available null is returned.
    public Map<String,String> getNextRow() {
        Map<String, String> returnRow = new HashMap<String, String>();
        StringBuilder itemName = new StringBuilder();
        int currentColumn = 0;
        char currChar;
        int nextChar;
        boolean doubleCommasAndMissingColumn = false;
        // If row we are on does not exist
        if (streamObject.peekNextChar() == -1) {
            return null; 
        }
        while (streamObject.peekNextChar()!= -1) {
            currChar = (char)streamObject.getNextChar();
            nextChar = streamObject.peekNextChar();
            if (currChar == ',') {
                returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
                itemName.setLength(0);
                currentColumn++;
                if ((char)nextChar == ',' && (char)streamObject.peekAheadChar(1) == '\n') {
                    doubleCommasAndMissingColumn = true;
                }
                if ((char)nextChar == ',') {
                    returnRow.put(mapOfColumnNames.get(currentColumn), null);
                    currentColumn++;
                    streamObject.getNextChar();
                    itemName.setLength(0);
                } else if ((char)nextChar == '\n' || nextChar == -1) {
                    returnRow.put(mapOfColumnNames.get(currentColumn), null);
                    streamObject.getNextChar();
                    itemName.setLength(0);
                    return returnRow;
                }
            } else if (currChar == '\n' && doubleCommasAndMissingColumn) {
                returnRow.put(mapOfColumnNames.get(currentColumn), null);
                itemName.setLength(0);
                currentColumn++;
                break;
            } else if (currChar == '\n') {
                returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
                itemName.setLength(0);
                currentColumn++;
                break;
            } 
            if (currChar != ',') {
                itemName.append(currChar);
            }
        }

        // If the row we are peaking is the last row of the CSV
        if (streamObject.peekNextChar() == -1) {
            returnRow.put(mapOfColumnNames.get(currentColumn), itemName.toString());
            itemName.setLength(0);
            currentColumn++;
        }
        return returnRow;
    }

    // Read the entire contents of the CSV file, put the items into a list and then call a method to print it out
    public void readCSVFile () {
        Map<String, String> returnRow = new HashMap<String, String>();
        while (peekNextRow() != null) {
            returnRow = getNextRow();   
            listOfMaps.add(returnRow);
        }
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
            throw new IllegalArgumentException("Please enter the name of the file that you wish to use.");
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];

        StreamClass stream = new StreamClass(inputtedFileName);
        CSVParser parserObject = new CSVParser(stream);
        parserObject.readCSVFile();
        parserObject.printListOfMaps();

    }
}