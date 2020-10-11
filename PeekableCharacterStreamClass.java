import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

// Create the class that will implement the interface
public class PeekableCharacterStreamClass implements PeekableCharacterStream {
    // Class attributes
    int currentIndex;
    String contentOfFile;

    // Constructor
    public PeekableCharacterStreamClass(String fileToRead) throws FileNotFoundException, IOException {
        String fileName = "/home/sysadat/ECS-140A/Project-001/test.txt";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            char c1 = (char) fis.read();
            char c2 = (char) fis.read();
            char c3 = (char) fis.read();

            System.out.println(c1);
            System.out.println(c2);
            System.out.println(c3);
        }
        contentOfFile = fileToRead;
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
        PeekableCharacterStreamClass myObj = new PeekableCharacterStreamClass("test");

    }
}