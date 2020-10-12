import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Create the class that will implement the interface
public class PCSClass implements PeekableCharacterStream {
    // Class attributes
    int currentIndex;
    int contentOfFileLength;
    String contentOfFile;

    // Constructor
    public PCSClass(String inputtedFileName) throws FileNotFoundException, IOException {
        // Get the name of the textfile from the command line, and then write the file's contents into a string
        Path fileName = Path.of(inputtedFileName);

        // System.out.println("The path is: " + fileName);

        String fileAsString = Files.readString(fileName);


        // System.out.println("Printing now ...");
        // System.out.println(fileAsString);
        
        contentOfFile = fileAsString;
        contentOfFileLength = contentOfFile.length();
        currentIndex = 0;

        // System.out.println("Content of file is: " + contentOfFile);

    }

    // Methods of the interface

    // Returns true if more characters are available, false otherwise
    public boolean moreAvailable() {
        // System.out.println("Length of string is: " + contentOfFileLength);

        if (currentIndex >= contentOfFileLength) {
            return false;
        } else {
            return true;
        }
    }

    // Returns the next character that would be returned without consuming
    // the character. If no more characters are available -1 is returned.
    public int peekNextChar() {
        boolean isAvaliable = moreAvailable();
        // System.out.println("In peekNextChar, result is: " + isAvaliable);
        char currentCharacter;
        int characterAsInt = 0;
        if (isAvaliable) {
            currentCharacter = contentOfFile.charAt(currentIndex);
            // System.out.println("Current character is: " + currentCharacter);
            // System.out.println("characterAsInt before is: " + characterAsInt);
            characterAsInt = currentCharacter;
            // System.out.println("characterAsInt is: " + characterAsInt);
            return characterAsInt;
        } else {
            return -1;
        }
    }
    // Returns the character ahead in the stream without consuming the
    // the character. peekAheadChar(0) returns the same character as
    // peekNextChar(). If no more characters are available at that position
    // -1 is returned.
    public int peekAheadChar(int ahead) {
        // The value for ahead is the value of the current index added to the value in ahead
        int aheadIndex = ahead + currentIndex;
        char currentCharacter;
        int characterAsInt = 0;
        System.out.println("Ahead index value is: " + aheadIndex);
        System.out.println("Ahead value is: " + ahead + "and aheadIndex is: " + currentIndex);
        if (aheadIndex >= contentOfFileLength) {
            return -1;
        } else {
            currentCharacter = contentOfFile.charAt(aheadIndex);
            characterAsInt = currentCharacter;
            return characterAsInt;
        }
    }


    // Returns the next character and consumes it. If no more characters are
    // available -1 is returned.
    public int getNextChar() {
        boolean isAvaliable = moreAvailable();
        // System.out.println("In peekNextChar, result is: " + isAvaliable);
        char currentCharacter;
        int characterAsInt = 0;
        if (isAvaliable) {
            currentCharacter = contentOfFile.charAt(currentIndex);
            // System.out.println("Current character is: " + currentCharacter);
            // System.out.println("characterAsInt before is: " + characterAsInt);
            characterAsInt = currentCharacter;
            // System.out.println("characterAsInt is: " + characterAsInt);
            return characterAsInt;
        } else {
            return -1;
        }
    }
/* 
     // Closes the stream.
     public void close() {
        
    } */

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            System.out.println("Please enter the name of the file that you wish to use.");
            System.exit(-1);
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];
        // System.out.println("Inputted file name is: " + inputtedFileName);

        PCSClass myObj = new PCSClass(inputtedFileName);
        boolean result = myObj.moreAvailable();
        int peekResult = myObj.peekNextChar();
        int peekAheadVal = 1;
        // System.out.println("Peek Ahead result is: " + myObj.peekAheadChar(peekAheadVal));
        // System.out.println("Peek result is: " + peekResult);

        // System.out.println("Result is: " + result);
        // System.out.println("Content of file is: " + myObj.contentOfFile);
        // System.out.println("Current index is: " + myObj.currentIndex);
        // System.out.println("Character at current index is: " + myObj.contentOfFile.charAt(myObj.currentIndex));

    }
}