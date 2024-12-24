package main;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class IDbasedNodeSetNodeView {

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();


    public static  void VSS_declare(Statement stmt){
        try {
            HashMap<String, String> nameToIds = new HashMap<>();
            String poID = "";
            // 176 NODES
//            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
//                    "$$ MATCH (f:Forum)-[:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND id(po) <1970330000000000 " +
//                    " RETURN id(po) $$) as (idpo agtype);");
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'tk' "+
                    " RETURN id(po) $$) as (idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                poID += returnedAgtype.getLong() + ",";

            }
            poID = poID.substring(0,poID.length()-1);
            nameToIds.put("po",poID);
            nodeTable.put("VSS", nameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSS_local(Statement stmt){
        try {
            int count = 0;
            String poID = nodeTable.get("VSS").get("po");
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE ID(po) IN ["+poID+"] RETURN po $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
               count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_declare(Statement stmt){
        try {
            HashMap<String, String> nameToIds = new HashMap<>();
            String poID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND id(po) <1970850000000000 " +
                    " RETURN id(po) $$) as (idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                poID += returnedAgtype.getLong() + ",";

            }
            poID = poID.substring(0,poID.length()-1);
            nameToIds.put("po",poID);
            nodeTable.put("VSL", nameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_local(Statement stmt){
        try {
            int count = 0;
            String poID = nodeTable.get("VSL").get("po");
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE ID(po) IN ["+poID+"] RETURN po $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_declare(Statement stmt){
        try {
            HashMap<String, String> nameToIds = new HashMap<>();
            String cID = "";
            String mID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <846000000000000 " +
                    " RETURN DISTINCT id(c), id(m) $$) as (idc agtype,idm agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                cID += returnedAgtype.getLong() + ",";
                returnedAgtype = rs.getObject(2, Agtype.class);
                mID += returnedAgtype.getLong() + ",";

            }
            cID = cID.substring(0,cID.length()-1);
            mID = mID.substring(0,mID.length()-1);
            nameToIds.put("c",cID);
            nameToIds.put("m",mID);
            nodeTable.put("VLL", nameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = nodeTable.get("VLL").get("c") + "," + nodeTable.get("VLL").get("m");
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE ID(c) IN ["+IDs+"] RETURN c $$) as (c agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_declare(Statement stmt){
        try {
            HashMap<String, String> nameToIds = new HashMap<>();
            String cID = "";
            String mID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <844900000000000  " +
                    " RETURN DISTINCT id(c), id(m) $$) as (idc agtype,idm agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                cID += returnedAgtype.getLong() + ",";
                returnedAgtype = rs.getObject(2, Agtype.class);
                mID += returnedAgtype.getLong() + ",";

            }
            cID = cID.substring(0,cID.length()-1);
            mID = mID.substring(0,mID.length()-1);
            nameToIds.put("c",cID);
            nameToIds.put("m",mID);
            nodeTable.put("VLL", nameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = nodeTable.get("VLL").get("c") + "," + nodeTable.get("VLL").get("m");
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE ID(c) IN ["+IDs+"] RETURN c $$) as (c agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
