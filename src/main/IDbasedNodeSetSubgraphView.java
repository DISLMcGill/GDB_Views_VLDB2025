package main;

import org.apache.age.jdbc.base.Agtype;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

/* Found all nodes using node view*/
public class IDbasedNodeSetSubgraphView {

    static HashMap<String,HashMap<String, HashSet<Long>>> nodeTable = new HashMap<>();
    static HashMap<String,HashMap<String, HashSet<Long>>> edgeTable = new HashMap<>();

    public static  void VSS_declare(Statement stmt){
        try {
            HashMap<String, HashSet<Long>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<Long>> edgeNameToIds = new HashMap<>();
            HashSet<Long> poID = new HashSet<>();
            HashSet<Long> fID = new HashSet<>();
            HashSet<Long> eID = new HashSet<>();
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'tk'  " +
                    " RETURN id(f),id(e),id(po) $$) as (idf agtype,ide agtype,idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                fID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(2, Agtype.class);
                eID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(3, Agtype.class);
                poID.add(returnedAgtype.getLong());

            }
            nodeNameToIds.put("po",poID);
            nodeNameToIds.put("f",poID);
            edgeNameToIds.put("e",eID);
            nodeTable.put("VSS", nodeNameToIds);
            edgeTable.put("VSS",edgeNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSS_local(Statement stmt){
        try {
            int count = 0;
            String poID = "";
            HashSet<Long> poSet = nodeTable.get("VSS").get("po");
            for(Long l:poSet) poID += l + ",";
            poID = poID.substring(0,poID.length()-1);
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
            HashMap<String, HashSet<Long>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<Long>> edgeNameToIds = new HashMap<>();
            HashSet<Long> poID = new HashSet<>();
            HashSet<Long> fID = new HashSet<>();
            HashSet<Long> eID = new HashSet<>();
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND id(po) <1970850000000000 " +
                    " RETURN id(f),id(e),id(po) $$) as (idf agtype,ide agtype,idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                fID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(2, Agtype.class);
                eID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(3, Agtype.class);
                poID.add(returnedAgtype.getLong());

            }
            nodeNameToIds.put("po",poID);
            nodeNameToIds.put("f",poID);
            edgeNameToIds.put("e",eID);
            nodeTable.put("VSL", nodeNameToIds);
            edgeTable.put("VSL",edgeNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_local(Statement stmt){
        try {
            int count = 0;
            String poID = "";
            HashSet<Long> poSet = nodeTable.get("VSL").get("po");
            for(Long l:poSet) poID += l + ",";
            poID = poID.substring(0,poID.length()-1);
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
            HashMap<String, HashSet<Long>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<Long>> edgeNameToIds = new HashMap<>();
            HashSet<Long> cID = new HashSet<>();
            HashSet<Long> e1ID = new HashSet<>();
            HashSet<Long> mID = new HashSet<>();
            HashSet<Long> e2ID = new HashSet<>();
            HashSet<Long> sID = new HashSet<>();
            HashSet<Long> e3ID = new HashSet<>();
            HashSet<Long> tID = new HashSet<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male'  " +
                    " RETURN DISTINCT id(c),id(e1), id(m),id(e2), id(s),id(e3),id(t) $$) as (idc agtype,ide1 agtype, idm agtype,ide2 agtype,ids agtype,ide3 agtype,idt agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                cID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(2, Agtype.class);
                e1ID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(3, Agtype.class);
                mID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(4, Agtype.class);
                e2ID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(5, Agtype.class);
                sID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(6, Agtype.class);
                e3ID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(7, Agtype.class);
                tID.add(returnedAgtype.getLong());
            }
            nodeNameToIds.put("c",cID);
            nodeNameToIds.put("m",mID);
            nodeNameToIds.put("s",sID);
            nodeNameToIds.put("t",tID);
            edgeNameToIds.put("e1",e1ID);
            edgeNameToIds.put("e2",e2ID);
            edgeNameToIds.put("e3",e3ID);
            nodeTable.put("VCL", nodeNameToIds);
            edgeTable.put("VCL", edgeNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            HashSet<Long> cIDs = nodeTable.get("VCL").get("c");
            HashSet<Long> mIDs = nodeTable.get("VCL").get("m");
            for(Long l: cIDs) IDs += l + " , ";
            for(Long l: mIDs) IDs += l + " , ";
            IDs = IDs.substring(0,IDs.length()-3);
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
            HashMap<String, HashSet<Long>> nodeNameToIds = new HashMap<>();
            HashMap<String, HashSet<Long>> edgeNameToIds = new HashMap<>();
            HashSet<Long> cID = new HashSet<>();
            HashSet<Long> e1ID = new HashSet<>();
            HashSet<Long> mID = new HashSet<>();
            HashSet<Long> e2ID = new HashSet<>();
            HashSet<Long> sID = new HashSet<>();
            HashSet<Long> e3ID = new HashSet<>();
            HashSet<Long> tID = new HashSet<>();

            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <844900000000000  " +
                    " RETURN DISTINCT id(c),id(e1), id(m),id(e2), id(s),id(e3),id(t) $$) as (idc agtype,ide1 agtype, idm agtype,ide2 agtype,ids agtype,ide3 agtype,idt agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                cID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(2, Agtype.class);
                e1ID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(3, Agtype.class);
                mID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(4, Agtype.class);
                e2ID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(5, Agtype.class);
                sID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(6, Agtype.class);
                e3ID.add(returnedAgtype.getLong());
                returnedAgtype = rs.getObject(7, Agtype.class);
                tID.add(returnedAgtype.getLong());
            }
            nodeNameToIds.put("c",cID);
            nodeNameToIds.put("m",mID);
            nodeNameToIds.put("s",sID);
            nodeNameToIds.put("t",tID);
            edgeNameToIds.put("e1",e1ID);
            edgeNameToIds.put("e2",e2ID);
            edgeNameToIds.put("e3",e3ID);
            nodeTable.put("VCS", nodeNameToIds);
            edgeTable.put("VCS", edgeNameToIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_local(Statement stmt){
        try {
            int count = 0;
            String IDs = "";
            HashSet<Long> cIDs = nodeTable.get("VCL").get("c");
            HashSet<Long> mIDs = nodeTable.get("VCL").get("m");
            for(Long l: cIDs) IDs += l + " , ";
            for(Long l: mIDs) IDs += l + " , ";
            IDs = IDs.substring(0,IDs.length()-3);
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