import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// Create the class that will implement the interface
public class PCSClass implements PeekableCharacterStream {
    // Class attributes
    int currentIndex;
    String contentOfFile;

    // Constructor
    public PCSClass(String inputtedFileName) throws FileNotFoundException, IOException {
        String fileName = inputtedFileName;

        try (FileInputStream fis = new FileInputStream(fileName)) {
            
            char c1 = (char) fis.read();
            char c2 = (char) fis.read();
            char c3 = (char) fis.read();

            System.out.println(c1);
            System.out.println(c2);
            System.out.println(c3);
        }
        contentOfFile = inputtedFileName;
        currentIndex = 0;
    }

    // Methods of the interface

    // Returns true if more characters are available, false otherwise
 /*    public boolean moreAvailable() {

    }

    // Returns the next character that would be returned without consuming
    // the character. If no more characters are available -1 is returned.
    public int peekNextChar() {
        
    }

    // Returns the character ahead in the stream without consuming the
    // the character. peekAheadChar(0) returns the same character as
    // peekNextChar(). If no more characters are available at that position
    // -1 is returned.
    public int peekAheadChar(int ahead) {
        
    }

    // Returns the next character and consumes it. If no more characters are
    // available -1 is returned.
    public public int getNextChar() {
        
    }

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
        // TODO: Delete this testing
        System.out.println(inputtedFileName);

        PCSClass myObj = new PCSClass(inputtedFileName);

    }
}