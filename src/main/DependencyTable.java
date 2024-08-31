package main;

import java.util.Hashtable;
import java.util.List;

public class DependencyTable extends Hashtable<String, TableEntry> {


    //This is the dependency table class which is a hashtable. This class should contain methods for easier interfacing
    //on the table.


    //One dependency table may contain a set of keys and a set of main.TableEntry objects. (values)
    //Each main.TableEntry object contains none or many main.EntryData objects
    //main.EntryData objects are what encapsulate conditions and dependencies


    private List<String> keys;
    private List<TableEntry> values;


    public boolean containsNoEntryData(String key){
        TableEntry result = super.get(key);
        return result.isEmpty();
    }

    public DependencyTable(){
        super();
    }







}
