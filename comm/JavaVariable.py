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
              MAP_LOCATION_1D_ARRAY: OneDArray(MapLocation(LOOP_NAME_EXT + self.name), self.name)
              
              
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
        
        innerConversion = self.variable.toInt(ints, self.innerIndex(off, "i"))

        # The first element is an int telling how big the array is
        loop = [ints + "[" + off + "] = " + self.name + ".length;",
        self.forLoopStart(), "\t" + self.innerDeclaration()]
        
        
        for line in innerConversion:
            loop.append("\t" + line)
        loop.append("}")
        
        return loop;
        
    def fromInt(self, ints, offset):
        off = condense(offset)
        
        size = "int " + self.name + "size = " + ints + "[" + off + "];";
        typeOfVar = self.variable.type()
        declaration = typeOfVar + "[] " + self.name + " = new " + typeOfVar + "[" + self.name + "size];"
        
        loop = self.forLoopStart()
        loopBody = self.variable.fromInt(ints, self.innerIndex(off, "i"))
        assignment = self.name + "[i] = " + self.variable.name + ";"
        
        a = [size, declaration, loop]
        for i in loopBody:
            a.append("\t" + i)
        a.append("\t" + assignment)
        a.append("}")
        return a
        
        pass


class TwoDArray(Array):
    
    def __init__(self, varImpl):
        self.variable = varImpl      
    


