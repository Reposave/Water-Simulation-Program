# Program MakeFIle
# Ardo Dlamini
# 09 March 2020
# Major Makefile

JAVAC=/usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=../src
BINDIR=../bin
DOCDIR=../doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=WaterUnit.class\
		WaterGrid.class\
		Terrain.class\
		WaterFlowPanel.class\
		SwingWaterThread.class\
		WaterThread.class\
		WaterDraw.class\
		FlowPanel.class\
		Flow.class\
		
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

docs:	
	javadoc -classpath $(BINDIR) -d $(DOCDIR) $(SRCDIR)/*.java

run1:
	 @java Flow "medsample_in.txt"

run2:
	 @java Flow "largesample_in.txt"

