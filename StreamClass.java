import java.io.FileInputStream;
import java.io.IOException;

// Create the class that will implement the interface
public class StreamClass implements PeekableCharacterStream {
    // Class attributes
    int currentIndex;
    int contentOfFileLength;
    String contentOfFile;
    FileInputStream fis;

    // Constructor
    public StreamClass(String inputtedFileName) throws IOException {
        // Get the name of the textfile from the command line, and then write the file's contents into a string
        // https://stackoverflow.com/questions/15161553/how-to-convert-fileinputstream-into-string-in-java
        fis = new FileInputStream(inputtedFileName);
        StringBuilder fileAsString = new StringBuilder();
        int currentCharacter;

        // Build a string to store the contents of the file we are reading
        while ((currentCharacter = fis.read()) != -1) {
            fileAsString.append((char)currentCharacter);
        }

        contentOfFile = fileAsString.toString();
        contentOfFileLength = contentOfFile.length();
        currentIndex = 0;

        close();
    }

    // Methods of the interface

    // Returns true if more characters are available, false otherwise
    public boolean moreAvailable() {
        boolean isAvaliable = true;
        if (currentIndex >= contentOfFileLength) {
            isAvaliable = false;
        }
        return isAvaliable;
    }

    // Returns the next character that would be returned without consuming
    // the character. If no more characters are available -1 is returned.
    public int peekNextChar() {
        boolean isAvaliable = moreAvailable();
        char currentCharacter;
        int characterAsInt = 0;
        if (isAvaliable) {
            currentCharacter = contentOfFile.charAt(currentIndex);
            characterAsInt = currentCharacter;
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
        // To peek ahead, we take the value of the current index and add it to the value in ahead
        int aheadIndex = ahead + currentIndex;
        char currentCharacter;
        int characterAsInt = 0;

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
        char currentCharacter;
        int characterAsInt = 0;
        if (isAvaliable) {
            currentCharacter = contentOfFile.charAt(currentIndex);
            characterAsInt = currentCharacter;
            currentIndex++;
            return characterAsInt;
        } else {
            return -1;
        }
    }

     // Closes the stream.
     public void close() {
        try {
            fis.close();
        }
        catch (IOException e) {
            System.out.println("Error in closing the stream");
        }
    }

    public static void main(String[] args) throws IOException {

        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            throw new IllegalArgumentException("Please enter the name of the file that you wish to use.");
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];
        StreamClass StreamObject = new StreamClass(inputtedFileName);

        // Testing
        System.out.println("More avaliable response is: " + StreamObject.moreAvailable());
        System.out.println("peekNextChar is: " + StreamObject.peekNextChar());
        System.out.println("peekAheadChar is: " + StreamObject.peekAheadChar(2));
        System.out.println("getNextChar response is: " + StreamObject.getNextChar());
        System.out.println("Current index is: " + StreamObject.currentIndex);
        System.out.println("Contents of file are: " + StreamObject.contentOfFile);
    }
}