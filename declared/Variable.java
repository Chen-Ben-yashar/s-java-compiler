package oop.ex6.declared;


public class Variable {

    private String type;
    private String name;
    private String value;

    private boolean isGlobal, isFinal, isAssigned, isAssignedToVariable;

    /**
     * Variable constructor.
     *
     * @param type                 String representing the Variable's Type.
     * @param name                 String representing the Variable's Name.
     * @param value                String representing the Variable's Value.
     * @param isAssigned           Boolean representing if the Variable is assigned.
     * @param isAssignedToVariable Boolean representing if Variable is assigned to a variable.
     * @param isFinal              Boolean representing if Variable is final.
     * @param isGlobal             Boolean representing if Variable is global.
     */
    public Variable(String type, String name, String value, boolean isAssigned,
                    boolean isAssignedToVariable, boolean isFinal, boolean isGlobal) {
        this.type = type;
        this.isAssigned = isAssigned;
        this.isFinal = isFinal;
        this.isGlobal = isGlobal;
        this.name = name;
        this.value = value;
        this.isAssignedToVariable = isAssignedToVariable;
    }

    /**
     * @return Variable's Name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Variable's Value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return Is Variable assigned to another variable.
     */
    public boolean getIsAssignedToVariable() {
        return isAssignedToVariable;
    }

    /**
     * @return Is Variable final.
     */
    public boolean getIsFinal() {
        return isFinal;
    }

    /**
     * @return Is Variable assigned.
     */
    public boolean getIsAssigned() {
        return isAssigned;
    }

    /**
     * @return Variable's Type.
     */
    public String getType() {
        return type;
    }

    /**
     * Function changes Variable's state to "Assigned".
     */
    public void setAssigned() {

        this.isAssigned = true;
    }

    /**
     * set Variable's value to new value.
     *
     * @param value New value to be assigned.
     */
    public void setValue(String value) {
        this.value = value;
    }

}
