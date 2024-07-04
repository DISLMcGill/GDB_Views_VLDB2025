package jess;

import org.neo4j.kernel.api.impl.index.SearcherReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.beans.*;

public class JessView implements Serializable {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    /*
    * This class is meant to represent a view template within Jess.
    * Intended usage: This class should contain a list of jess.JessPath fact identifiers.
    * Upon deletion of a node or edge, the jess.JessPath fact checks if it still belongs to this view.
    * If not, the jess.JessPath is retracted, and the entry in jess.JessView's list is also removed.
    *
    *
     */

    private String viewRuleName;
    private ArrayList<Integer> pathIDs;

    public ArrayList<Integer> getPathIDs(){
        return pathIDs;
    }
    public String getViewRuleName(){
        return viewRuleName;
    }

    public void removeID(Integer i){
        pathIDs.remove(i);
    }


    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

}
