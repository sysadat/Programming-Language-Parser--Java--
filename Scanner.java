import java.util.*;
import java.io.IOException;

// Create the Scanner class
public class Scanner {
    // Class attributes
    StreamClass streamObject;
    List<String> listOfKeywords;
    int currentLineIndex;
    int currentCharIndex;
    String[] stringOfOperators;
    Set<String> setOfOperators;
    boolean previousConstantOrIdentifier;
    static String noString = "S";
    static char noChar = 'C';

    // Constructor that takes in a stream and a list of keywords.
    public Scanner(StreamClass stream, List<String> keywordlist){
        streamObject = stream;
        listOfKeywords = keywordlist;
        currentLineIndex = 1;
        currentCharIndex = 0;
        // https://stackoverflow.com/questions/2041778/how-to-initialize-hashset-values-by-construction
        stringOfOperators = new String[] {"(", ",", ")", "{", "}", "=", "==", "<", ">", "<=", ">=", "!=", "+", "-", "*", "/", ";"};
        setOfOperators = new HashSet<>(Arrays.asList(stringOfOperators));
        previousConstantOrIdentifier = false;
    }

    // Check if the string entered is a keyword. Returns true if it is an keyword, false otherwise. 
    public boolean isKeyword (String currString) { 
        boolean isKeywordCheck = false;
        if (listOfKeywords.contains(currString)) {
            isKeywordCheck = true;
        }
        return isKeywordCheck;
    }

    // Identifier := ( _ | Alpha ) { ( _ | Digit | Alpha ) }
    // Check if the string entered is an identifier. Returns true if it is an identifier, false otherwise. 
    public boolean isIdentifier (String currString) {
        // Need at least one character to be a proper indentifier
        if (currString.length() < 1) {
            return false;
        }

        boolean isIndentifierCheck = false;
        char firstIndex = currString.charAt(0);
        if (firstIndex != '_' && !isAlpha(firstIndex)) {
            isIndentifierCheck = false;
            return isIndentifierCheck;
        } else {
            for (int i = 0; i < currString.length(); i++) {
                char currChar = currString.charAt(i);
                if (currChar != '_' && !isDigit(currChar) && !isAlpha(currChar)) {
                    isIndentifierCheck = false;
                    return isIndentifierCheck;
                }
            }
            isIndentifierCheck = true;
        }

        return isIndentifierCheck;
    }

    // Operator := ( | , | ) | { | } | = | == | < | > | <= | >= | != | + | - | * | / | ;
    // Check if character or string is an operator. Can take in either char or string. Will return true if it is an operator, false otherwise.
    // If 0 is passed in for charOrString, that means we passed in a character. If 1, that means we passed in a string. 
    public boolean isOperator(char currChar, String currString, int charOrString) {
        boolean isOperatorCheck = false;
        if (charOrString == 0) {
            String charAsString = Character.toString(currChar);
            isOperatorCheck = setOfOperators.contains(charAsString);
        } else if (charOrString == 1) {
            isOperatorCheck = setOfOperators.contains(currString);
        }
        return isOperatorCheck;
    }

    // IntConstant := [ - ] Digit { Digit }
    // Check if a string is an IntConstant. Will return true if it is an IntConstant, false otherwise.
    public boolean isIntConstant(String currString) {
        boolean isIntConstantCheck = false;
        // https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
        int stringLength = currString.length();
        if (stringLength == 0) {
            return false;
        }
        int i = 0;
        // If the string is just '-' that means it is not an int constant
        if (currString.charAt(0) == '-') {
            if (stringLength == 1) {
                isIntConstantCheck = false;
            }
            i = 1;
        }
        for (; i < stringLength; i++) {
            char currChar = currString.charAt(i);
            if (!isDigit(currChar)) {
                isIntConstantCheck = false;
                return isIntConstantCheck; 
            }
        }
        isIntConstantCheck = true;
        return isIntConstantCheck; 
    }

    // FloatConstant := [ - ] Digit { Digit } [ . Digit { Digit } ]
    // Check if a string is a FloatConstant. Will return true if it is an FloatConstant, false otherwise.
    public boolean isFloatConstant(String currString) {
        boolean isFloatConstantCheck = false;
        int lastStringIndex = currString.length() - 1;
        // We can't have '.' be the last character in a float constant. If there is a '.', a digit must follow after it
        if (currString.charAt(lastStringIndex) == '.') {
            return false; 
        }
        // https://www.geeksforgeeks.org/check-if-a-given-string-is-a-valid-number-integer-or-floating-point-in-java/
        try { 
            Float.parseFloat(currString); 
            isFloatConstantCheck = true;
            return isFloatConstantCheck;
        } catch (NumberFormatException e) { 
            isFloatConstantCheck = false;
            return isFloatConstantCheck;
        } 
    }

    // StringConstant := " { ( CharacterLiteral | EscapedCharacter ) } "
    // Check if a string is a string constant. Returns true if it is, false otherwise. 
    public boolean isStringConstant(String currString) {
        int stringLength = currString.length();
        // The smallest a string constant can be is an empty string (""). Therefore, if the size is smaller than that we can just return false
        if (stringLength < 2) {
            return false;
        }
        int lastIndexInt = stringLength - 1;
        boolean isStringConstantCheck = false;
        char firstIndex = currString.charAt(0);
        char lastIndex = currString.charAt(lastIndexInt);

        if (firstIndex != '\"' || lastIndex != '\"' ) {
            isStringConstantCheck = false;
        } else {
            for (int i = 1; i < lastIndexInt; i++) {
                char currChar = currString.charAt(i);
                if (!isCharacterLiteral(currChar) || !isEscapedCharacter(currChar)) {
                    isStringConstantCheck = false;
                    break;
                }
            }
            isStringConstantCheck = true;
        }

        return isStringConstantCheck;
    }

    // Digit := 0 – 9
    // Check if a character falls in the ASCII range for a digit. Returns true if it does, false otherwise
    public boolean isDigit(char currChar) {
        boolean isDigitCheck = false;
        if (currChar >= '0' && currChar <= '9') {
            isDigitCheck = true;
        }
        return isDigitCheck;
    }

    // Alpha := A – Z | a – z
    // Check if a character falls in the ASCII range for an alphabetical character. Returns true if it does, false otherwise
    public boolean isAlpha(char currChar) {
        boolean isAlphaCheck = false;
        if (currChar >= 'A' && currChar <= 'Z' || currChar >= 'a' && currChar <= 'z') {
            isAlphaCheck = true;
        }
        return isAlphaCheck;
    }

    // WhiteSpace := Space | Tab | CarriageReturn | NewLine
    // Check if a character is a whitespace. Returns true if it does, false otherwise
    public boolean isWhiteSpace(char currChar) {
        boolean isWhiteSpaceCheck = false;
        if (currChar == ' ' || currChar == '\t' || currChar == '\r' || currChar == '\n') {
            isWhiteSpaceCheck = true;
        }
        return isWhiteSpaceCheck;
    }

    // CharacterLiteral := Space - ! | # - [ | ] - ~
    // Check if a character is a character literal. Returns true if it does, false otherwise
    public boolean isCharacterLiteral(char currChar) {
        boolean isCharacterLiteralCheck = false;
        if (currChar >= ' ' && currChar <= '!' || currChar >= '#' && currChar <= '[' || currChar >= ']' && currChar <= '~') {
            isCharacterLiteralCheck = true;
        }
        return isCharacterLiteralCheck;
    }

    // EscapedCharacter := \b | \n | \r | \t | \\ | \' | \"
    // Check if a character is an escaped character. Returns true if it does, false otherwise
    public boolean isEscapedCharacter(char currChar) {
        boolean isEscapedCharacterCheck = false;
        if (currChar == '\b' || currChar == '\n' || currChar == '\r' || currChar == '\t' || currChar == '\\' || currChar == '\'' || currChar == '\"') {
            isEscapedCharacterCheck = true;
        }
        return isEscapedCharacterCheck;
    }

    public Token stringToToken (String currString) {
        Token.TokenType typeOfToken;
        boolean isStringKeyword = isKeyword(currString);
        boolean isStringIdentifier = isIdentifier(currString);
        boolean isStringOperator = isOperator(noChar, currString, 1);
        boolean isStringIntConstant = isIntConstant(currString);
        boolean isStringFloatConstant = isFloatConstant(currString);
        boolean isStringStringConstant = isStringConstant(currString);

        if (isStringKeyword) {
            typeOfToken = Token.TokenType.KEYWORD;
            previousConstantOrIdentifier = false;
        } else if (isStringIdentifier) {
            typeOfToken = Token.TokenType.IDENTIFIER;
            previousConstantOrIdentifier = true;
        } else if (isStringOperator) {
            typeOfToken = Token.TokenType.OPERATOR;
            previousConstantOrIdentifier = false;
        } else if (isStringIntConstant) {
            typeOfToken = Token.TokenType.INT_CONSTANT;
            previousConstantOrIdentifier = true;
        } else if (isStringFloatConstant) {
            typeOfToken = Token.TokenType.FLOAT_CONSTANT;
            previousConstantOrIdentifier = true;
        } else if (isStringStringConstant) {
            typeOfToken = Token.TokenType.STRING_CONSTANT;
            previousConstantOrIdentifier = true;
        } else {
            // If the string does not match any of the previous tokens, it is invalid
            typeOfToken = Token.TokenType.INVALID;
            previousConstantOrIdentifier = false;
        }

        Token tokenToReturn = new Token(currString, typeOfToken, currentLineIndex, currentCharIndex);
        return tokenToReturn;
    }

    public String getString () {
        boolean doubleQuotesSeen = false;
        boolean retStringBuilderEmpty = false;
        StringBuilder retStringBuilder = new StringBuilder();
        String retString;
        int aheadIndex = 0;

        while (streamObject.moreAvailable()) {
            if (retStringBuilder.length() < 1) {
                // TODO
                // Get row and index vals
                retStringBuilderEmpty = true;
            }
            char currChar = (char)streamObject.getNextChar();
            char nextChar = (char)streamObject.peekNextChar();

            if (currChar == '\"') {
                doubleQuotesSeen = true;
            }
            // We want to skip whitespaces during tokenizing
            if (!doubleQuotesSeen && isWhiteSpace(currChar)) {
                if (retStringBuilderEmpty) {
                // if (retStringBuilder.length() == 0) {
                    // System.out.println("is");
                    continue;
                } else {
                    break;
                }
            }
            retStringBuilder.append(currChar);
            currentCharIndex++;

            if (!doubleQuotesSeen) {
                String currcharAsString = Character.toString(currChar);
                String nextCharAsString = Character.toString(nextChar);
                String operatorCheck = currcharAsString + nextCharAsString;
                String currStringBuilder = retStringBuilder.toString();
                int currStringBuilderLength = currStringBuilder.length();

                if (isOperator(noChar, operatorCheck, 1)) {
                    // System.out.println("S");
                    continue;
                } else if ((isIntConstant(currStringBuilder) || isFloatConstant(currStringBuilder)) && isOperator(nextChar, noString, 0)) {
                    // System.out.println("E");
                    break;
                } else if (!isOperator(noChar, operatorCheck, 1) && isOperator(currChar, noString, 0) && isOperator(nextChar, noString, 0)) {
                    // System.out.println("Q");
                    break;
                } else if (previousConstantOrIdentifier && isOperator(currChar, noString, 0) &&
                    (isIntConstant(Character.toString(nextChar)) || isFloatConstant(Character.toString(nextChar)))) {
                    // System.out.println("YOLO");
                    break;
                } else if (isOperator(currChar, noString, 0) && 
                    (!isIntConstant(Character.toString(nextChar)) && !isFloatConstant(Character.toString(nextChar)))) {
                    // System.out.println("R");
                    break;
                } else if (isIdentifier(currStringBuilder) && (isOperator(nextChar, noString, 0) || !isIdentifier(Character.toString(nextChar)))) {
                    // System.out.println("F");
                    break;
                } else if (currStringBuilderLength == 1 && (!isAlpha(currChar) && !isDigit(currChar) && isOperator(currChar, noString, 0))) {
                    // System.out.println("Z");
                    break;
                }
            }

            String builderToString = retStringBuilder.toString();
            if (isOperator(nextChar, noString, 0) && isStringConstant(builderToString)) {
                break;
            }
            aheadIndex++;
        }

        char nextChar = (char)streamObject.peekNextChar();
        if (nextChar == '\n') {
            streamObject.getNextChar();
            currentCharIndex++;
            currentLineIndex++;
        } else if (nextChar == ' ') {
            streamObject.getNextChar();
            currentCharIndex++;
        }
        retString = retStringBuilder.toString();
        return retString;
    }

    // Returns the next token without consuming it. If no more tokens are available a None token is returned. 
    public Token peekNextToken() {
        return null;
    }

    // Returns the next token and consumes it. If no more tokens are available a None token is returned.
    public Token getNextToken() {
        if (streamObject.moreAvailable()) {
            String tokenizableString = getString();
            Token retToken = stringToToken(tokenizableString);
            return retToken;
        }
        Token noneToken = new Token("", Token.TokenType.NONE, currentLineIndex, currentCharIndex);
        return noneToken;
    }

    // Print out the token's line number, character position, token type and token text
    public void printToken (Token tokenToPrint) {
        System.out.println("@\t" + Integer.toString(tokenToPrint.getLineNumber()) + ",\t"
                            + Integer.toString(tokenToPrint.getCharPosition()) + "\t"
                            + tokenToPrint.getType().toString() + " " 
                            + "\"" + tokenToPrint.getText() + "\""); 
    }

    public void tokenizeFile (){
        boolean tokenizing = true;
        while (tokenizing) {
            Token newToken = getNextToken();
            Token.TokenType typeOfToken = newToken.getType();
            if (typeOfToken != Token.TokenType.NONE) {
                printToken(newToken);
            } else {
                printToken(newToken);
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            throw new IllegalArgumentException("Please enter the name of the file that you wish to use.");
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];

        // Create a list of strings based on keywords from project 2 description
        // https://javarevisited.blogspot.com/2012/12/how-to-initialize-list-with-array-in-java.html
        List<String> keywordlist = Arrays.asList("unsigned", "char", "short", "int", "long", "float", "double", "while", "if", "return", "void", "main");
        StreamClass stream = new StreamClass(inputtedFileName);
        Scanner scannerObject = new Scanner(stream, keywordlist);

        scannerObject.tokenizeFile();

        // Testing
        // TODO: Fix constant - constant, char, invalid characters, underscore following constant
        // Token newToken = scannerObject.stringToToken("newVar");
        // scannerObject.printToken(newToken);

        // Token newToken1 = scannerObject.stringToToken("5.02");
        // scannerObject.printToken(newToken1);
    }
}