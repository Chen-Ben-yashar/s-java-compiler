# s-java-compiler
s-java is asimplified version of Java. This Program receives s-java files and makes sure they compile correctly (according to the s-java assumptions)



=======================
=  Files Description:  =
=======================

ex6 - package
    declared - package
        Method.java
        Variable.java

    exceptions - package
        CodeLineException.java
        InvalidCodeStructureException.java
        InvalidIfOrWhileException.java
        InvalidMethodCallException.java
        InvalidMethodException.java
        SjavaException.java
        SjavaIOException.java
        VariableMismatchException.java

    main - package
        JavaParser.java
        Sjavac.java

    MethodFactory.java
    RegexStatements.java
    VariablesFactory.java
    VariableVerifier.java


=============
=  Design:  =
=============

Our design is built as following:

The package "main" consists of 2 classes, javaParser and Sjavac. The Sjavac class is where the main function
runs, and it is where the exception cases are handled. The main calls the two of the methods of the javaParser
class. First, it calls the method "firstCodeCheck".

For each code line, "firstCodeCheck" verifies that the line's format is correct. This is done by calling
functions in the RegexStatements class. The RegexStatements contains all regular expressions necessary for the
verifying done throughout the program.

Furthermore, "firstCodeCheck" verifies the scope correctness (i.e. opening and closing {} match), and creates
the Global Variables (globals only!) and Methods in the code.
The variables and Methods are built using the Variable Factory and the Method Factory. Each of those classes
creates a new object, while verifying that it is a correct variable or function. These verifications are done
using the "VariableVerifier" class. The "VariableVerifier" class checks for edge cases, such as: declared
type matches assigned value ("String a = 5;"), assigning variable to variable issues ("int a = b;"), etc.

This is a good time to point out that our program contains a few lists and stacks that are used all through
the program's run:
-	A list of methods: Each slot is a Method object, containing all relevant data of the function (
        i.e. declared variables, name etc.).
-	A stack of variables: each slot contains a Variable object, containing all relevant data (i.e. name, type,
        is it assigned, is it global etc.). This stack is constantly updated, and at all times contains all
        the variables that the program should acknowledge. The Stack structure supports the FIFO element,
        which we used to update variables in between scopes.
-	A stack of scopes: this stack contains counters of the number of variables in each scope. In this way,
        when we exit a scope we delete all scope-related variables – this is done according to the
        scope-counter stack.

By the end of "firstCodeCheck", the Method list is built, and the Variable stack is updated to contain all
global variables, and the Method list contains all the Methods declared throughout the code.
After the "firstCheckCode", the "JavaParser" runs the "secondCodeCheck". This function runs through the code
again, and this time handles the variables and method calls that are NOT in the global scope. It verifies
variables assignment and function calls. Amongst others, this function handles the following edge cases:
assigning variable to illegal variable (no such variable in scope or outside it), incorrect method call etc.
Exceptions are built as a separate package, and are explained later in the README.


================================
=  Design issues and debates:  =
================================

The issues mainly surrounded about implementing the scope checking,
We initially wanted to implement the scopes as a Scopes Objects which would have been independent objects and
contain the variables of the outer scope as they were initialized, and then add Variables that are initialized
inside.
But, because we are working on a Java code check, We didn't need to do that (maybe if we wanted to implement
a compiler and a debugger, it will be a good design).

After that, we though about doing the scopes with a recursion, when each call to the recursion we
send the current set of variables, and when the depth of the recursion ends, we delete the added variables.
This design is complex and does not naturally fit our purpose of Code verifying.
We don't need a recursion to go over the inner scopes because we have a chronological order,
and if approaching the problem according to the current state we're in (i.e. inside an if scope).
We understood that we can implement it much better and efficient.
The design we finally chose is simple and also the space-complexity doesn't sky rocket.

Finally, we chose the design regarding the state of the code line.
Because the Sjava code is operating chronologically, we could use the state-based scoping of the
code, while we store the added variables in the stack and the number of the elements we added,
and we therefore we can remove them easily when the finishing the current scope.


================================
=  Design issues and debates:  =
================================

Of course, our initial UML was indeed initial, and lacked a few functions we later added.
Even so, all classes in the initial design exist in our program and all the main functions exists.
The main addition is that we added the class "Variable Verifier".
At the beginning we thought that the verifying would be part of the "Variable Factory", but later we
understood that it contained a few big functions,
and that we needed it in other places as well (for instance in the "Sjava Parser").
The main functions of the Class are:
    "declarationSameScopeVariable" - Function receives a Variable and verifies that there is no other Variable
                                     with the same name WITHIN THE SAME SCOPE.

    "checkIfAssignVariableExists" - Function receives a Variable that is assigned to another variable, and
                                    verifies that the assignment is legal.
                                    The function handles cases such as "int a = b;" .

    "checkVariableType" - Function receives a non-assigned Variable Name and a Variable Value, and searches
                          for the Value's assignment - then verifies the declaration and value are of the same
                          type.  This function handles the case "a=4;" - function will check "a" was assigned
                          and is of "int" type.



EXCEPTION HANDLING:
    We created a package called "exceptions". Every exception is a class of its own, and they all inherit from
    the "SjavaException" class. Every Exception class contains a short relevant default message. The
    Exceptions are thrown throughout the program, and are handled in the main function (printing relevant
    message and returning relevant number).

    Exceptions can be thrown in one of the following processes: trying to open the code file (results in an
    SjavaIOException), reading an invalid line, creating a Method or Variable Object, assigning variables or
    calling methods.

---

ADDING NEW VARIABLE-TYPE:
    If we want to add a new variable we must change the following:

        -	Updating the regular expressions to include the new type that was added.

        -	Update the "Variable Factory" to include such a type and create a proper instance of it.

        -	Update the "Variable Verifier" class to check validity of the new type added and correspond
            correctly with existing types.

        -   The if/while function to support (or not support) the new variable boolean operation.


---

ADDITIONAL PROPERTIES - for the future (not implemented)

    1.	Adding new Method Types:

           -    Adding new Method types is possible, with the following changes to our code: First, we would
                add a field to the "Method" class holding it's "type". Then, we would add the new types to
                our regular
                expressions that check for method declaration so that they would contain the new types.
                also, the "Variable Verifier" class should be updated, as variables will now be allowed to be
                assigned with the function call (i.e. "int a = foo();".

           -    We also want to check if the return statement returning value with the correct type.


    2. Using Methods of Standard Java:
            If we want to support Standard Java methods,
            We will need to implement new verifiers and exceptions depending on the Java Standard Method.
            The verifier will check the input args of the Java Standard Method,
            and compare them to the called Java Standard Method arguments, by number of arguments and
            their type.
            We implemented a similar verifier for the called Sjava method.
            Also, we will check if the Java Standard Method needs to return a value, and we will check
            if we assign this value to the appropriate variable (i.e. check type, if not final, and
            if declared).
            Of course we will need to update our RegexStatements Class to parse correctly the Java Standard
            Method and it's input arguments.



---

EXPLAINING REGULAR EXPRESSIONS:

    We decided to explain the following regular expressions:
        1. "Method Declaration" Regex:
            the regex:"\\s*void\\s+([a-zA-Z]\\w*)\\s*\\((\\s*(final)?\\s*" +
                 "(int|double|String|boolean|char)\\s+((_\\w+)|([a-zA-Z]\\w*))\\s*)*\\s*(,(\\s*(final)?\\s*"+
                 "(int|double|String|boolean|char)\\s*((_\\w+)|([a-zA-Z]\\w*)))\\s*)*\\)\\s*\\{\\s*"

            This regex is a complex regex that captures ALL valid forms of Method declarations, i.e. of the
            form "void _//name__ ( //variables ) {".
            We used this Regex in the First Code Check, and if the regex did not capture it - it is for sure
            not a method declaration. In other words, this regex is a "gate gaurd Regex" that does not "let
            in" phrases that do not match this format. We used similar regex statements in our First check -
            in order to to check all other types of possible code lines as well
            (i.e. "if" and "while" statements, variable declarations etc).


        2. "Multi Variable Declaration" Regex:
            The regex: "(final)?\\s*(int|double|String|boolean|char)?\\s+" +
               "(\\w+|[a-zA-Z]\\w*)\\s*\\s*(=\\s*(-?\\d+\\.?\\d*|\\w+|[a-zA-Z]\\w*|'.'|\".*\")\\s*)?\\s*?\\s*"

            This regex was used to capture multiple variables of the same type that were declared in the same
            line. In this regex, much thought was put into the grouping structure of the regex, so that it
            would capture a single variable every time. By using the "m.find()" command we were able to catch
            all variables that were declared.
            This regex was used both in the first check and also in the second check, in order to handle cases
            of multi variable declaration in different scopes (global scope in the first check, other scopes
            in the second check.

