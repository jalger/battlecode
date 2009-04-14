#!/usr/bin/env python


DEFAULT_NUM_ARGS = 2
SCOPE_NUM_ARGS = 3

INT = "int"
BOOLEAN = "boolean"
POINT = "Point"
MAP_LOCATION = "MapLocation"

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
    
    def numIntsToRepresent(self):
        pass

    def fromInt(self, ints, offset):
        pass

   	def toInt(self, index):
   	    pass

        
class Int(VariableImplementation):
    def default(self):
        return "0"
    
    def numIntsToRepresent(self):
        return "1"
        
    def fromInt(self, ints, offset):
        self.value = ints    
        
    def toInt(self):
        return [self.name]
        
class Point(VariableImplementation):
    def default(self):
        return "null"

    def numIntsToRepresent(self):
        return "2"

    def fromInt(self, ints, offset):
        this.x = ints[0]
        this.y = ints[1]
    
    def toInt(self):
        return [self.name + ".getX()", self.name + ".getY()"]
        
class Boolean(VariableImplementation) :
    def default(self):
        return "false"

    def numIntsToRepresent(self):
        return "1"

    def fromInt(self, ints, offset):
        pass
        
    def toInt(self):
        return [self.name + " ? 1 : 0"]
        
class MapLocation(VariableImplementation) :
    def default(self):
        return "null"

    def numIntsToRepresent(self):
        return "2"

    def fromInt(self, ints):
        this.x = ints[0]
        this.y = ints[1]

    def toInt(self):
        return [self.name + ".getX()", self.name + ".getY()"]


class Array(VariableImplementation):
    def default(self):
        return "null"
        
                
