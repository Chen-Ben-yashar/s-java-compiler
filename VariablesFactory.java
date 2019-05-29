package oop.ex6;

import oop.ex6.declared.Method;
import oop.ex6.declared.Variable;
import oop.ex6.exceptions.SjavaException;
import oop.ex6.exceptions.VariableMismatchException;
import oop.ex6.main.JavaParser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariablesFactory {
    public static final boolean IS_FINAL = true;
    public static final boolean NOT_FINAL = false;
    public static final String TYPE_VARIABLE = "variable";
    public static final String TYPE_STRING = "String";
    public static final String TYPE_CHAR = "char";
    public static final String TYPE_DOUBLE = "double";
    public static final String TYPE_BOOLEAN = "boolean";
    public static final String TYPE_INT = "int";

    private RegexStatements regexStatements;

    /**
     * The constructor gets the RegexStatements
     *
     * @param regexStatements - the Instance of the regexStatements in order to use regex
     */
    public VariablesFactory(RegexStatements regexStatements) {
        this.regexStatements = regexStatements;
    }


    /**
     * Receives a code line that contains a declaration of a new variable, and returns a matching Variable
     * object.
     * Operates a case such as "final int a = (//something);"
     *
     * @param variableLine            code line containing a variable declaration.
     * @param isGlobal                true iff the variable is global.
     * @param isFromMethodDeclaration true iff the variable is declared within a method declaration. This
     *                                is used for
     *                                creating objects declared during method creation.
     * @return a new Variable object with the variable's data.
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public Variable buildVariable(String variableLine, boolean isGlobal, boolean isFromMethodDeclaration)
            throws SjavaException {
        boolean isFinal = NOT_FINAL;
        boolean isAssigned = JavaParser.NOT_ASSIGN;
        String name;
        String type;
        String value = null;
        boolean isAssignedToVariable = JavaParser.NOT_ASSIGN;
        LinkedList<Method> VariablesList = new LinkedList<Method>();
        Pattern declareVariable = Pattern.compile(regexStatements.SIMPLE_TYPE_AND_VARIABLE);
        Matcher m = declareVariable.matcher(variableLine);
        if (!m.matches()) {
            throw new VariableMismatchException();
        }

        if (m.group(RegexStatements.GROUP_ONE) != null) {       // group 1 representing "final"
            isFinal = IS_FINAL;

            if (m.group(RegexStatements.GROUP_FOUR) == null && !isFromMethodDeclaration) {
                throw new VariableMismatchException(VariableMismatchException.ERROR_VARIABLE_NOT_INITIALIZE);
            }
        }
        type = m.group(RegexStatements.GROUP_TWO);              // group 2 representing the Variable's type
        name = m.group(RegexStatements.GROUP_THREE);              // group 3 representing the Variable's name
        value = m.group(RegexStatements.GROUP_FIVE);
        if (isFromMethodDeclaration) {      // if from method declaration - it is assigned by default
            isAssigned = JavaParser.IS_ASSIGN;
            return new Variable(type, name, value, isAssigned, JavaParser.NOT_ASSIGN, isFinal, isGlobal);
        }
        if (m.group(RegexStatements.GROUP_FOUR) != null) {   // value assignment check := group(4)
            isAssigned = JavaParser.IS_ASSIGN;
            String valueType = getValueType(value);
            checkCompatibilityTypeAndValue(valueType, type);

            if (valueType.equals(TYPE_VARIABLE)) {
                isAssignedToVariable = JavaParser.IS_ASSIGN;
            }
            else {
                value = type;
            }
        }
        return new Variable(type, name, value, isAssigned, isAssignedToVariable, isFinal, isGlobal);
    }

    /**
     * Function determines the type of a given variable's value.
     *
     * @param value String representing the Variable's value.
     * @return Variable's value type.
     */
    public String getValueType(String value) {
        String valueType;
        if (value.startsWith(RegexStatements.STRING_MARK) && value.endsWith(RegexStatements.STRING_MARK)) {
            valueType = TYPE_STRING;
        }
        else if (value.startsWith(RegexStatements.CHAR_MARK) && value.endsWith(RegexStatements.CHAR_MARK)) {
            valueType = TYPE_CHAR;
        }
        else if (value.matches(RegexStatements.DOUBLE_CHECK_REGEX)) {
            if (value.contains(RegexStatements.PERIOD)) {
                valueType = TYPE_DOUBLE;
            }
            else {
                valueType = TYPE_INT;
            }
        }
        else if (value.matches(RegexStatements.BOOLEAN_CHECK_REGEX)) {
            valueType = TYPE_BOOLEAN;
        }
        else {
            valueType = TYPE_VARIABLE;
            // value is a variable, we will return the variable name as the value.
        }
        return valueType;
    }

    /**
     * Function verifies a given Variable, and checks that the DECLARED value matches the ASSIGNED value.
     *
     * @param valueType Variable's value type
     * @param type      Variable's declared type
     * @throws SjavaException declared type does not match assigned value type.
     */
    protected void checkCompatibilityTypeAndValue(String valueType, String type) throws SjavaException {
        if (valueType.equals(TYPE_VARIABLE)) {
            return;

        }
        // mismatch between type and value  (i.e. int a = "hi")
        if ((type.equals(TYPE_INT) && !valueType.equals(TYPE_INT)) ||
                (type.equals(TYPE_DOUBLE) && valueType.equals(TYPE_STRING)) ||
                (type.equals(TYPE_DOUBLE) && valueType.equals(TYPE_CHAR)) ||
                (type.equals(TYPE_DOUBLE) && valueType.equals(TYPE_BOOLEAN)) ||
                (type.equals(TYPE_STRING) && !valueType.equals(TYPE_STRING)) ||
                (type.equals(TYPE_CHAR) && !valueType.equals(TYPE_CHAR)) ||
                (type.equals(TYPE_BOOLEAN) && (valueType.equals(TYPE_STRING))) ||
                (type.equals(TYPE_BOOLEAN) && valueType.equals(TYPE_CHAR))) {


            throw new VariableMismatchException();     // type mismatch
        }

    }

    /**
     * function receives a code line of the form "int a = 5;" and returns the Variable Name.
     *
     * @param currentLine code line to be examined
     * @return the Variable Name, or null value doesn't exist.
     */
    public String getVariableName(String currentLine) {
        Pattern declareVariable = Pattern.compile(regexStatements.VARIABLE_AND_VALUE);
        Matcher m = declareVariable.matcher(currentLine);
        if (m.find()) {
            return m.group(RegexStatements.GROUP_ONE);
        }
        else {
            return null;
        }
    }

    /**
     * function receives a code line of the form "int a = 5;" and returns the Variable Value.
     *
     * @param currentLine code line to be examined
     * @return the Variable Value, or null value doesn't exist.
     */
    public String getVariableValue(String currentLine) {
        Pattern declareVariable = Pattern.compile(regexStatements.VARIABLE_AND_VALUE);
        Matcher m = declareVariable.matcher(currentLine);
        if (m.find()) {
            return m.group(RegexStatements.GROUP_TWO);
        }
        else {
            return null;
        }

    }

}
