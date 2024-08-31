package main;

import java.util.List;

public class EntryData {

    public List<String> dependents;
    public List<Condition> conditions;

    //Used for graph UPDATES
    public boolean containsConditionOnAttribute(String attributeName){

        for(Condition c : conditions){
            if(c.attribute.equals(attributeName)) return true;
            //TODO: technically can be more specific. eg view condition is "n.attribute = 1" and the change
            // sets the value to attribute = 3. then it should return false. So far it only checks if the attribute name is the same

        }

        return false;
    }


    public void setDependents(List<String> dependents) {
        this.dependents = dependents;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void addDependent(List<String> deps){
        dependents.addAll(deps);
    }
}
