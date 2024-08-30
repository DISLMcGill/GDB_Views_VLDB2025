package jess;

import java.io.Serializable;
import java.beans.*;
import java.util.ArrayList;

public class JessPath implements Serializable {

    /*
    * This class is meant to store all information along a Path that is subscribed to by a view.
    * In order to keep the path maintained throughout graph changes, the meta-information about the
    * view subscription itself must also be maintained.
    *
    * Information about the graph are assumed to be well defined within the Node and Edge templates.
    * If a Node or Edge is deemed to not belong to the Path anymore, then the fact representing this object
    * must be retracted. Due to the power of logical support, meta-info about conditions need not be attached to
    * any of the nodes, paths, or edges.
    *
    * A view on node sets will contain an empty set of edges, and a set of disjoint nodes.
    *
    * Example:
    * (defrule view-1
    * (logical ?x <- .. ?y <- .. ?z <- ..
    * )
    * =>
    * (assert (jess.JessPath (x) (y) (z) ))
    * )
    * For example, if the jess.JessPath (fact-id 10) which includes fact id 5 was part of "view-1", and it is removed, then
    * then the Integer 10 is removed from pathIDs. To know about the association between fact-id 5 and fact-id 10, a mapping
    * is required. This mapping is also good for when we want to retrieve the views - if we want to use view-1
    * then we find all JessViews corresponding to view-1 and all JessPaths found in the jess.JessView list.
    *
    * The only non-automatic maintenance is related to the updating of the jess.JessView list. When a jess.JessNode is modified
    * or deleted, any JessPaths dependent on it will automatically be retracted, but references to these JessPaths within
    * JessViews may remain.
    * todo nullify these references. they do not null themselves by default
    */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private int factID;

    public JessPath(){
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

    }

    public ArrayList<JessNode> nodes;
    public ArrayList<JessEdge> edges;

    public void addNode(JessNode n){
        nodes.add(n);
    }

    public void addEdge(JessEdge e){
        edges.add(e);
    }

    public ArrayList<JessNode> getNodes(){
        return nodes;
    }

    public ArrayList<JessEdge> getEdges(){
        return edges;
    }


    public int getFactID(){
        return factID;
    }
    public void setFactID(int id){
        int temp = id;
        factID = id;
        pcs.firePropertyChange("factId", temp, factID);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
}
