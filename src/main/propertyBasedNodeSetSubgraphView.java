package main;

import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class propertyBasedNodeSetSubgraphView {

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();


    public static  void VSS_declare(Statement stmt){
        try {
            HashMap<String, String> nameToIds = new HashMap<>();
            String poID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'tk'  " +
                    "  SET f.VSS = CASE WHEN f.VSS IS NOT NULL THEN f.VSS + ',f' ELSE 'f' END " +
                    "  SET po.VSS = CASE WHEN po.VSS IS NOT NULL THEN po.VSS + ',po' ELSE 'po' END " +
                    "$$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSS_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE po.VSS CONTAINS 'po' RETURN po $$) as (po agtype);");

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
                    "$$ MATCH (f:Forum)-[e:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND id(po) <1970850000000000 " +
                    "  SET f.VSL = CASE WHEN f.VSL IS NOT NULL THEN f.VSL + ',f' ELSE 'f' END " +
                    "  SET po.VSL = CASE WHEN po.VSL IS NOT NULL THEN po.VSL + ',po' ELSE 'po' END " +
                    "$$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE po.VSL CONTAINS 'po' RETURN po $$) as (po agtype);");

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
            String poID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <844700000000000  " +
                    " WITH COLLECT(DISTINCT c) +COLLECT(DISTINCT m)+COLLECT(DISTINCT s)+COLLECT(DISTINCT t) AS allnodes"+
                    " UNWIND allnodes AS c SET c.VCS = 'c' " +
                    "$$) as (idpo agtype);");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL RETURN c $$) as (po agtype);");

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
            String poID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male'" +
                    "  SET c.VCL = CASE WHEN c.VCL IS NOT NULL THEN c.VCL + ',c' ELSE 'c' END " +
                    "  SET m.VCL = CASE WHEN m.VCL IS NOT NULL THEN m.VCL + ',m' ELSE 'm' END " +
                    "  SET s.VCL = CASE WHEN s.VCL IS NOT NULL THEN s.VCL + ',s' ELSE 's' END " +
                    "  SET t.VCL = CASE WHEN t.VCL IS NOT NULL THEN t.VCL + ',t' ELSE 't' END " +
                    "$$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCL IS NOT NULL RETURN c $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanup(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Post) WHERE c.VSS IS NOT NULL REMOVE c.VSS $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Post) WHERE c.VSL IS NOT NULL REMOVE c.VSL $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL REMOVE c.VCS $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCL IS NOT NULL REMOVE c.VCL $$) as (po agtype);");


            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH ()-[e]->() WHERE e.VSS IS NOT NULL REMOVE e.VSS $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH ()-[e]->() WHERE e.VSL IS NOT NULL REMOVE e.VSL $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH ()-[e]->() WHERE e.VCS IS NOT NULL REMOVE e.VCS $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH ()-[e]->() WHERE e.VCL IS NOT NULL REMOVE e.VCL $$) as (po agtype);");


            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL RETURN c $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
            System.out.println("cleanup done ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}