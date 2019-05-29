package oop.ex6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexStatements {

    public static final int GROUP_ZERO = 0;
    public static final int GROUP_ONE = 1;
    public static final int GROUP_TWO = 2;
    public static final int GROUP_THREE = 3;
    public static final int GROUP_FOUR = 4;
    public static final int GROUP_FIVE = 5;

    public static final String STRING_MARK = "\"";
    public static final String PERIOD = ".";
    public static final String CHAR_MARK = "\'";
    public static final String BOOLEAN_CHECK_REGEX = "(\\s*true\\s*|\\s*false\\s*|\\s*-?\\d+\\.?\\d*\\s*)";
    public static final String DOUBLE_CHECK_REGEX = "-?\\d+(\\.\\d+)?";

    public static final String EMPTY_STRING = "";

    public static final String EQUALS_SIGN = " = ";

    public static final String SEMICOLON = ";";
    public static final String SPACE = " ";
    public final String EMPTY_LINE = "\\s*";
    public final String COMMENT_LINE = "//.*";
    public final String CODE_LINE = ".*(;|\\{|\\})\\s*$";
    public final String NAME = "((_\\w+)|([a-zA-Z]\\w*))";
    public final String VALUE = "(-?\\d+\\.?\\d*|((_\\w+)|([a-zA-Z]\\w*))|('.')|(\".*\"))";
    public final String CONDITION = "(\\s*(-?\\d+\\.?\\d*|(_\\w+)|([a-zA-Z]\\w*))\\s*" +
            "(\\|\\||&&))*\\s*(-?\\d+\\.?\\d*|(_\\w+)|([a-zA-Z]\\w*))\\s*";

    public final String IF_STATEMENT = "\\s*if\\s*\\(\\s*(\\s*(-?\\d+\\.?\\d*|(_\\w+)|([a-zA-Z]\\w*))\\s*" +
            "(\\|\\||&&))*\\s*(-?\\d+\\.?\\d*|(_\\w+)|([a-zA-Z]\\w*))\\s*\\s*\\)\\s*\\{\\s*";
    public final String IF_WHILE_STATEMENT_PARAMETERS = "(\\(|\\|\\||\\&\\&)\\s*([a-zA-Z]+[a-zA-Z0-9]*)";

    public final String WHILE_STATEMENT = "\\s*while\\s*\\(\\s*(\\s*(-?\\d+\\.?\\d*|(_\\w+)|([a-zA-Z]\\w*))" +
            "\\s*(\\|\\||&&))*\\s*(-?\\d+\\.?\\d*|(_\\w+)|([a-zA-Z]\\w*))\\s*\\s*\\)\\s*\\{\\s*";

    public final String CALL_METHOD = "\\s*([a-zA-Z]\\w*)\\s*" +
            "\\((\\s*(-?\\d+\\.?\\d*|((_\\w+)|([a-zA-Z]\\w*))|('.')|(\".*\"))\\s*)?" +
            "(,\\s*(-?\\d+\\.?\\d*|((_\\w+)|([a-zA-Z]\\w*))|('.')|(\".*\"))\\s*)*\\)\\s*;\\s*";

    public final String CALL_METHOD_PARAMETERS = "(\\(|\\,)\\s*([a-zA-Z0-9_\"\']+)";

    public final String CLOSING_LINE = "\\s*}\\s*";
    public final String RETURN_LINE = "\\s*return;\\s*";

    public final String DECLARE_METHOD = "\\s*void\\s+([a-zA-Z]\\w*)\\s*\\((\\s*(final)?\\s*" +
            "(int|double|String|boolean|char)\\s+((_\\w+)|([a-zA-Z]\\w*))\\s*)*\\s*(,(\\s*(final)?\\s*" +
            "(int|double|String|boolean|char)\\s*((_\\w+)|([a-zA-Z]\\w*)))\\s*)*\\)\\s*\\{\\s*";

    public final String VARIABLE_AND_VALUE = "(\\w+|[a-zA-Z]\\w*)\\s*=\\s*(-?\\d+\\.?\\d*" +
            "|\\w+|[a-zA-Z]\\w*|'.'|\\\".\\\")\\s*;\\s*";


    public final String VARIABLE_DECLARE = "((\\s*final\\s)?\\s*(int|double|String|boolean|char)\\s)\\s*(" +
            "(_\\w+)|([a-zA-Z]\\w*))\\s*(=\\s*(-?\\d+\\.?\\d*|((_\\w+)|([a-zA-Z]\\w*))|('.')|(\".*\"))\\s*)" +
            "?" +
            "(,\\s*((_\\w+)|([a-zA-Z]\\w*))\\s*(=\\s*(-?\\d+\\.?\\d*|((_\\w+)|([a-zA-Z]\\w*))|('.')|(\"" +
            ".*\"))" +
            "\\s*)?)*\\s*;\\s*";

    public final String SIMPLE_TYPE_AND_VARIABLE = "(final)?\\s*(int|double|String|boolean|char)\\s+" +
            "(\\w+|[a-zA-Z]\\w*)\\s*\\s*(=\\s*(-?\\d+\\.?\\d*|\\w+|[a-zA-Z]\\w*|'.'|\".*\")\\s*)?\\s*;?\\s*";

    public final String MULTI_VARIABLE_DECLARATION = "(final)?\\s*(int|double|String|boolean|char)?\\s+" +
            "(\\w+|[a-zA-Z]\\w*)\\s*\\s*(=\\s*(-?\\d+\\.?\\d*|\\w+|[a-zA-Z]\\w*|'.'|\".*\")\\s*)?\\s*?\\s*";


    private Pattern emptyLine = Pattern.compile(EMPTY_LINE);
    private Pattern commentLine = Pattern.compile(COMMENT_LINE);
    private Pattern codeLine = Pattern.compile(CODE_LINE);
    private Pattern name = Pattern.compile(NAME);
    private Pattern value = Pattern.compile(VALUE);
    private Pattern condition = Pattern.compile(CONDITION);
    private Pattern ifStatement = Pattern.compile(IF_STATEMENT);
    private Pattern whileStatement = Pattern.compile(WHILE_STATEMENT);
    private Pattern callMethod = Pattern.compile(CALL_METHOD);
    private Pattern closingLine = Pattern.compile(CLOSING_LINE);
    private Pattern returnLine = Pattern.compile(RETURN_LINE);
    private Pattern declareMethod = Pattern.compile(DECLARE_METHOD);
    private Pattern declareVariable = Pattern.compile(VARIABLE_DECLARE);
    private Pattern variableAndValue = Pattern.compile(VARIABLE_AND_VALUE);


    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Empty Line regex phrase.
     */
    public Boolean checkEmptyLine(String line) {
        Matcher m = emptyLine.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Variable And Value regex phrase (i.e. a phrase like "a = 5;)" .
     */
    public boolean checkVariableAndValue(String line) {
        Matcher m = variableAndValue.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Comment Line regex phrase (i.e. starts with \\... ) .
     */
    public Boolean checkCommentLine(String line) {
        Matcher m = commentLine.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Code Line regex phrase (i.e. ends with one of {, }, or ; ) .
     */
    public Boolean checkCodeLine(String line) {
        Matcher m = codeLine.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Name regex phrase (i.e. the variable's name, such as "aaa" or
     * "_b5" " .
     */
    public Boolean checkName(String line) {
        Matcher m = name.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Value regex phrase (i.e. one of the following: a number such
     * as -25.56, or
     * a string such as "aaa", or a char such as 'a' or another variable such as "k") .
     */
    public Boolean checkValue(String line) {
        Matcher m = value.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Condition regex phrase (i.e. is a valid condition statement,
     * such as (4) or
     * (true) or (a) ) .
     */
    public Boolean checkCondition(String line) {
        Matcher m = condition.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line is a statement matching the Statement regex phrase (i.e. is of the form
     * "if (//statement) { " ) .
     */
    public Boolean checkIfStatement(String line) {
        Matcher m = ifStatement.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the While Statement regex phrase (i.e. is of the form "while
     * (//statement) {" ).
     */
    public Boolean checkWhileStatement(String line) {
        Matcher m = whileStatement.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Closing Line regex phrase (i.e. "}"  ) .
     */
    public Boolean checkClosingLine(String line) {
        Matcher m = closingLine.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches a Method Call regex phrase (i.e. of the form: "foo (a, b) "  ).
     */
    public Boolean checkCallMethod(String line) {
        Matcher m = callMethod.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line matches the Return regex phrase (i.e. "return;" ).
     */
    public Boolean checkReturnLine(String line) {
        Matcher m = returnLine.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line declares a new Method (i.e. of the form " void foo (int a, String b) { " ) .
     */
    public Boolean checkDeclareMethod(String line) {
        Matcher m = declareMethod.matcher(line);
        return m.matches();
    }

    /**
     * @param line current code line to be examined.
     * @return true iff the Line declares a new Variable (i.e. of the form "final int a = 5;" ) .
     */
    public Boolean checkDeclareVariable(String line) {
        Matcher m = declareVariable.matcher(line);
        return m.matches();
    }


}
