import java.util.*;
import java.io.IOException;

// Create the Scanner class
public class Scanner {
    // Class attributes
    StreamClass streamObject;
    List<String> listOfKeywords;
    int currentLineIndex;
    int currentCharIndex;
    String[] stringOfSingleCharOperators;
    String[] stringOfDoubleCharOperators;
    String[] stringOfOperators;
    Set<String> setOfOperators;
    boolean previousConstantOrIdentifier;
    static String noString = "S";
    static char noChar = 'C';
    boolean nextCharIsNewLine = false;

    // Constructor that takes in a stream and a list of keywords.
    public Scanner(StreamClass stream, List<String> keywordlist){
        streamObject = stream;
        listOfKeywords = keywordlist;
        currentLineIndex = 1;
        currentCharIndex = 1;
        stringOfSingleCharOperators = new String[] {"(", ",", ")", "{", "}", "=", "<", ">", "+", "-", "*", "/", ";"};
        stringOfDoubleCharOperators = new String[] {"==", "<=", ">=", "!="};
        // https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
        stringOfOperators = Arrays.copyOf(stringOfSingleCharOperators, stringOfSingleCharOperators.length + stringOfDoubleCharOperators.length);
        System.arraycopy(stringOfDoubleCharOperators, 0, stringOfOperators, stringOfSingleCharOperators.length, stringOfDoubleCharOperators.length);
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
        } else if (stringLength == 2) {
            char initialIndex = currString.charAt(0);
            char secondIndex = currString.charAt(1);
            if (initialIndex == '\"' && secondIndex == '\"') {
                return true;
            }
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
        Token tokenToReturn = new Token(currString, typeOfToken, currentLineIndex, currentCharIndex - currString.length() - 1);
        if (nextCharIsNewLine) {
            currentLineIndex++;
            currentCharIndex = 1;
            nextCharIsNewLine = false;
        }
        return tokenToReturn;
    }

    public String peekString() {
        boolean doubleQuotesSeen = false;
        StringBuilder retStringBuilder = new StringBuilder();
        String retString;
        int aheadIndex = 0;
        for (; streamObject.peekAheadChar(aheadIndex)!= -1; aheadIndex++) {
            boolean retStringBuilderEmpty = false;
            if (retStringBuilder.length() < 1) {
                retStringBuilderEmpty = true;
            }
            char currChar = (char)streamObject.peekNextChar();
            char nextChar = (char)streamObject.peekAheadChar(aheadIndex + 1);

            if (currChar == '\"') {
                doubleQuotesSeen = true;
            }

            // We want to skip whitespaces during tokenizing
            if (!doubleQuotesSeen && isWhiteSpace(currChar)) {
                if (retStringBuilderEmpty) {
                    continue;
                } else {
                    break;
                }
            }
            retStringBuilder.append(currChar);

            if (!doubleQuotesSeen) {
                String currcharAsString = Character.toString(currChar);
                String nextCharAsString = Character.toString(nextChar);
                String operatorCheck = currcharAsString + nextCharAsString;
                String currStringBuilder = retStringBuilder.toString();
                int currStringBuilderLength = currStringBuilder.length();

                if (isOperator(noChar, operatorCheck, 1)) {
                    continue;
                } else if ((isIntConstant(currStringBuilder) || isFloatConstant(currStringBuilder)) && isOperator(nextChar, noString, 0)) {
                    break;
                } else if (!isOperator(noChar, operatorCheck, 1) && isOperator(currChar, noString, 0) && isOperator(nextChar, noString, 0)) {
                    break;
                } else if (previousConstantOrIdentifier && isOperator(currChar, noString, 0) &&
                    (isIntConstant(Character.toString(nextChar)) || isFloatConstant(Character.toString(nextChar)))) {
                    break;
                } else if (isOperator(currChar, noString, 0) && 
                    (!isIntConstant(Character.toString(nextChar)) && !isFloatConstant(Character.toString(nextChar)))) {
                    break;
                } else if (isIdentifier(currStringBuilder) && (isOperator(nextChar, noString, 0) || 
                    (!isIdentifier(Character.toString(nextChar)) && !isDigit(nextChar)))) {
                    break;
                } else if (currStringBuilderLength == 1 && (!isAlpha(currChar) && !isDigit(currChar) && !isOperator(currChar, noString, 0))) {
                    break;
                }
            }

            String builderToString = retStringBuilder.toString();
            if (isOperator(nextChar, noString, 0) && isStringConstant(builderToString)) {
                break;
            }
        }

        char nextChar = (char)streamObject.peekNextChar();
        if (nextChar == '\n' || nextChar == ' ') {
            aheadIndex++;
        }
        retString = retStringBuilder.toString();
        return retString;
    }

    public String getString () {
        boolean doubleQuotesSeen = false;
        StringBuilder retStringBuilder = new StringBuilder();
        String retString;

        while (streamObject.moreAvailable()) {
            boolean retStringBuilderEmpty = false;
            if (retStringBuilder.length() < 1) {
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
                    continue;
                } else if ((isIntConstant(currStringBuilder) || isFloatConstant(currStringBuilder)) && isOperator(nextChar, noString, 0)) {
                    break;
                } else if (!isOperator(noChar, operatorCheck, 1) && isOperator(currChar, noString, 0) && isOperator(nextChar, noString, 0)) {
                    break;
                } else if (previousConstantOrIdentifier && isOperator(currChar, noString, 0) &&
                    (isIntConstant(Character.toString(nextChar)) || isFloatConstant(Character.toString(nextChar)))) {
                    break;
                } else if (isOperator(currChar, noString, 0) && 
                    (!isIntConstant(Character.toString(nextChar)) && !isFloatConstant(Character.toString(nextChar)))) {
                    break;
                } else if (isIdentifier(currStringBuilder) && (isOperator(nextChar, noString, 0) || 
                        (!isIdentifier(Character.toString(nextChar)) && !isDigit(nextChar)))) {
                    break;
                } else if (currStringBuilderLength == 1 && (!isAlpha(currChar) && !isDigit(currChar) && !isOperator(currChar, noString, 0) &&
                            (!isAlpha(nextChar) && !isDigit(nextChar) && !isOperator(nextChar, noString, 0)))) {
                    System.out.println(!isOperator(currChar, noString, 0));
                    System.out.println(!isAlpha(currChar));
                    System.out.println("z");
                    break;
                }
            }

            String builderToString = retStringBuilder.toString();
            if (isOperator(nextChar, noString, 0) && isStringConstant(builderToString)) {
                break;
            }
        }

        char nextChar = (char)streamObject.peekNextChar();
        if (nextChar == '\n') {
            streamObject.getNextChar();
            nextCharIsNewLine = true;
        } else if (nextChar == ' ') {
            streamObject.getNextChar();
            currentCharIndex++;
        }
        retString = retStringBuilder.toString();
        return retString;
    }

    // Returns the next token without consuming it. If no more tokens are available a None token is returned. 
    public Token peekNextToken() {
        if (streamObject.moreAvailable()) {
            String tokenizableString = peekString();
            Token retToken = stringToToken(tokenizableString);
            return retToken;
        }
        Token noneToken = new Token("", Token.TokenType.NONE, currentLineIndex + 1, 1);
        return noneToken;
    }

    // Returns the next token and consumes it. If no more tokens are available a None token is returned.
    public Token getNextToken() {
        if (streamObject.moreAvailable()) {
            String tokenizableString = getString();
            Token retToken = stringToToken(tokenizableString);
            return retToken;
        }
        Token noneToken = new Token("", Token.TokenType.NONE, ++currentLineIndex, 1);
        return noneToken;
    }

    // Print out the token's line number, character position, token type and token text
    public void printToken (Token tokenToPrint) {
        // Don't print out the double quotes when we are printing out a string constant
        if (tokenToPrint.getType() == Token.TokenType.STRING_CONSTANT) {
            System.out.println("@\t" + Integer.toString(tokenToPrint.getLineNumber()) + ",\t"
            + Integer.toString(tokenToPrint.getCharPosition()) + "\t"
            + tokenToPrint.getType().toString() + " " 
            + tokenToPrint.getText()); 
        } else {
            System.out.println("@\t" + Integer.toString(tokenToPrint.getLineNumber()) + ",\t"
            + Integer.toString(tokenToPrint.getCharPosition()) + "\t"
            + tokenToPrint.getType().toString() + " " 
            + "\"" + tokenToPrint.getText() + "\""); 
        }
    }

    public void tokenizeFile (){
        while (peekNextToken().getType() != Token.TokenType.NONE) {
            Token newToken = getNextToken();
            printToken(newToken);
        }
        Token noneToken = new Token("", Token.TokenType.NONE, ++currentLineIndex, 1);
        printToken(noneToken);
    }

    public static void main(String[] args) throws IOException {
        // TODO: 
        // - invalid for escaped character string 
        // - multiple line invalid 
        // - invalid for string constant 
        // - why string constant only comes back with one double quote

        // If the user did not specify a file or put more than one file
        if (args.length != 1) {
            throw new IllegalArgumentException("Please enter the name of the file that you wish to use.");
        }

        // Get the name of the file inputted from the user in the command line
        String inputtedFileName = args[0];
        StreamClass stream = new StreamClass(inputtedFileName);

        // Create a list of strings based on keywords from project 2 description
        String[] stringOfKeyWords = getKeywords();
        List<String> keywordlist =  Arrays.asList(stringOfKeyWords);
        Scanner scannerObject = new Scanner(stream, keywordlist);

        scannerObject.tokenizeFile();

    }

    // Helper function that takes in key words, in this instance the keywords from project 2, and then returns them
    public static String[] getKeywords() {
        String[] retString = new String[] {"unsigned", "char", "short", "int", "long", "float", "double", "while", "if", "return", "void", "main"};
        return retString;
    }
}