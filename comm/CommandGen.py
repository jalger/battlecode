#!/usr/bin/env python
# Skeleton script modified from Noah Spurrier from 
# http://code.activestate.com/recipes/528877/

# Given a list of input files, each of which must be in the format described
# below, makes a Java file out of the input.  Used to quickly generate
# classes we need for communication
#
# File format
# Line 1: Class name, what the .java file will be called
# Line 2: The name of the SubMessageBodyType enum within SubMessageBody.
# We use this in order to keep all of the IDs for messages within one place
# Line 3+: [private | public ] int | boolean | MapLocation | Point [ "[]" || "[][]" ]? identifier;
#
# In other words, lines 3 on accept a declaration of a single int, boolean,
# MapLocation, Point, or a one or two dimensional array of each
#
# Author: Nicholas Dunn
# Date:   April 20, 2009

FILE_EXTENSION = ".java"
ENUM_FILE = "SubMessageBody"
ENUM_NAME = "SubMessageBodyType"
EXTENDS_FILE = "SubMessageBody"

import sys, os, traceback, optparse
import time
import re
from JavaVariable import *
#from pexpect import run, spawn

def main ():

    global options, args
    
    for f in args:
        processFile(open(f, "r"))
     
    
def processFile(f):
    """
    Given an input text file, parses it and makes a corresponding .java
    file with method bodies filled in.
    """

    # Format is 
    # classTitle
    # enumID
    # list of variables
    classTitle = f.readline().strip()
    enumID = f.readline().strip()
    # The rest of the lines inside specify the variables
    variableDeclarations = []
    for line in f:
        # strip out white space (new lines)
        variableDeclarations.append(line.strip())

    for decl in variableDeclarations:
        x = Variable(decl)
     
    variables = [Variable(decl) for decl in variableDeclarations]   
    
    for var in variables:
        print var.toString()
    
    
    newFile = open(classTitle + FILE_EXTENSION, "w")
    writeHeader(f=newFile, title=classTitle, variables=variables)
    newFile.write("{\n")
    
    writeDeclarations(newFile, classTitle, enumID, variables)
    writeFunctions(newFile, classTitle, enumID, variables)
    
    newFile.write("}")
    newFile.close()
    

    
    
def writeHeader(f, title, variables, packageName = "teamJA_ND.comm"):
    f.write("package " + packageName + ";\n")
    f.write("\n\n")
    #unique imports we need
    neededImports = list(set([ var.implementation.neededImport() for var in variables]))
    
    for neededImport in neededImports:
        if (neededImport != None):
            f.write("import " + neededImport + ";\n")
    
    # We also need the clock for timing purposes
    f.write("import battlecode.common.Clock;")
    f.write("\n\n")
    
    f.write("public class " + title + " extends " + EXTENDS_FILE)

    
def writeDeclarations(f, title, enumID, declarations):
    
    for declaration in declarations:
        f.write("\t" + str(declaration) + "\n")
        
    f.write("\tpublic static final int ID = " + ENUM_FILE + "." + enumID + "_ID;\n")
    # Write the static parser declaration
    f.write("\tpublic static final " + title + " PARSER = new " + title + "(" + getDefaultArguments(declarations) + ");\n" )
    f.write("""
    int clockTurnNum;
    int clockByteNum;
    final int BYTES_PER_ROUND = 6000;
    """)
    f.write("\n\n")
    
    
def getDefaultArguments(declarations):
    vals = ""
    for f in declarations:
        vals += f.implementation.default() + ", "
    # strip off extra comma and space
    return vals[:-2]
    
def getRequiredArguments(variables):
    strings = [ v.type + " " + v.name for v in variables ]
    
    result = ""
    for string in strings:
        result += string + ", "
    
    # cut off extra comma and space
    s = result[:-2]
    return s
    
def writeFunctions(f, classTitle, enumID, declarations):
    
    writeConstructor(f, classTitle, declarations)
    
    writeGetLength(f, declarations)

    writeGetID(f)

    writeToIntArray(f, classTitle, declarations)
    
    writeFromIntArray(f, classTitle, declarations)
    
    writeGetters(f, declarations)    
    
    writeTick(f)
    
    writeTock(f)
    
    writeToString(f, classTitle, declarations);
    
def writeGetID(f):
    f.write("\tpublic int getID() { return ID; }\n\n")    
    
def writeConstructor(f, classTitle, declarations):
    #Constructor
    f.write("\tpublic " + classTitle + " (" + getRequiredArguments(declarations) + ") {\n")
    
    for decl in declarations:
        f.write("\t\tthis." + decl.name + " = " + decl.name + ";\n")
        
    f.write("\t}\n\n")
    
    pass

def writeGetters(f, variables):
    for variable in variables:
        name = variable.name[0].upper() + variable.name[1:]
        f.write("\tpublic " + variable.type + " get" + name +"() {\n")
        f.write("\t\treturn " + variable.name + ";\n")
        f.write("\t}\n")

def writeTick(f):
    f.write("""
    public void debug_tick() {
        clockTurnNum = Clock.getRoundNum();
        clockByteNum = Clock.getBytecodeNum();
    }
""")

    
def writeTock(f):
    f.write("""
    public void debug_tock() {
        int turnFinal = Clock.getRoundNum();
        int bytesFinal = Clock.getBytecodeNum() - 1; //The -1 accounts for the cost of calling debug_tock().
        int delta = bytesFinal - clockByteNum + BYTES_PER_ROUND*(turnFinal - clockTurnNum);
        System.out.println(delta + " bytecodes used since calling debug_tick().");
    }
""")

def writeToString(f, className, declarations):
    f.write("\tpublic String toString() {\n")
    f.write("\t\t return \"" + className +"\\n\"")
    if (len(declarations) > 0):
        f.write("+" + " +\n".join(["\t\t" + d.toString() for d in declarations]))
    
    f.write(";\n")
    f.write("\t}\n")



def calculateLength(declarations):
    # minimum length of two
    MIN_LENGTH = "2"
    
    additional = "+".join([decl.implementation.numIntsToRepresent() for decl in declarations])
    
    return condense(MIN_LENGTH + "+" + additional)


def writeGetLength(f, declarations):
    f.write("\tpublic int getLength() {\n")
    f.write("\t\treturn " + calculateLength(declarations) + ";\n")
    f.write("\t}\n")

    
def writeToIntArray(f, classTitle, variables):
    f.write("\tpublic void toIntArray(int[] array, int offset) {\n")
    f.write("\t\tarray[offset] = getLength();\n");
    f.write("\t\tarray[offset + 1] = ID;\n")
    
    counter = "offset + 2"
    for variable in variables:
        var = variable.implementation
        
        ints = var.toInt("array", counter)
        for val in ints:
            f.write("\t\t" + val + "\n")
        
        try:
            counter = str(int(counter) + int(var.numIntsToRepresent()))
        except ValueError:
            counter += " + " + var.numIntsToRepresent()
            
    f.write("\t}\n")
    

    
def writeFromIntArray(f, classTitle, variables):
    f.write("\tpublic " + classTitle + " fromIntArray(int[] array, int offset) {\n")
    
    f.write("\t\tint counter = 2 + offset;\n")
    counter = "counter"
    for variable in variables:
        var = variable.implementation
        
        strings, counterIncrement = var.fromInt("array", counter), var.numIntsToRepresent()
        for line in strings:
            f.write("\t\t" + line + "\n")
        
        # See if the counter and increment can be changed into ints (not all)    
        # counter increments can
        try:
            intCounter = int(counter)
            intIncrement = int(counterIncrement)
            counter = str(intCounter + intIncrement)
        # Couldn't convert to ints, stay in strings
        except ValueError:    
            counter += " + " + counterIncrement    
    
    
    names = [ v.name for v in variables ]

    constructorArgs = ""
    for name in names:
        constructorArgs += name + ", "

    # cut off extra comma and space
    constructorArgs = constructorArgs[:-2]
    
    f.write("\t\treturn new " + classTitle + "(" + constructorArgs + ");\n")
    
    f.write("\t}\n")
    pass
    
def replaceExtension(f, extension = FILE_EXTENSION):
    """ Given a file object, chops off the current file extension and 
    replaces it with string 'extension'
    
    USAGE:
    f = open("MentalState.txt")
    print replaceExtension(f, "java") #prints out "MentalState.java"
    
    """
    extensionIndex = f.name.rindex(".")
    newFileName = ""
    # There was no .* ending to file name
    if extensionIndex == -1:
        newFileName = f.name + extension
    else:
        newFileName = f.name[:extensionIndex] + extension
    return newFileName


# All this comes from http://code.activestate.com/recipes/528877/
if __name__ == '__main__':
    try:
        start_time = time.time()
        parser = optparse.OptionParser(formatter=optparse.TitledHelpFormatter(), usage=globals()['__doc__'], version='$Id$')
        parser.add_option ('-v', '--verbose', action='store_true', default=False, help='verbose output')
        (options, args) = parser.parse_args()
        #if len(args) < 1:
        #    parser.error ('missing argument')
        if options.verbose: print time.asctime()
        main()
        if options.verbose: print time.asctime()
        if options.verbose: print 'TOTAL TIME IN MINUTES:',
        if options.verbose: print (time.time() - start_time) / 60.0
        sys.exit(0)
    except KeyboardInterrupt, e: # Ctrl-C
        raise e
    except SystemExit, e: # sys.exit()
        raise e
    except Exception, e:
        print 'ERROR, UNEXPECTED EXCEPTION'
        print str(e)
        traceback.print_exc()
        os._exit(1)
