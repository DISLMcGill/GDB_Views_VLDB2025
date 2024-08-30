package jess;

import java.io.Serializable;
import java.beans.*;

public class JessEdge implements Serializable {



    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private int id; //fact id
    private EdgeEnum label; //type

    private int identity;
    private int start;
    private int end;


    private JessNode node1;
    private JessNode node2;


    public JessEdge(){

    }

    public void setIdentity(int i){
        int temp = identity;
        identity = i;
        pcs.firePropertyChange("identity",temp,identity);
    }
    public int getIdentity(){
        return identity;
    }

    public int getStart(){
        return start;
    }

    public void setStart(int i){
        int temp = start;
        start = i;
        pcs.firePropertyChange("start", temp, start);
    }

    public int getEnd(){
        return end;
    }

    public void setEnd(int i){
        int temp = end;
        end = i;
        pcs.firePropertyChange("end", temp, end);
    }
    public EdgeEnum getLabel(){
        return label;
    }

    public void setId(int ID){ //neo4j automatic identifier
        id = ID;
    }

    public void setLabel(EdgeEnum l){
        label = l;
    }

    public JessNode getNode1(){
        return node1;
    }

    public void setNode1(JessNode n){
        node1=n;
    }

    public JessNode getNode2(){
        return node2;
    }

    public void setNode2(JessNode n){
        node2=n;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
}
