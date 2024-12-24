package main;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class sigmodPropertyBasedNodeSetNodeView {

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();


    public static  void VCS_S_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000" +
                    " WITH DISTINCT c SET c.VCS = CASE WHEN c.VCS IS NOT NULL THEN c.VCS + ',c' ELSE 'c' END  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_S(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL AND id(c) < 847900000000000RETURN  c $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_L_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000 " +
                    " WITH DISTINCT c SET c.VCS = CASE WHEN c.VCS IS NOT NULL THEN c.VCS + ',c' ELSE 'c' END  $$) as (idpo agtype);");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_L(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL  RETURN c $$) as (po agtype);");

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
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Post) WHERE c.VSS IS NOT NULL REMOVE c.VSS $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Post) WHERE c.VSL IS NOT NULL REMOVE c.VSL $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL REMOVE c.VCS $$) as (po agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment) WHERE c.VCL IS NOT NULL REMOVE c.VCL $$) as (po agtype);");

            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
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

    public static  void VCL_S_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000" +
                    " WITH DISTINCT c SET c.VCS = CASE WHEN c.VCS IS NOT NULL THEN c.VCS + ',c' ELSE 'c' END  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_S(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL AND id(c) < 846200000000000 RETURN  c $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_L_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000" +
                    " WITH DISTINCT c SET c.VCS = CASE WHEN c.VCS IS NOT NULL THEN c.VCS + ',c' ELSE 'c' END  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_L(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment) WHERE c.VCS IS NOT NULL AND id(c) < 849200000000000 RETURN  c $$) as (po agtype);");

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
