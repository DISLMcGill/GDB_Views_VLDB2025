package main;

import java.util.List;

public class GraphChange{

    //Do not remember the purpose of this class. It's not used but it may have been an early draft

    enum changeType{
        DELETE,
        INSERT,
        UPDATE
    }

    //variables that will exist no matter the changeType
    public String fullQuery;
    public List<Condition> conditionList;
    public changeType graphChangeType;

    //deletion-only variables
    public String deletedVarName;
    public String deletedVarLabel;


    //insertion-only variables
    public String insertedQuery;


    //update-only variables
    public String updatedAttribute;
    public String updatedVariableName;


    public changeType getGraphChangeType() {
        return graphChangeType;
    }

    public GraphChange(String query, changeType changetype){

        if(query.contains("SET") || query.contains("REMOVE"))   graphChangeType = changeType.UPDATE;
        if(query.contains("DELETE")) graphChangeType = changeType.DELETE;
        if(query.contains("CREATE")) graphChangeType = changeType.INSERT;

    }



}
