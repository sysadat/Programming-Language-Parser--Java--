import java.io.IOException;

// Create the Scanner class
public class Scanner {

    public Scanner() throws IOException {
        System.out.println("Created!");
    }

    public static void main(String[] args) throws IOException {
        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            throw new IllegalArgumentException("Please enter the name of the file that you wish to use.");
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];

        Scanner scannerObject = new Scanner();
    }
}