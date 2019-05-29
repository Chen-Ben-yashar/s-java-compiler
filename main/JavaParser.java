package oop.ex6.main;

import oop.ex6.MethodFactory;
import oop.ex6.RegexStatements;
import oop.ex6.VariableVerifier;
import oop.ex6.VariablesFactory;
import oop.ex6.declared.Method;
import oop.ex6.declared.Variable;
import oop.ex6.exceptions.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaParser {

    public final static boolean FLAG_ON = true;
    public final static boolean FLAG_OFF = false;
    public final static boolean IS_GLOBAL = true;
    public final static boolean IS_ASSIGN = true;
    public final static boolean NOT_ASSIGN = false;
    public final static boolean IS_FROM_DECLARATION = true;
    public final static boolean NOT_FROM_DECLARATION = false;
    public final static boolean NOT_GLOBAL = false;

    private final int METHOD_SCOPE_BALANCE = 1;
    private final int GLOBAL_SCOPE_BALANCE = 0;
    private final int INITIALIZE_COUNTER = 0;
    private final int HEAD_INDEX = 0;
    private final int COUNT1 = 1;

    private String[] javaLinesArray;
    private RegexStatements regexCondition = new RegexStatements();
    private LinkedList<Variable> variableStack;
    private LinkedList<Method> methodStack;
    private MethodFactory methodFactory;
    private VariablesFactory variablesFactory;
    private VariableVerifier variableVerifier;
    private int globalVariablesCounter;


    /**
     * This constructor of the parser initialize the Stacks of Variables,Methods,
     * Constructing the needed Factories and the VariableVerifier classes.
     * To parse the Sjava code.
     *
     * @param javaLinesArray the code lines of the Java code
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public JavaParser(String[] javaLinesArray) throws SjavaException {

        this.javaLinesArray = javaLinesArray;
        variableStack = new LinkedList<Variable>();
        methodStack = new LinkedList<Method>();
        variablesFactory = new VariablesFactory(regexCondition);
        methodFactory = new MethodFactory(regexCondition, variablesFactory);
        variableVerifier = new VariableVerifier(regexCondition, variablesFactory);

    }

    /**
     * This method do the CodeCheck.
     *
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    public void javaCodeCheck() throws SjavaException {
        firstCodeCheck();
        secondCodeCheck();
    }

    /**
     * This is the first check, it will check the code for illegal code lines,
     * brackets structure, verify the global variables, and the declared functions.
     *
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    private void firstCodeCheck() throws SjavaException {
        globalVariablesCounter = INITIALIZE_COUNTER;
        int curlBracketBalance = GLOBAL_SCOPE_BALANCE;
        boolean methodCloseFlag = FLAG_OFF;
        boolean inFunction = FLAG_OFF;

        for (String currentLine : javaLinesArray) {
            if (methodCloseFlag && !regexCondition.checkClosingLine(currentLine)) {
                methodCloseFlag = FLAG_OFF;
            }
            else if (!regexCondition.checkCodeLine(currentLine)) {
                throw new CodeLineException();
            }
            else if (curlBracketBalance < GLOBAL_SCOPE_BALANCE) { // Global Scope is the lowest scope rank.
                throw new InvalidCodeStructureException();
            }
            else if (regexCondition.checkReturnLine(currentLine)) {
                if (curlBracketBalance == METHOD_SCOPE_BALANCE) {
                    methodCloseFlag = FLAG_ON;
                }
            }
            else if (regexCondition.checkIfStatement(currentLine) || regexCondition.checkWhileStatement
                    (currentLine)) {
                curlBracketBalance++;
            }
            else if (regexCondition.checkClosingLine(currentLine)) {
                if ((curlBracketBalance == METHOD_SCOPE_BALANCE) && (!methodCloseFlag) && (inFunction)) {
                    throw new InvalidCodeStructureException();
                }
                if ((curlBracketBalance == METHOD_SCOPE_BALANCE) && methodCloseFlag) {
                    inFunction = FLAG_OFF;
                    curlBracketBalance--;
                    methodCloseFlag = FLAG_OFF;
                    continue;
                }
                curlBracketBalance--;
            }
            else if (regexCondition.checkDeclareVariable(currentLine)) {
                if (curlBracketBalance != GLOBAL_SCOPE_BALANCE) {
                    continue;
                }
                globalVariablesCounter++;
                addDeclaredVariable(currentLine, IS_GLOBAL);

            }


            // a = b;
            else if (regexCondition.checkVariableAndValue(currentLine)) {
                if (curlBracketBalance == GLOBAL_SCOPE_BALANCE) {
                    addUndeclaredVariableValueReturnType(currentLine);
                }
            }
            else if (regexCondition.checkDeclareMethod(currentLine)) {

                if (curlBracketBalance == GLOBAL_SCOPE_BALANCE) {
                    curlBracketBalance++;
                    inFunction = FLAG_ON;
                    Method tempMethod = methodFactory.buildMethod(currentLine);
                    if (findMethodInStack(tempMethod.getName()) != null) {
                        throw new InvalidMethodException(InvalidMethodException.
                                METHOD_DECLARED_ALREADY_MESSAGE);
                    }
                    methodStack.add(tempMethod);
                }
                else {
                    throw new InvalidMethodException(InvalidMethodException
                            .METHOD_DECLARED_INSIDE_SCOPE_MESSAGE);
                }
            }
            else if (regexCondition.checkCallMethod(currentLine)) {
                continue;
            }
            else {
                throw new InvalidCodeStructureException();
            }
        }
        if (curlBracketBalance != GLOBAL_SCOPE_BALANCE) {
            throw new InvalidCodeStructureException(
                    InvalidCodeStructureException.OPEN_CLOSE_BRACKETS_NOT_EVEN);
        }
    }

    /**
     * This method handle when we assign a value to a variable that been declared,
     * previously ( a = 4 ) .
     * Verifying if the assignment is legal in all aspects (Correct type, declared before,
     * not final, in the same scope).
     *
     * @param currentLine - the current java code.
     * @return - A new variable that been assigned.
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    private Variable addUndeclaredVariableValueReturnType(String currentLine) throws
            SjavaException {
        variableVerifier.checkVariableType(variablesFactory.getVariableName(currentLine),
                variablesFactory.getVariableValue(currentLine), variableStack.size(), variableStack);
        Variable curVariable = findVariableInStack(variablesFactory.getVariableName(currentLine),
                variableStack.size());
        return curVariable;
    }

    /**
     * This method gets a declaration code line (int a = 4) and checks
     * if it correctly been declared and the value of assignment is correct.
     *
     * @param currentLine - the current java code.
     * @param isGlobal    -  Variable is global flag
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    private void addDeclaredVariable(String currentLine, boolean isGlobal) throws SjavaException {
        Pattern declareVariable = Pattern.compile(regexCondition.MULTI_VARIABLE_DECLARATION);
        Matcher m = declareVariable.matcher(currentLine);
        String prefix = "";

        boolean initFind = FLAG_ON;
        while (m.find()) {
            Variable tempVar;
            if (!initFind) {

                tempVar = variablesFactory.buildVariable
                        (prefix + m.group(RegexStatements.GROUP_ZERO) + RegexStatements.SEMICOLON,
                                isGlobal, IS_FROM_DECLARATION);
            }
            else {
                if (m.group(RegexStatements.GROUP_ONE) != null) {
                    prefix += m.group(RegexStatements.GROUP_ONE);
                }
                prefix += m.group(RegexStatements.GROUP_TWO);
                tempVar = variablesFactory.buildVariable
                        (m.group(RegexStatements.GROUP_ZERO) + RegexStatements.SEMICOLON, isGlobal,
                                NOT_FROM_DECLARATION);
                initFind = FLAG_OFF;
            }

            if (tempVar.getIsAssignedToVariable()) {

                variableVerifier.checkIfAssignVariableExists(tempVar, tempVar.getValue()
                        , variableStack.size(), variableStack);
            }
            if (tempVar.getIsAssigned() && !tempVar.getIsAssignedToVariable()) {
                variableVerifier.declarationSameScopeVariable(tempVar, variableStack.size(), variableStack);
            }

            variableStack.add(HEAD_INDEX, tempVar);

        }


    }

    /**
     * The second check of the code will verify the code when it is inside scopes
     * of functions, if, while.
     * It will check if the call of the method is legal.
     * The If,While statements are correct,
     * The assignments of variables inside the scopes.
     *
     * @throws SjavaException Throws the appropriate SjavaException exception.
     */
    private void secondCodeCheck() throws SjavaException {
        int curlBracketBalance = GLOBAL_SCOPE_BALANCE;
        LinkedList<Integer> scopeVariableCounter = new LinkedList<Integer>();
        for (String currentLine : javaLinesArray) {

            if (regexCondition.checkIfStatement(currentLine) || regexCondition.checkWhileStatement
                    (currentLine)) {
                curlBracketBalance++;
                LinkedList<String> conditionBlockParameters = methodFactory.parseConditionBlockParameters
                        (currentLine);
                for (String parameter : conditionBlockParameters) {
                    String parameterType = variablesFactory.getValueType(parameter);
                    if (!parameterType.equals(VariablesFactory.TYPE_VARIABLE)) {
                        if (parameterType.equals(VariablesFactory.TYPE_STRING) || parameterType.equals
                                (VariablesFactory.TYPE_CHAR)) {
                            throw new InvalidIfOrWhileException(
                                    InvalidIfOrWhileException.STRING_OR_CHAR_IN_IF_WHILE);
                        }

                    }
                    else {
                        Variable parameterVariable = findVariableInStack(parameter, variableStack.size());
                        if (parameterVariable == null) {
                            throw new VariableMismatchException(VariableMismatchException
                                    .UNDECLARED_VARIABLE);
                        }
                        if (parameterVariable.getType().equals(VariablesFactory.TYPE_STRING) ||
                                parameterVariable.getType().equals(VariablesFactory.TYPE_CHAR)) {
                            throw new InvalidIfOrWhileException(InvalidIfOrWhileException
                                    .STRING_OR_CHAR_IN_IF_WHILE);
                        }
                        if (!parameterVariable.getIsAssigned()) {
                            throw new VariableMismatchException(VariableMismatchException
                                    .ERROR_VARIABLE_NOT_ASSIGNED);
                        }
                    }


                }


                scopeVariableCounter.add(HEAD_INDEX, INITIALIZE_COUNTER);
            }
            else if (regexCondition.checkDeclareMethod(currentLine)) {

                scopeVariableCounter.add(HEAD_INDEX, INITIALIZE_COUNTER);
                Method tempMethod = methodFactory.buildMethod(currentLine);
                tempMethod = findMethodInStack(tempMethod.getName());
                Variable[] methodVariableArray = tempMethod.getVariable();
                for (Variable methodVariable : methodVariableArray) {
                    variableVerifier.declarationSameScopeVariable(methodVariable, variableStack.size() -
                            globalVariablesCounter, variableStack);
                    variableStack.add(HEAD_INDEX, new Variable(methodVariable.getType(),
                            methodVariable.getName(), methodVariable.getType(),
                            IS_ASSIGN, NOT_ASSIGN, methodVariable.getIsFinal(),
                            NOT_GLOBAL));
                    int count = scopeVariableCounter.pop();
                    scopeVariableCounter.add(HEAD_INDEX, count + COUNT1);

                }
                curlBracketBalance++;

            }
            else if (regexCondition.checkClosingLine(currentLine)) {
                curlBracketBalance--;
                int variablesToDelete = scopeVariableCounter.pop();
                for (int i = 0; i < variablesToDelete; i++) {
                    variableStack.pop();
                }

            }
            else if (regexCondition.checkDeclareVariable(currentLine)) {
                if (curlBracketBalance > GLOBAL_SCOPE_BALANCE) {
                    Variable tempVar = variablesFactory.buildVariable
                            (currentLine, NOT_GLOBAL, NOT_FROM_DECLARATION);
                    if (findVariableInStack(tempVar.getName(), scopeVariableCounter.getFirst()) != null) {
                        throw new VariableMismatchException();
                    }
                    addDeclaredVariable(currentLine, NOT_GLOBAL);

                    int count = scopeVariableCounter.pop();
                    scopeVariableCounter.add(HEAD_INDEX, count + COUNT1);


                }
            }
            else if (regexCondition.checkVariableAndValue(currentLine)) {
                if (curlBracketBalance > GLOBAL_SCOPE_BALANCE) {
                    Variable declaredVariable = addUndeclaredVariableValueReturnType(currentLine);
                    Variable tempVariable = variablesFactory.buildVariable(
                            declaredVariable.getType() + RegexStatements.SPACE + currentLine, NOT_GLOBAL,
                            NOT_FROM_DECLARATION);

                    if (findVariableInStack(declaredVariable.getName(), scopeVariableCounter.getFirst()) ==
                            null) {
                        variableStack.add(HEAD_INDEX, tempVariable);
                        int count = scopeVariableCounter.pop();
                        scopeVariableCounter.add(HEAD_INDEX, count + COUNT1);

                    }
                    else {
                        declaredVariable.setAssigned();
                        declaredVariable.setValue(tempVariable.getValue());
                    }


                }
            }

            else if (regexCondition.checkCallMethod(currentLine)) {
                LinkedList<String> calledMethodAndParameters = methodFactory
                        .parseCallMethodNameAndParameters(currentLine);
                String methodName = calledMethodAndParameters.pop();
                Method declaredMethod = findMethodInStack(methodName);

                if (declaredMethod == null) {
                    throw new InvalidMethodCallException(InvalidMethodCallException.UNDECLARED_METHOD_ERROR);
                }
                Variable[] declaredMethodVariableArray = declaredMethod.getVariable();
                if (declaredMethodVariableArray.length != calledMethodAndParameters.size()) {
                    throw new InvalidMethodCallException(InvalidMethodCallException
                            .CALL_METHOD_PARAMETERS_ERROR); // call method parameters not the same size as
                    // declared.
                }
                Iterator<String> calledMethodParametersIterator = calledMethodAndParameters.iterator();
                for (int i = 0; i < calledMethodAndParameters.size(); i++) {
                    String curCallMethodParameterValue = calledMethodParametersIterator.next();
                    String curCallMethodParameterType = variablesFactory.getValueType
                            (curCallMethodParameterValue);
                    String declaredMethodParameterType = declaredMethodVariableArray[i].getType();
                    if (!curCallMethodParameterType.equals(VariablesFactory.TYPE_VARIABLE)) {
                        if (!declaredMethodParameterType.equals(curCallMethodParameterType)) {
                            throw new VariableMismatchException();
                        }

                    }
                    else {
                        Variable curCalledMethodParameterVariable =
                                findVariableInStack(curCallMethodParameterValue, variableStack.size());
                        if (curCalledMethodParameterVariable == null) {
                            throw new VariableMismatchException(VariableMismatchException
                                    .UNDECLARED_VARIABLE); // variable not exist
                        }
                        if (!curCalledMethodParameterVariable.getType().equals(declaredMethodParameterType)) {
                            throw new VariableMismatchException(); // bad variable type
                        }
                    }


                }

            }

        }

    }

    private Method findMethodInStack(String methodName) {
        for (Method curMethod : methodStack) {
            if (curMethod.getName().equals(methodName)) {
                return curMethod;
            }
        }
        return null;
    }

    /**
     * This method finds a variable by his declaration name inside the
     * Variable Stack.
     *
     * @param variable     - the variable name.
     * @param scopeCounter - the number of elements inside the scope we want to check.
     * @return The variable is found, null if not found.
     */
    private Variable findVariableInStack(String variable, int scopeCounter) {
        Iterator<Variable> iterStackVariable = variableStack.iterator();
        for (int i = 0; i < scopeCounter; i++) {
            if (iterStackVariable.hasNext()) {
                Variable tempVariable = iterStackVariable.next();
                if (tempVariable.getName().equals(variable)) {
                    return tempVariable;
                }
            }
        }
        return null;
    }


}
