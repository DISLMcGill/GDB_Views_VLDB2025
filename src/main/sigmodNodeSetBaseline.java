package main;

import org.apache.age.jdbc.base.Agtype;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class sigmodNodeSetBaseline {

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();




    public static void VCS_S_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000 AND id(c) <847900000000000" +
                    " RETURN DISTINCT c $$) as (idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                count+=1;

            }
            System.out.println("Found rows: "+ count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void VCS_L_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906847000 " +
                    " RETURN DISTINCT c $$) as (idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                count+=1;

            }
            System.out.println("Found rows: "+ count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void VCL_S_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000 AND id(c) <846200000000000" +
                    " RETURN DISTINCT c $$) as (idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                count+=1;

            }
            System.out.println("Found rows: "+ count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VCL_L_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc1', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) WHERE id(s) <1125899906853000 AND id(c) <848800000000000" +
                    " RETURN DISTINCT c $$) as (idpo agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                count+=1;

            }
            System.out.println("Found rows: "+ count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
