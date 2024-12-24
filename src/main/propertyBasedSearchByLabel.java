package main;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class propertyBasedSearchByLabel{

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();


    public static  void VSS_declare(Statement stmt){
        try {
            HashMap<String, String> nameToIds = new HashMap<>();
            String poID = "";
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'tk'  " +
                    " WITH DISTINCT po SET po.VSS= 'po' $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSS_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE po.VSS='po' RETURN po $$) as (po agtype);");

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
                    " WITH DISTINCT po SET po.VSL= 'po' $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VSL_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (po:Post) WHERE po.VSL='po' RETURN po $$) as (po agtype);");

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
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) < 844900000000000" +
                    " WITH DISTINCT c SET c.VCS = CASE WHEN c.VCS IS NOT NULL THEN c.VCS + ',c' ELSE 'c' END  $$) as (idpo agtype);");
            System.out.println("FINISHI c");
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) < 844900000000000" +
                    " WITH DISTINCT m SET m.VCS = CASE WHEN m.VCS IS NOT NULL THEN m.VCS + ',m' ELSE 'm' END  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_local(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL RETURN  c $$) as (po agtype);");

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
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <846000000000000 " +
                    " WITH DISTINCT c SET c.VCL = CASE WHEN c.VCL IS NOT NULL THEN c.VCL + ',c' ELSE 'c' END  $$) as (idpo agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE s.gender='male' AND id(c) <846000000000000 " +
                    " WITH DISTINCT m SET m.VCL = CASE WHEN m.VCL IS NOT NULL THEN m.VCL + ',m' ELSE 'm' END  $$) as (idpo agtype);");

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

            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL RETURN c $$) as (po agtype);");

            //while (rs.next()) {
                // Returning Result as Agtype
            //     count++;
           
            //}
            //System.out.println("Found nodes: "+ count);
            System.out.println("cleanup done ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
