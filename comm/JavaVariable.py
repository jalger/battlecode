#!/usr/bin/env python


DEFAULT_NUM_ARGS = 2
SCOPE_NUM_ARGS = 3

INT = "int"
BOOLEAN = "boolean"
POINT = "Point"
MAP_LOCATION = "MapLocation"

PACKAGE = "teamJA_ND"
POINT_LOC = PACKAGE + "." + POINT
MAP_LOCATION_LOC = "battlecode.common.MapLocation";

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
        }[self.type]
        
        
        
    def __repr__(self):
        return self.scope + " " + self.type + " " + self.name + ";"
    
   
	    

class VariableImplementation:
    def __init__(self, name):
        self.name = name
    
    # By default, do not need an import statement
    def neededImport(self):
        return None
    
    def numIntsToRepresent(self):
        pass

    def fromInt(self, ints, offset):
        """
        Returns a list of Strings, one per line, giving the steps necessary
        to recreate this variable from the int array, as well as the string
        expression of how many ints to advance the counter
        """
        pass

   	def toInt(self):
   	    pass

        
class Int(VariableImplementation):
    
    def default(self):
        return "0"
    
    def numIntsToRepresent(self):
        return "1"
        
    def fromInt(self, ints, offset):
        return ["int " + self.name + " = " + ints + "[" + str(offset) + "];"], self.numIntsToRepresent()

        
    def toInt(self):
        return [self.name]
        
class Point(VariableImplementation):
    def neededImport(self):
        return POINT_LOC
    
    def default(self):
        return "null"

    def numIntsToRepresent(self):
        return "2"

    def fromInt(self, ints, offset):
        try:
            counterPlusOne = str(int(offset) + 1)
        except ValueError:
            counterPlusOne = offset + " + 1"
            print offset, counterPlusOne
            
        return ["Point " + self.name + " = new Point(" + ints + "[" + offset + "], " + ints + "[" + counterPlusOne + "]);"], self.numIntsToRepresent()
        
    def toInt(self):
        return [self.name + ".x", self.name + ".y"]
        
class Boolean(VariableImplementation) :
    def default(self):
        return "false"

    def numIntsToRepresent(self):
        return "1"

    def fromInt(self, ints, offset):
        return ["boolean " + self.name + " = (" + ints + "[" + str(offset) + "] == 1);"], self.numIntsToRepresent() 
        
    def toInt(self):
        return [self.name + " ? 1 : 0"]
        
class MapLocation(VariableImplementation) :
    def neededImport(self):
        return MAP_LOCATION_LOC;
    
    def default(self):
        return "null"

    def numIntsToRepresent(self):
        return "2"

    def fromInt(self, ints, offset):
        try:
            counterPlusOne = str(int(offset) + 1)
        except ValueError:
            counterPlusOne = offset + " + 1"
        return ["MapLocation " + self.name + " = new MapLocation(" + ints + "[" + str(offset) + "], " + ints + "[" + counterPlusOne + "]);"], self.numIntsToRepresent()

    def toInt(self):
        return [self.name + ".getX()", self.name + ".getY()"]


class Array(VariableImplementation):
    def default(self):
        return "null"
        

class Int1DArray(Array):
    def numIntsToRepresent(self):
        return self.name + ".length"
        
    def toInt(self):    
        pass