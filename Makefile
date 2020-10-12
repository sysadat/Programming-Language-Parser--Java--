# https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Token.java \
		PeekableCharacterStream.java \
		StreamClass.java \
		Scanner.java \
		CSVParser.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class