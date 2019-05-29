package oop.ex6;

import oop.ex6.declared.Variable;
import oop.ex6.exceptions.SjavaException;
import oop.ex6.exceptions.VariableMismatchException;
import oop.ex6.main.JavaParser;

import java.util.Iterator;
import java.util.LinkedList;


public class VariableVerifier {

    private static final boolean FOUND_VARIABLE = true;
    private static final boolean NOT_FOUND_VARIABLE = false;
    private RegexStatements regexStatements;
    private VariablesFactory variablesFactory;

    /**
     * The constructor gets the Variable factory, and the RegexStatements.
     *
     * @param regexStatements  - the Instance of the regexStatements in order to use regex
     * @param variablesFactory - the variables factory.
     */
    public VariableVerifier(RegexStatements regexStatements, VariablesFactory variablesFactory) {
        this.regexStatements = regexStatements;
        this.variablesFactory = variablesFactory;

    }

    /**
     * Function receives a Variable and verifies that there is no other Variable with the same name WITHIN
     * THE SAME SCOPE. This function Handles the case such as the following (within same scope):
     * String a;
     * ...
     * int a;
     *
     * @param variable       Variable to be checked
     * @param scopeCounter   number of variables declared in current scope
     * @param variablesStack Stack of all variables of current and outer scopes
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public void declarationSameScopeVariable(Variable variable, int scopeCounter,
                                             LinkedList<Variable> variablesStack) throws SjavaException {
        Iterator<Variable> variableStackIterator = variablesStack.iterator();

        for (int i = 0; i < scopeCounter; i++) {
            if (variableStackIterator.hasNext()) {
                if (variable.getName().equals(variableStackIterator.next().getName())) {
                    throw new VariableMismatchException(VariableMismatchException.ERROR_VARIABLE_IN_SCOPE);
                }
            }
        }
    }

    /**
     * Function receives a Variable that is assigned to another variable, and verifies that the assignment
     * is legal.
     * The function handles cases such as "int a = b;" .
     * The function verifies two things:
     * 1. Variable name is not assigned before in same scope (i.e. "String a;  ...  int a = b; "  - done by
     * calling
     * declarationSameScopeVariable function.
     * 2. Assigned Variable is of the same type of the declared variable (i.e. if "int a = b;", verify b is
     * an int).
     *
     * @param variable             New Variable.
     * @param assignedVariableName Assigned variable.
     * @param scopeCounter         number of variables in current scope.
     * @param variablesStack       Stack containing all variables.
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public void checkIfAssignVariableExists(Variable variable, String assignedVariableName, int scopeCounter,
                                            LinkedList<Variable> variablesStack) throws SjavaException {
        declarationSameScopeVariable(variable, scopeCounter, variablesStack);
        String variableType = variable.getType();
        if (assignedVariableName.equals(VariablesFactory.TYPE_INT) || assignedVariableName.equals
                (VariablesFactory.TYPE_CHAR) ||
                assignedVariableName.equals(VariablesFactory.TYPE_DOUBLE) ||
                assignedVariableName.equals(VariablesFactory.TYPE_STRING) || assignedVariableName.equals
                (VariablesFactory.TYPE_BOOLEAN)) {
            return;
        }
        for (Variable curVariable : variablesStack) {
            if (curVariable.getName().equals(assignedVariableName)) {
                if (curVariable.getType().equals(variableType) && curVariable.getIsAssigned()) {
                    return;
                }
                else {
                    throw new VariableMismatchException();         // Variable assignment exception
                }
            }
        }
        throw new VariableMismatchException(VariableMismatchException.ERROR_VARIABLE_NOT_INITIALIZE); //
        // Not existing variable to assign
    }

    /**
     * Function receives a non-assigned Variable Name and a Variable Value, and searches for the Value's
     * assignment - then verifies the declaration and value are of the same type.
     * This function handles the case "a=4;" - function will check "a" was assigned and is of "int" type.
     *
     * @param variableName   String of variable name
     * @param variableValue  String of Variable value
     * @param scopeCounter   number of values in current scope
     * @param variablesStack stack of all Variables in current scope and outer scopes
     * @return The expected variable type (i.e. if a was declared as an int - will return "int").
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public String checkVariableType(String variableName, String variableValue, int scopeCounter,
                                    LinkedList<Variable> variablesStack) throws SjavaException {
        String expectedVariableType = RegexStatements.EMPTY_STRING;
        Variable expectedVariable = null;
        boolean foundExpectedVariable = NOT_FOUND_VARIABLE;

        for (Variable curVariable : variablesStack) {

            if (variableName.equals(curVariable.getName())) {
                expectedVariable = curVariable;
                expectedVariableType = curVariable.getType();
                foundExpectedVariable = FOUND_VARIABLE;
                break;
            }
        }
        if (!foundExpectedVariable) {
            throw new VariableMismatchException();     // value not assigned exception
        }
        if (expectedVariable.getIsFinal() && expectedVariable.getIsAssigned()) {
            throw new VariableMismatchException(VariableMismatchException.ERROR_VARIABLE_NOT_ASSIGNED);//
            // IS final exception, assigning to final exception
        }
        Variable tempVar = variablesFactory.buildVariable(expectedVariableType + RegexStatements.SPACE +
                variableName + RegexStatements.EQUALS_SIGN + variableValue + RegexStatements.SEMICOLON,
                JavaParser.NOT_GLOBAL, JavaParser
                .NOT_FROM_DECLARATION);
        if (tempVar.getIsAssignedToVariable()) {
            checkIfAssignVariableExists(tempVar, tempVar.getValue(), scopeCounter, variablesStack);
        }
        return expectedVariableType;
    }


}