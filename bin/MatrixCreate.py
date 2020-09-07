#Ardo Dlamini
#Python Testing Code
#09 March 2020

import random
import os

# generate a set of random heights for a specified grid.

def Average(lst): 
    return sum(lst) / len(lst)
 
def IsInt(s):
    try: 
        int(s)
        return True
    except ValueError:
        return False

def main():
	try:

		rows = eval(input("How many rows to create? "))
		columns = eval(input("How many columns to create? "))
		if not (rows > 2 and columns > 2):
			#To avoid infinite loops.
			rows = 3;
			columns = 3;
		else:
			pass

		output_file = open("../NewTerrain/"+str(rows)+"x"+str(columns)+".txt","w")
		output_file.write(str(rows)+" "+str(columns)+"\n")

		count = 0;
		num_of_elements = rows*columns;

		while not(count>=num_of_elements):
			value = round(random.uniform(500, 1000),2)
			output_file.write(str(value)+" ")
			count=count+1
			#resets file pointer.
				
		output_file.close()

	except FileNotFoundError as errno:
		print("File not found")
	finally:
		pass

if __name__ == "__main__":
	main()
