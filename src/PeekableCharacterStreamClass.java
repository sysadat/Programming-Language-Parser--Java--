import java.io.FileInputStream;

// Create the class that will implement the interface
public class PeekableCharacterStreamClass implements PeekableCharacterStream {
    // Class attributes
    int currentIndex;
    String contentOfFile;

    // Constructor
    public PeekableCharacterStreamClass(String fileToRead) {
        contentOfFile = fileToRead;
        currentIndex = 0;
    }

    public void testPrint(String itemToPrint) {
        System.out.println(itemToPrint);
    }

    public static void main(String[] args) {
        PeekableCharacterStreamClass myObj = new PeekableCharacterStreamClass("I love my wife");
        myObj.testPrint("LOL");
      }
}