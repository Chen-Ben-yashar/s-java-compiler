package oop.ex6.declared;

public class Method {
    private String methodName;
    private Variable[] declaredVariableArray;

    /**
     * Method constructor.
     *
     * @param methodName            String representing the Method Name
     * @param declaredVariableArray List of declared variables in Method Declaration (i.e. "void foo(INT A,
     *                              INT B){").
     */
    public Method(String methodName, Variable[] declaredVariableArray) {
        this.methodName = methodName;
        this.declaredVariableArray = declaredVariableArray;
    }

    /**
     * @return Method's Name.
     */
    public String getName() {
        return methodName;
    }

    /**
     * @return Method's declared variables array.
     */
    public Variable[] getVariable() {
        return declaredVariableArray;
    }

}
