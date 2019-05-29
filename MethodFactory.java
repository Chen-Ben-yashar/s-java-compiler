package oop.ex6;

import oop.ex6.declared.Method;
import oop.ex6.declared.Variable;
import oop.ex6.exceptions.SjavaException;
import oop.ex6.main.JavaParser;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MethodFactory {


    RegexStatements regexStatements;
    VariablesFactory variablesFactory;

    /**
     * The constructor gets the Variable factory, and the RegexStatements.
     *
     * @param regexStatements  - the Instance of the regexStatements in order to use regex
     *                         to parse the method declaration line.
     * @param variablesFactory - the variables factory.
     */
    public MethodFactory(RegexStatements regexStatements, VariablesFactory variablesFactory) {
        this.regexStatements = regexStatements;
        this.variablesFactory = variablesFactory;
    }

    /**
     * Receives a code line that contains a declaration of a new Method,
     * and returns a matching Method object.
     *
     * @param codeLine code line containing a Method declaration.
     * @return a new Method object with the Method's data.
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public Method buildMethod(String codeLine) throws SjavaException {
        LinkedList<Variable> variableList = new LinkedList<Variable>();
        String methodName;
        Pattern methodDeclaration = Pattern.compile(regexStatements.DECLARE_METHOD);
        Matcher m = methodDeclaration.matcher(codeLine);
        m.matches();

        methodName = m.group(RegexStatements.GROUP_ONE);
        Pattern declareVariable = Pattern.compile(regexStatements.SIMPLE_TYPE_AND_VARIABLE);
        m = declareVariable.matcher(codeLine);
        while (m.find()) {
            String tempVar = m.group(RegexStatements.GROUP_ZERO);
            tempVar = tempVar.trim();
            variableList.add(variablesFactory.buildVariable(
                    tempVar, JavaParser.IS_GLOBAL, JavaParser.IS_FROM_DECLARATION));
        }

        Variable[] variableArray = variableList.toArray(new Variable[variableList.size()]);
        return new Method(methodName, variableArray);
    }

    /**
     * Function creates a list containing the Method's name and the Method's parameters.
     *
     * @param currentLine code line containing a Method declaration.
     * @return list of Method's name and parameters.
     */
    public LinkedList<String> parseCallMethodNameAndParameters(String currentLine) {
        LinkedList<String> methodNameAndParameters = new LinkedList<String>();
        Pattern methodDeclaration = Pattern.compile(regexStatements.CALL_METHOD);
        Matcher m = methodDeclaration.matcher(currentLine);
        m.matches();
        methodNameAndParameters.add(m.group(RegexStatements.GROUP_ONE));
        Pattern declareVariable = Pattern.compile(regexStatements.CALL_METHOD_PARAMETERS);
        m = declareVariable.matcher(currentLine);
        while (m.find()) {
            methodNameAndParameters.add(m.group(RegexStatements.GROUP_TWO));
        }
        return methodNameAndParameters;
    }


    /**
     * receives a condition line, and handles the case of a complex condition line (AND,OR operators).
     *
     * @param currentLine a condition line.
     * @return list of all parsed conditions.
     */
    public LinkedList<String> parseConditionBlockParameters(String currentLine) {
        LinkedList<String> parametersList = new LinkedList<String>();

        Pattern methodDeclaration = Pattern.compile(regexStatements.IF_WHILE_STATEMENT_PARAMETERS);
        Matcher m = methodDeclaration.matcher(currentLine);
        m.matches();
        while (m.find()) {
            parametersList.add(m.group(RegexStatements.GROUP_TWO));
        }
        return parametersList;
    }


}
