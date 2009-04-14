

""" 
This python code is a script that helps automate some of the drudgery of
writing the Java classes needed for communication.

Input files will be in the following format: 



Accepted keywords:

int
int[]
int[][]
boolean
boolean[]
boolean[][]
MapLocation
MapLocation[]
MapLocation[][]
Point
Point[]
Point[][]


byte
int
boolean
MapLocation
Point

[]
List<>
"""


def processFile(self, f):
    # Format is 
    # classTitle
    # enumID
    # list of variables
    classTitle = f.readline()
    enumID = f.readline()
    # The rest of the lines inside specify the variables
    
    print classTitle, enumID
    

