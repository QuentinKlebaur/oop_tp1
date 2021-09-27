JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		ApplicationServeur.java 	\
		ClientSessionThread.java	\
		Commande.java				\
		StopThread.java

default: classes

classes: $(CLASSES:.java=.class)