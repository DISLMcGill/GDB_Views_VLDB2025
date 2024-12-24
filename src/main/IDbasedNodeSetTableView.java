package main;

import org.apache.age.jdbc.base.Agtype;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/* Found all nodes using node view*/
public class IDbasedNodeSetTableView {

    static HashMap<String,HashMap<String, HashSet<String>>> nodeTable = new HashMap<>();
    static HashMap<String,HashMap<String, HashSet<String>>> edgeTable = new HashMap<>();
    static HashMap<String,ArrayList<Long[]>> tupleTable = new HashMap<>();

    public static  void VSS_declare(Statement stmt){
        try {
            Long[] tupleInARow = new Long[1];
            ArrayList<Long[]> allRows = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'tk'  " +
                    " RETURN DISTINCT ID(po) $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Long poID = rs.getObject(1, Agtype.class).getLong();
                tupleInARow[0]=(poID);

                allRows.add(tupleInARow);
            }
            tupleTable.put("VSS",allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            ArrayList<Long[]> nodeIDs = tupleTable.get("VSS");
            for(Long[] row: nodeIDs){
                for(Long s:row)  IDs += s + ",";
            }
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
            Long[] tupleInARow = new Long[1];
            ArrayList<Long[]> allRows = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND id(po) <1970850000000000 " +
                    " RETURN DISTINCT ID(po) $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Long poID = rs.getObject(1, Agtype.class).getLong();
                tupleInARow[0]=(poID);

                allRows.add(tupleInARow);
            }
            tupleTable.put("VSL",allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            ArrayList<Long[]> nodeIDs = tupleTable.get("VSL");
            for(Long[] row: nodeIDs){
                for(Long s:row)  IDs += s + ",";
            }
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
            Long[] tupleInARow = new Long[2];
            ArrayList<Long[]> allRows = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <844900000000000  " +
                    " RETURN DISTINCT ID(c),ID(m) $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Long poID = rs.getObject(1, Agtype.class).getLong();
                tupleInARow[0]=(poID);
                poID = rs.getObject(2, Agtype.class).getLong();
                tupleInARow[1]=(poID);

                allRows.add(tupleInARow);
            }
            tupleTable.put("VCS",allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            ArrayList<Long[]> nodeIDs = tupleTable.get("VCS");
            for(Long[] row: nodeIDs){
                for(Long s:row)  IDs += s + ",";
            }
            IDs = IDs.substring(0,IDs.length()-1);
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE ID(c) IN ["+IDs+"] RETURN DISTINCT c $$) as (c agtype);");
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
            Long[] tupleInARow = new Long[2];
            ArrayList<Long[]> allRows = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male'  " +
                    " RETURN DISTINCT ID(c),ID(m) $$) as (e1 agtype,e2 agtype, e3 agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Long poID = rs.getObject(1, Agtype.class).getLong();
                tupleInARow[0]=(poID);
                poID = rs.getObject(2, Agtype.class).getLong();
                tupleInARow[1]=(poID);

                allRows.add(tupleInARow);
            }
            tupleTable.put("VCL",allRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_local(Statement stmt) {
        try {
            int count = 0;
            String IDs = "";
            ArrayList<Long[]> nodeIDs = tupleTable.get("VCL");
            for (Long[] row : nodeIDs) {
                for (Long s : row) IDs += s + ",";
            }
            IDs = IDs.substring(0, IDs.length() - 1);
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE ID(c) IN [" + IDs + "] RETURN DISTINCT c $$) as (c agtype);");
            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}