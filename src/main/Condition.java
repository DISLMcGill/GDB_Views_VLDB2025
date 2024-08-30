package main;

public class Condition {



    public String attribute;
    public String conditionString;

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }

    public String getConditionString() {
        return conditionString;
    }

    public void printCondition(){
        System.out.println(conditionString);
    }

    @Override
    public boolean equals(Object c){
        if (! (c instanceof Condition)) return false;
        if((((Condition) c).conditionString).equals(conditionString)) return true;
        return false;

    }

    @Override
    public int hashCode(){
        return conditionString.hashCode() + attribute.hashCode();
    }

    public String toString(){
        return conditionString;
    }


}
