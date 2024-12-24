package main;

import org.apache.age.jdbc.base.Agtype;
import org.apache.age.jdbc.base.type.AgtypeMap;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/* Found all nodes using node view*/
public class IDbasedPathSetPathView_copy {

    static HashMap<String,HashMap<String, HashSet<String>>> nodeTable = new HashMap<>();
    static HashMap<String,HashMap<String, HashSet<String>>> edgeTable = new HashMap<>();
    static HashMap<String,HashMap<String, List<String[]>>> pathTable = new HashMap<>();

    public static void VSS_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, List<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[1];
            List<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e1:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Internet Explorer' AND id(po) <1970400000000000"+
                    " RETURN DISTINCT e1 $$) as (e1 agtype);");

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

    public static void VSS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "[";
            List<String[]> allPaths = pathTable.get("VSS").get("p");
            for(String[] onePath: allPaths){
                IDs += "["+ onePath[0]+ "]";
                IDs += ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            IDs += ']';
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ UNWIND " + IDs + " AS onePath " +
                    "MATCH p=(f:Forum)-[e1:CONTAINER_OF]->(po:Post)" +
                    "WHERE ID(e1)= onePath[0] RETURN p $$) as (p agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VSL_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, List<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[1];
            List<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e1:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Firefox' "+
                    " RETURN DISTINCT e1 $$) as (e1 agtype);");

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

    public static void VSL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "[";
            List<String[]> allPaths = pathTable.get("VSS").get("p");
            for(String[] onePath: allPaths){
                IDs += "["+ onePath[0]+ "]";
                IDs += ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            IDs += ']';
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ UNWIND " + IDs + " AS onePath " +
                    "MATCH p=(f:Forum)-[e1:CONTAINER_OF]->(po:Post)" +
                    "WHERE ID(e1)= onePath[0] RETURN p $$) as (p agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void VMS_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, List<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[2];
            List<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) " +
                    "WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer' AND id(c) <844630000000000  " +
                    " RETURN DISTINCT e1,e2 $$) as (e1 agtype,e2 agtype);");

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

                allPaths.add(edgesInAPath);
            }
            nodeNameToIds.put("nodes",nodeIDs);
            edgeNameToIds.put("edges",edgeIDs);
            pathNameToIds.put("p",allPaths);
            nodeTable.put("VMS", nodeNameToIds);
            edgeTable.put("VMS",edgeNameToIds);
            pathTable.put("VMS",pathNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VMS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "[";
            List<String[]> allPaths = pathTable.get("VMS").get("p");
            for(String[] onePath: allPaths){
                IDs += "["+ onePath[0] + "," + onePath[1] + "]";
                IDs += ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            IDs += ']';
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ UNWIND " + IDs + " AS onePath " +
                    "MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) " +
                    "WHERE ID(e1)= onePath[0] AND ID(e2)= onePath[1] RETURN p $$) as (p agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VML_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, List<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[2];
            List<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) " +
		    " WHERE NOT m.browserUsed = 'Internet Explorer' AND id(c) <845100000000000 "+
                    " RETURN DISTINCT e1,e2 $$) as (e1 agtype,e2 agtype);");

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

                allPaths.add(edgesInAPath);
            }
            nodeNameToIds.put("nodes",nodeIDs);
            edgeNameToIds.put("edges",edgeIDs);
            pathNameToIds.put("p",allPaths);
            nodeTable.put("VML", nodeNameToIds);
            edgeTable.put("VML",edgeNameToIds);
            pathTable.put("VML",pathNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VML_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "[";
            List<String[]> allPaths = pathTable.get("VML").get("p");
            for(String[] onePath: allPaths){
                IDs += "["+ onePath[0] + "," + onePath[1] + "]";
                IDs += ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            IDs += ']';
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ UNWIND " + IDs + " AS onePath " +
                    "MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) " +
                    "WHERE ID(e1)= onePath[0] AND ID(e2)= onePath[1] RETURN p $$) as (p agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void VCS_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, List<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[3];
            List<String[]> allPaths = new ArrayList<>();

	    ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e0:REPLY_OF]->(m:Comment)<-[e1:LIKES]-(s:Person)<-[e2:HAS_CREATOR]-(t:Post) " +
                     "WHERE s.gender='female' AND m.browserUsed = 'Firefox'  AND id(c) <844425000000000 AND t.language = 'ar'" +
                    " RETURN DISTINCT e0,e1,e2 $$) as (e1 agtype,e2 agtype, e3 agtype);");

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

    public static void VCS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "[";
            List<String[]> allPaths = pathTable.get("VCS").get("p");
            for(String[] onePath: allPaths){
                IDs += "["+ onePath[0] + "," + onePath[1] + "," + onePath[2] + "]";
                IDs += ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            IDs += ']';
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ UNWIND " + IDs + " AS onePath " +
		    "MATCH p=(c:Comment)-[e0:REPLY_OF]->(m:Comment)<-[e1:LIKES]-(s:Person)<-[e2:HAS_CREATOR]-(t:Post) " +
                    "WHERE ID(e0)= onePath[0] AND ID(e1)= onePath[1] AND ID(e2)= onePath[2] RETURN p $$) as (p agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VCL_declare(Statement stmt){
        try {
            HashMap<String, HashSet<String>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<String>> edgeNameToIds = new HashMap<>();
            HashMap<String, List<String[]>> pathNameToIds = new HashMap<>();
            HashSet<String> nodeIDs = new HashSet<>();
            HashSet<String> edgeIDs = new HashSet<>();
            String[] edgesInAPath = new String[3];
            List<String[]> allPaths = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
			    "$$ MATCH p=(c:Comment)-[e0:REPLY_OF]->(m:Comment)<-[e1:LIKES]-(s:Person)<-[e2:HAS_CREATOR]-(t:Post) " +
			    "WHERE s.gender='male' AND id(c) <844440000000000 "+
                    " RETURN DISTINCT e0,e1,e2 $$) as (e1 agtype,e2 agtype, e3 agtype);");

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

    public static void VCL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "[";
            List<String[]> allPaths = pathTable.get("VCS").get("p");
            for(String[] onePath: allPaths){
                IDs += "["+ onePath[0] + "," + onePath[1] + "," + onePath[2] + "]";
                IDs += ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            IDs += ']';
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ UNWIND " + IDs + " AS onePath " +
		    "MATCH p=(c:Comment)-[e0:REPLY_OF]->(m:Comment)<-[e1:LIKES]-(s:Person)<-[e2:HAS_CREATOR]-(t:Post) " +
                    "WHERE ID(e0)= onePath[0] AND ID(e1)= onePath[1] AND ID(e2)= onePath[2] RETURN p $$) as (p agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
