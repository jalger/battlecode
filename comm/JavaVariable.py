#!/usr/bin/env python


DEFAULT_NUM_ARGS = 2
SCOPE_NUM_ARGS = 3

INT = "int"
BOOLEAN = "boolean"
POINT = "Point"
MAP_LOCATION = "MapLocation"

INT_1D_ARRAY = INT + "[]"
BOOLEAN_1D_ARRAY = BOOLEAN + "[]"
POINT_1D_ARRAY = POINT + "[]"
MAP_LOCATION_1D_ARRAY = MAP_LOCATION + "[]"

INT_2D_ARRAY = INT + "[][]"
BOOLEAN_2D_ARRAY = BOOLEAN + "[][]"
POINT_2D_ARRAY = POINT + "[][]"
MAP_LOCATION_2D_ARRAY = MAP_LOCATION + "[][]"

LOOP_NAME_EXT = "tmp"


PACKAGE = "teamJA_ND"
POINT_LOC = PACKAGE + "." + POINT
MAP_LOCATION_LOC = "battlecode.common.MapLocation";

def counterIncrement(counter, amount):
    try:
        newCounter = str(int(counter) + int(amount))
    except ValueError:
        newCounter = counter + " + " + amount
    return newCounter
    
    
    
def condense(string):
    """
    Given a String with int literals, attempts to consolidate all of the
    added literals into a single added literal
    """
    
    nums = string.split("+")
    numString = ""
    
    intSum = 0
    nonLiteralCount = 0
    for num in nums:
        try:
            intSum += int(num)
        except ValueError:
            nonLiteralCount += 1
            # Don't add the plus sign in front unless there are other values
            if numString == "":
                numString += num
            else:
                numString += "+" + num
        pass
    
    if nonLiteralCount > 0:
        numString += "+" + str(intSum)
    else:
        numString = str(intSum)
        
    return numString
    


class Variable:
    
      #private int x;
    def __init__(self, string):
        semicolonIndex = string.rindex(";")
        stripped = string[:semicolonIndex].strip()
        
        strings = stripped.split(" ")
        
        length = len(strings)
        if length == DEFAULT_NUM_ARGS:
            self.scope = ""
            self.type = strings[0]
            self.name = strings[1]
        else:
            self.scope = strings[0]
            self.type = strings[1]
            self.name = strings[2]
        
        # TODO: arrays
        
        # Switch on the type of the variable
        self.implementation = {
              INT: Int(self.name),
              BOOLEAN: Boolean(self.name),
              POINT: Point(self.name),
              MAP_LOCATION: MapLocation(self.name),
              # 1D arrays
              INT_1D_ARRAY: OneDArray(Int(LOOP_NAME_EXT + self.name), self.name),
              BOOLEAN_1D_ARRAY: OneDArray(Boolean(LOOP_NAME_EXT + self.name), self.name),
              POINT_1D_ARRAY: OneDArray(Point(LOOP_NAME_EXT + self.name), self.name),
              MAP_LOCATION_1D_ARRAY: OneDArray(MapLocation(LOOP_NAME_EXT + self.name), self.name),
              
              #2D arrays
              INT_2D_ARRAY: TwoDIntArray(Int(LOOP_NAME_EXT + self.name), self.name),
              BOOLEAN_2D_ARRAY: TwoDBooleanArray(Boolean(LOOP_NAME_EXT + self.name), self.name),
              POINT_2D_ARRAY: TwoDPointArray(Point(LOOP_NAME_EXT + self.name), self.name),
              MAP_LOCATION_2D_ARRAY: TwoDMapLocationArray(MapLocation(LOOP_NAME_EXT + self.name), self.name)
          
              
        }[self.type]
        
        
        
    def __repr__(self):
        return self.scope + " " + self.type + " " + self.name + ";"
    
   
	    

class VariableImplementation:
    def __init__(self, name):
        self.name = name
    
    def type(self):
        return None
    
        
    
    # By default, do not need an import statement
    def neededImport(self):
        return None
    
    def numIntsToRepresent(self):
        pass

    def fromInt(self, ints, offset):
        """
        Returns a list of Strings, one per line, giving the steps necessary
        to recreate this variable from the int array
        """
        pass

   	def toInt(self, ints, offset):
   	    pass

        
class Int(VariableImplementation):
    def type(self):
        return "int"
    
    def default(self):
        return "0"
    
    def numIntsToRepresent(self):
        return "1"
        
    def fromInt(self, ints, offset):
        off = condense(offset)
        
        return ["int " + self.name + " = " + ints + "[" + off + "];"]

        
    def toInt(self, ints, offset):
        off = condense(offset)
        
        return [ints + "[" + off + "] = " + self.name + ";"]
        
class Point(VariableImplementation):
    def type(self):
        return "Point"
    
    
    def neededImport(self):
        return POINT_LOC
    
    def default(self):
        return "null"

    def numIntsToRepresent(self):
        return "2"

    def fromInt(self, ints, offset):
        
        off = condense(offset)
        
        counterPlusOne = condense(counterIncrement(off, "1"))
            
        return ["Point " + self.name + " = new Point(" + ints + "[" + offset + "], " + ints + "[" + counterPlusOne + "]);"]
        
    def toInt(self, ints, offset):
        return [ints + "[" + offset + "] = " + self.name + ".x;", ints + "[" + counterIncrement(offset, "1") + "] = " + self.name + ".y;"]
        
class Boolean(VariableImplementation) :
    def type(self):
        return "boolean"
    
    def default(self):
        return "false"

    def numIntsToRepresent(self):
        return "1"

    def fromInt(self, ints, offset):
        off = condense(offset)
        
        return ["boolean " + self.name + " = (" + ints + "[" + off + "] == 1);"]
        
    def toInt(self, ints, offset):
        return [ints + "[" + condense(offset) + "] = " + self.name + " ? 1 : 0;"]
        
class MapLocation(VariableImplementation) :
    def type(self):
        return "MapLocation"
    
    
    def neededImport(self):
        return MAP_LOCATION_LOC;
    
    def default(self):
        return "null"

    def numIntsToRepresent(self):
        return "2"

    def fromInt(self, ints, offset):
        off = condense(offset)
        counterPlusOne = condense(counterIncrement(off, "1"))
        return ["MapLocation " + self.name + " = new MapLocation(" + ints + "[" + off + "], " + ints + "[" + counterPlusOne + "]);"]

    def toInt(self, ints, offset):
        
        return [ints + "[" + condense(offset) + "] = " + self.name + ".getX();", ints + "[" + condense(counterIncrement(offset, "1")) + "] = " + self.name + ".getY();"]


class Array(VariableImplementation):
    def default(self):
        return "null"
        
    def neededImport(self):
        return self.variable.neededImport()

class OneDArray(Array):
    
    def __init__(self, varImpl, name = "arrayType"):
        self.variable = varImpl     
        self.name = name 

    def numIntsToRepresent(self):
        return "1 + (" + self.variable.numIntsToRepresent() + " * " + self.name + ".length)"

    # All for loops will start the same
    def forLoopStart(self):
        return "for (int i = 0; i < " + self.name + ".length; i++) {"
        
    def innerIndex(self, initialOffset, counterVar):
        # one extra space for the storage of size of array
        return condense(initialOffset + " + (" + self.variable.numIntsToRepresent() + " * " + counterVar + ") + 1")


    def innerDeclaration(self):
        return self.variable.type() + " " + self.variable.name + " = " + self.name + "[i];"


    def toInt(self, ints, offset):
        off = condense(offset)
        
        index = "\tint startIndex = " +self.innerIndex(off, "i") + ";"
        loopBody = self.variable.toInt(ints, "startIndex")
        innerConversion = self.variable.toInt(ints, self.innerIndex(off, "i"))

        # The first element is an int telling how big the array is
        loop = [ints + "[" + off + "] = " + self.name + ".length;",
        self.forLoopStart(), index, "\t" + self.innerDeclaration()]
        
        
        for line in loopBody:
            loop.append("\t" + line)
        loop.append("}")
        
        return loop;
        
    def fromInt(self, ints, offset):
        off = condense(offset)
        
        size = "int " + self.name + "size = " + ints + "[" + off + "];";
        typeOfVar = self.variable.type()
        declaration = typeOfVar + "[] " + self.name + " = new " + typeOfVar + "[" + self.name + "size];"
        
        loop = self.forLoopStart()
        index = "\tint startIndex = " +self.innerIndex(off, "i") + ";"
        loopBody = self.variable.fromInt(ints, "startIndex")
        assignment = self.name + "[i] = " + self.variable.name + ";"
        
        a = [size, declaration, loop, index]
        for i in loopBody:
            a.append("\t" + i)
        a.append("\t" + assignment)
        a.append("}")
        return a
        


class TwoDArray(Array):
    """ 
    A two-dimensional array in our case must be rectangular, not jagged.  In
    other words, for all i, array[i].length == array[i+1].length
    """

    
    def __init__(self, varImpl, name = "arrayType"):
        self.variable = varImpl     
        self.name = name 

    def numIntsToRepresent(self):
        numInts = self.variable.numIntsToRepresent()
        return "2 + (" + numInts + " * (" + self.numRows() + "* " + self.numCols() + "))"
    
    
    def numRows(self):
        return self.name + ".length"
        
    def numCols(self):
        return self.name + "[0].length"
    
    # Loop over the rows
    def outerLoopStart(self):
        return "for (int i = 0; i < " + self.numRows() + "; i++)"
    
    # Loop over the columns
    def innerLoopStart(self):
        return "for (int j = 0; j < " + self.numCols() + "; j++)"
    
    
    def innerToLoop(self, array, offset):
        pass
    
    def convertToOneDIndex(self, i, j, numRows, numCols, offset):
        numIntsToRepr = self.variable.numIntsToRepresent()
        return condense("(" + i + " * " + numCols + "* " + numIntsToRepr + ") + (" + numIntsToRepr + " * " + j + ") + " + offset)
    
    # Position 0 = numRows, 1 = numCols, all the rest 
    def toInt(self, ints, offset):
        off = condense(offset)

        numRowsName = self.name + "numRows"
        numColsName = self.name + "numCols"
        
        numRows = "int " + numRowsName + " = " + self.numRows() + ";"
        numCols = "int " + numColsName + " = " + self.numCols() + ";"
        
        rowDecl = ints + "[" + off + "] = " + numRowsName + ";"
        colDecl = ints + "[" + condense(off + " + 1") + "] = " + numColsName + ";"
        outer = self.outerLoopStart() + "{"
        inner = "\t" + self.innerLoopStart() + "{"
        index = "\t\tint startIndex = " + self.convertToOneDIndex("i", "j", numRowsName, numColsName, offset + " + 2") + ";"

        
        loop = [numRows, numCols, rowDecl, colDecl, outer, inner, index]
        
        
        body = self.innerToLoop(ints, condense("startIndex"))
        for line in body:
            loop.append("\t\t" + line)
        
        loop.append("\t}")
        loop.append("}")
        
        return loop
        
    def fromInt(self, ints, offset):
        off = condense(offset)
        
        numRowsName = self.name + "numRows"
        numColsName = self.name + "numCols"
        
        
        numRows = "int " + numRowsName + " = " + ints + "[" + off + "];";
        numCols = "int " + numColsName + " = " + ints + "[" + condense(off + "+ 1") + "];";
        
        typeOfVar = self.variable.type()
        declaration = typeOfVar + "[][] " + self.name + " = " + \
        "new " + typeOfVar + "[" + self.name + "numRows][" + self.name + "numCols];"
        
        outer = self.outerLoopStart() + "{"
        inner = "\t" + self.innerLoopStart() + "{"
        index = "\t\tint startIndex = " +self.convertToOneDIndex("i", "j", self.name + "numRows", self.name + "numCols", offset + " + 2") + ";"
        loopBody = self.variable.fromInt(ints, "startIndex")
        assignment = "\t\t" + self.name + "[i][j] = " + self.variable.name + ";"
        
        
        loop = [numRows, numCols, declaration, outer, inner, index]
        
        for line in loopBody:
            loop.append("\t\t" + line)
        
        loop.append(assignment)
        loop.append("\t}")
        loop.append("}")
        return loop

class TwoDIntArray(TwoDArray):
    def innerToLoop(self, array, offset):
        return [array + "[" + offset + "] = " + self.name + "[i][j];"]
        
        
        
class TwoDBooleanArray(TwoDArray):
    def innerToLoop(self, array, offset):
        return [array + "[" + offset + "] = " + self.name + "[i][j] ? 1 : 0;"]
        
class TwoDPointArray(TwoDArray):
    def innerToLoop(self, array, offset):
        return [array + "[" + offset + "] = " + self.name + "[i][j].x;", array + "[" + condense(offset + " + 1") + "] = " + self.name + "[i][j].y;"]
        
class TwoDMapLocationArray(TwoDArray):
    def innerToLoop(self, array, offset):
        return [array + "[" + offset + "] = " + self.name + "[i][j].getX();", array + "[" + condense(offset + " + 1") + "] = " + self.name + "[i][j].getY();"]
