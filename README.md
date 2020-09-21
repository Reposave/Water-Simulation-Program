# Water-Simulation-Program
A program to simulate running water in java.

README

Navigate to “/A2” In terminal you can compile the file using make and run it using
make run1 or make run2. There are two Makefiles, it is easier to use the one inside the bin folder
and can be edited to take any arguments. The Makefile outside the bin folder searches for the
Makefile inside the bin folder and executes it using the command "cd bin && $(MAKE) run"

Alternatively, Navigate to the bin folder, open terminal and use java Flow “filename.txt”

You can also place your Terrain txt data in the Terrain folder or the NewTerrain folder. The program will search both.

• make run1: "medsample_in.txt"

• make run2: "largesample_in.txt" requires a larger screen.

The End Button will terminate the program.

The Reset Button will pause the movement of water first, reset all points then clear the display of WaterUnits.

Pause Button, pauses the movement of water.

Play Button will resume movement of water.
