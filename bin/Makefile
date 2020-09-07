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
	 @java Flow "medsample_in.txt"

run2:
	 @java Flow "largesample_in.txt"

run3:
	 #Runs one at a time.
	 @java Flow "largesample_in.txt"
	 @java Flow "medsample_in.txt"

run4:
	 @java TerrainApp "large_in.txt" "large_out.txt" "t"

run5:	
	 @python3 MatrixCreate.py

run6: 
	 @java TerrainApp "4x4.txt" "4x4.txt" "t"
	 @java TerrainApp "4x4.txt" "4x4.txt" "r"
	 @java TerrainApp "4x10.txt" "4x10.txt" "t"
	 @java TerrainApp "4x10.txt" "4x10.txt" "r"

run7:
	 @java TerrainApp "256x250.txt" "256x250.txt" "t"
	 @java TerrainApp "256x250.txt" "256x250.txt" "r"
	 @java TerrainApp "512x512.txt" "512x512.txt" "t"
	 @java TerrainApp "512x512.txt" "512x512.txt" "r"

run8:
	 @java TerrainApp "1024x1024.txt" "1024x1024.txt" "t"
	 @java TerrainApp "1024x1024.txt" "1024x1024.txt" "r"
	 @java TerrainApp "2000x2000.txt" "2000x2000.txt" "t"
	 @java TerrainApp "2000x2000.txt" "2000x2000.txt" "r"

run9:
	 @java TerrainApp "4095x4095.txt" "4095x4095.txt" "t"
	 @java TerrainApp "4095x4095.txt" "4095x4095.txt" "r"


