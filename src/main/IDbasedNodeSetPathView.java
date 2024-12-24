package main;

import org.apache.age.jdbc.base.Agtype;
import org.apache.age.jdbc.base.type.AgtypeMap;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/* Found all nodes using node view*/
public class IDbasedNodeSetPathView {

    static HashMap<String,HashMap<String, HashSet<String>>> nodeTable = new HashMap<>();
    static HashMap<String,HashMap<String, HashSet<String>>> edgeTable = new HashMap<>();
    static HashMap<String,HashMap<String, ArrayList<String[]>>> pathTable = new HashMap<>();
public static  void VSS_declare(Statement stmt){
    try {
        HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
        HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
        HashMap<String, ArrayList<String[]>> pathNameToIds = new HashMap<>();
        HashSet<String> nodeIDs = new HashSet<>();
        HashSet<String> edgeIDs = new HashSet<>();
        String[] edgesInAPath = new String[1];
        ArrayList<String[]> allPaths = new ArrayList<>();

        ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'tk'  " +
                " RETURN DISTINCT e $$) as (e1 agtype,e2 agtype, e3 agtype);");

        while (rs.next()) {
            // Returning Result as Agtype
            AgtypeMap returnedAgtypeMap = rs.getObject(1, Agtype.class).getMap();
            String startID =  returnedAgtypeMap.getObject("start_id").toString();
            String endID =  returnedAgtypeMap.getObject("end_id").toString();
            String edgeID =  returnedAgtypeMap.getObject("id").toString();
            nodeIDs.add(startID);
            nodeIDs.add(endID);
            edgeIDs.add(edgeID);
            edgesInAPath[0]=(edgeID);

            allPaths.add(edgesInAPath);
        }
        nodeNameToIds.put("nodes",nodeIDs);
        edgeNameToIds.put("edges",edgeIDs);
        pathNameToIds.put("p",allPaths);
        nodeTable.put("VSS", nodeNameToIds);
        edgeTable.put("VSS",edgeNameToIds);
        pathTable.put("VSS",pathNameToIds);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    public static  void VSS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            HashSet<String> nodeIDs = nodeTable.get("VSS").get("nodes");
            for(String s: nodeIDs) IDs += s + ",";
            IDs = IDs.substring(0,IDs.length()-1);
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE ID(po) IN ["+IDs+"] RETURN c $$) as (c agtype);");

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
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, ArrayList<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[1];
            ArrayList<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND id(po) <1970850000000000 " +
                    " RETURN DISTINCT e $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                AgtypeMap returnedAgtypeMap = rs.getObject(1, Agtype.class).getMap();
                String startID =  returnedAgtypeMap.getObject("start_id").toString();
                String endID =  returnedAgtypeMap.getObject("end_id").toString();
                String edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[0]=(edgeID);

                allPaths.add(edgesInAPath);
            }
            nodeNameToIds.put("nodes",nodeIDs);
            edgeNameToIds.put("edges",edgeIDs);
            pathNameToIds.put("p",allPaths);
            nodeTable.put("VSL", nodeNameToIds);
            edgeTable.put("VSL",edgeNameToIds);
            pathTable.put("VSL",pathNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            HashSet<String> nodeIDs = nodeTable.get("VSL").get("nodes");
            for(String s: nodeIDs) IDs += s + ",";
            IDs = IDs.substring(0,IDs.length()-1);
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE ID(po) IN ["+IDs+"] RETURN c $$) as (c agtype);");

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
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, ArrayList<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[3];
            ArrayList<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <844900000000000  " +
                    " RETURN DISTINCT e1,e2,e3 $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                AgtypeMap returnedAgtypeMap = rs.getObject(1, Agtype.class).getMap();
                String startID =  returnedAgtypeMap.getObject("start_id").toString();
                String endID =  returnedAgtypeMap.getObject("end_id").toString();
                String edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[0]=(edgeID);

                returnedAgtypeMap = rs.getObject(2, Agtype.class).getMap();
                startID =  returnedAgtypeMap.getObject("start_id").toString();
                endID =  returnedAgtypeMap.getObject("end_id").toString();
                edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[1]=(edgeID);

                returnedAgtypeMap = rs.getObject(3, Agtype.class).getMap();
                startID =  returnedAgtypeMap.getObject("start_id").toString();
                endID =  returnedAgtypeMap.getObject("end_id").toString();
                edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[2]=(edgeID);

                allPaths.add(edgesInAPath);
            }
            nodeNameToIds.put("nodes",nodeIDs);
            edgeNameToIds.put("edges",edgeIDs);
            pathNameToIds.put("p",allPaths);
            nodeTable.put("VCS", nodeNameToIds);
            edgeTable.put("VCS",edgeNameToIds);
            pathTable.put("VCS",pathNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            HashSet<String> nodeIDs = nodeTable.get("VCS").get("nodes");
            for(String s: nodeIDs) IDs += s + ",";
            IDs = IDs.substring(0,IDs.length()-1);
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


    public static  void VCL_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, ArrayList<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[3];
            ArrayList<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male'  " +
                    " RETURN DISTINCT e1,e2,e3 $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                AgtypeMap returnedAgtypeMap = rs.getObject(1, Agtype.class).getMap();
                String startID =  returnedAgtypeMap.getObject("start_id").toString();
                String endID =  returnedAgtypeMap.getObject("end_id").toString();
                String edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[0]=(edgeID);

                returnedAgtypeMap = rs.getObject(2, Agtype.class).getMap();
                startID =  returnedAgtypeMap.getObject("start_id").toString();
                endID =  returnedAgtypeMap.getObject("end_id").toString();
                edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[1]=(edgeID);

                returnedAgtypeMap = rs.getObject(3, Agtype.class).getMap();
                startID =  returnedAgtypeMap.getObject("start_id").toString();
                endID =  returnedAgtypeMap.getObject("end_id").toString();
                edgeID =  returnedAgtypeMap.getObject("id").toString();
                nodeIDs.add(startID);
                nodeIDs.add(endID);
                edgeIDs.add(edgeID);
                edgesInAPath[2]=(edgeID);

                allPaths.add(edgesInAPath);
            }
            nodeNameToIds.put("nodes",nodeIDs);
            edgeNameToIds.put("edges",edgeIDs);
            pathNameToIds.put("p",allPaths);
            nodeTable.put("VCL", nodeNameToIds);
            edgeTable.put("VCL",edgeNameToIds);
            pathTable.put("VCL",pathNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            HashSet<String> nodeIDs = nodeTable.get("VCL").get("nodes");
            for(String s: nodeIDs) IDs += s + ",";
            IDs = IDs.substring(0,IDs.length()-1);
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