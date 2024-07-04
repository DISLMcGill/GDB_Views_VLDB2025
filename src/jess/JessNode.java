package jess;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import jess.Defrule;

import java.beans.*;
import java.io.Serializable;

public class JessNode implements Serializable{



    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private NodeEnum label;

    private int id; //neo4j built in identifier, useful for creating edges since that's how edges are defined


    //User fields
    private int userId;
    private String displayname;
    private String aboutme;
    private int reputation;
    private int upvotes;
    private int downvotes;
    private int views;


    //Post fields
    private String body;
    private int comments;
    private int postId;
    private int score;

    //Tag field
    private String tagId;




    public JessNode(){

    }

    public int getId() {
        return id;
    }

    public void setId(int i){
        int temp = id;
        id = i;
        pcs.firePropertyChange("id",temp, id);
    }

    //User getter and setters
    public int getUserId(){
        return userId;
    }

    public void setUserId(int ID){
        int temp = userId;
        userId = ID;
        pcs.firePropertyChange("userId", new Integer(temp), new Integer(userId));
    }

    public NodeEnum getLabel(){
        return label;
    }

    public void setLabel(NodeEnum lt){
        label = lt;
        //No fire for property change since labels do not change
    }

    public String getDisplayname(){
        return displayname;
    }

    public void setDisplayname(String s){
        String temp = displayname;
        displayname = s;
        pcs.firePropertyChange("displayname", temp, displayname);
    }

    public String getAboutme(){
        return aboutme;
    }

    public void setAboutme(String s){
        String temp = aboutme;
        aboutme = s;
        pcs.firePropertyChange("aboutme", temp, aboutme);
    }

    public int getReputation(){
        return reputation;
    }

    public void setReputation(int i){
        int temp = reputation;
        reputation = i;
        pcs.firePropertyChange("reputation", temp, reputation);
    }

    public int getUpvotes(){
        return upvotes;
    }

    public void setUpvotes(int i){
        int temp = upvotes;
        upvotes = i;
        pcs.firePropertyChange("upvotes", temp, upvotes);
    }

    public int getDownvotes(){
        return downvotes;
    }

    public void setDownvotes(int i){
        int temp = downvotes;
        downvotes = i;
        pcs.firePropertyChange("downvotes", temp, downvotes);
    }

    public int getViews(){
        return views;
    }

    public void setViews(int i){
        int temp = views;
        views = i;
        pcs.firePropertyChange("views", temp, views);
    }


    //Post getters and setters

    public int getComments(){
        return comments;
    }

    public void setComments(int i){
        int temp = comments;
        comments = i;
        pcs.firePropertyChange("comments", temp, comments);
    }

    public int getPostId(){
        return postId;
    }

    public void setPostId(int i){
        int temp = postId;
        postId = i;
        pcs.firePropertyChange("postId", temp, postId);
    }

    public String getBody(){
        return body;
    }

    public void setBody(String s){
        String temp = body;
        body = s;
        pcs.firePropertyChange("body", temp, body);
    }

    public int getScore(){
        return score;
    }

    public void setScore(int i){
        int temp = score;
        score = i;
        pcs.firePropertyChange("score", temp, score);
    }


    //Tag getter and setter
    public String getTagId(){
        return tagId;
    }

    public void setTagId(String i){
        String temp = tagId;
        tagId = i;
        pcs.firePropertyChange("tagId", temp, tagId);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }


}
