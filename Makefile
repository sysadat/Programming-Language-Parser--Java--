JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Token.java \
		PeekableCharacterStream.java \
		PeekableCharacterStreamClass.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class