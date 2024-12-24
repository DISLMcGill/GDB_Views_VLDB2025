package main;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class sigmodPropertyBasedSubgraphSetNodeView {

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();



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


    public static  void VCS_S_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000" +
                    " WITH DISTINCT c SET c.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
            System.out.println("finish c");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000  " +
                    " WITH DISTINCT m SET m.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
            System.out.println("finish m");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000 " +
                    " WITH DISTINCT s SET s.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000  " +
                    " WITH DISTINCT t SET t.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static  void VCS_S(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) "+
                    " WHERE  id(c) <847900000000000 AND c.VCL IS NOT NULL AND m.VCL IS NOT NULL AND s.VCL IS NOT NULL  AND t.VCL IS NOT NULL RETURN DISTINCT m $$) as (po agtype);");

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
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000" +
                    " WITH DISTINCT c SET c.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
	    System.out.println("finish c");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000  " +
		    " WITH DISTINCT m SET m.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
	    System.out.println("finish m");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000 " +
		    " WITH DISTINCT s SET s.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000  " +
		    " WITH DISTINCT t SET t.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCS_L(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) "+
		    " WHERE  id(c) <850600000000000 AND c.VCL IS NOT NULL AND m.VCL IS NOT NULL AND s.VCL IS NOT NULL  AND t.VCL IS NOT NULL RETURN DISTINCT m $$) as (po agtype);");

            while (rs.next()) {
                // Returning Result as Agtype
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static  void VCL_S_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000" +
                    " WITH DISTINCT c SET c.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
            System.out.println("finish c");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000  " +
                    " WITH DISTINCT m SET m.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
            System.out.println("finish m");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000 " +
                    " WITH DISTINCT s SET s.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000  " +
                    " WITH DISTINCT t SET t.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static  void VCL_S(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) "+
                    " WHERE  id(c) <846500000000000 AND c.VCL IS NOT NULL AND m.VCL IS NOT NULL AND s.VCL IS NOT NULL  AND t.VCL IS NOT NULL RETURN DISTINCT m $$) as (po agtype);");

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
                    " WITH DISTINCT c SET c.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
            System.out.println("finish c");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000  " +
                    " WITH DISTINCT m SET m.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");
            System.out.println("finish m");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000 " +
                    " WITH DISTINCT s SET s.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000  " +
                    " WITH DISTINCT t SET t.VCL = 'VCL' "+
                    "  $$) as (idpo agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void VCL_L(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH (c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) "+
                    " WHERE  id(c) <847200000000000 AND c.VCL IS NOT NULL AND m.VCL IS NOT NULL AND s.VCL IS NOT NULL  AND t.VCL IS NOT NULL RETURN DISTINCT m $$) as (po agtype);");

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
