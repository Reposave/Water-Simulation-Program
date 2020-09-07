# Program MakeFIle
# Ardo Dlamini
# 13 March 2020
# Minor Makefile, Controls another Makefile.
#v2.0

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=Terrain.class\
		FlowPanel.class\
		Flow.class\
        
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

docs:	
	javadoc -classpath $(BINDIR) -d $(DOCDIR) $(SRCDIR)/*.java

run1:
	cd bin && $(MAKE) run1
	#changes directory to bin and runs that Makefile.
run2:
	cd bin && $(MAKE) run2
run3: 
	cd bin && $(MAKE) run3
run4:
	cd bin && $(MAKE) run4
run5:
	cd bin && $(MAKE) run5
run6:
	cd bin && $(MAKE) run6
run7:
	cd bin && $(MAKE) run7
run8:
	cd bin && $(MAKE) run8
run9:
	cd bin && $(MAKE) run9

