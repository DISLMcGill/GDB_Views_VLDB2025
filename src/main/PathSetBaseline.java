package main;

import org.apache.age.jdbc.base.Agtype;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/* Found all nodes using node view*/
public class PathSetBaseline {

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();


    public static void VSS_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Internet Explorer' AND id(po) <1970400000000000" +
                    " RETURN id(po) $$) as (idpo agtype);");

            while (rs.next()) {
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VSL_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'uz' " +
                    " RETURN id(po) $$) as (idpo agtype);");

            while (rs.next()) {
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VMS_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) " +
                    "WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer' AND id(c) <844630000000000 " +
                    " RETURN DISTINCT p $$) as (idc agtype);");

            while (rs.next()) {
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VML_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) WHERE s.gender='male' AND m.browserUsed = 'Chrome' AND id(c) <844640000000000 " +
                    " RETURN DISTINCT p $$) as (idc agtype);");

            while (rs.next()) {
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void VCS_baseline(Statement stmt){
        try {
            int count = 0;

	    ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) " +
                     "WHERE s.gender='female' AND m.browserUsed = 'Firefox'  AND id(c) <844425000000000 AND t.language = 'ar'" +
                    " RETURN DISTINCT p $$) as (idm agtype);");

            while (rs.next()) {
                count+=1;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VCL_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[:REPLY_OF]->(m:Comment)<-[:LIKES]-(s:Person)<-[:HAS_CREATOR]-(t:Post) " +
                     "WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer'  AND id(c) <844425000000000 AND t.language = 'tk'" +
                    " RETURN DISTINCT p $$) as (idm agtype);");

            while (rs.next()) {
                count+=1;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void archive_VCL_return_alot_baseline(Statement stmt){
        try {
            int count = 0;
            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) WHERE s.gender='male'  " +
                    " RETURN DISTINCT id(c),id(e1), id(m),id(e2), id(s),id(e3),id(t) $$) as (idc agtype,ide1 agtype, idm agtype,ide2 agtype,ids agtype,ide3 agtype,idt agtype);");

            while (rs.next()) {

                // Returning Result as Agtype
                Agtype returnedAgtype = rs.getObject(1, Agtype.class);
                returnedAgtype = rs.getObject(2, Agtype.class);
                returnedAgtype = rs.getObject(3, Agtype.class);
                returnedAgtype = rs.getObject(4, Agtype.class);
                returnedAgtype = rs.getObject(5, Agtype.class);
                returnedAgtype = rs.getObject(6, Agtype.class);
                returnedAgtype = rs.getObject(7, Agtype.class);
                count+=1;

            }
            System.out.println("Found rows: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void randomQ(Statement stmt){
        try {
            int count = 0;


            ResultSet rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c0:Comment)-[e0:REPLY_OF]->(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) WHERE s.gender='female' AND m.browserUsed ='Firefox'  AND id(c) <844500000000000 " +
                    " RETURN p $$) as (idc agtype);");

            while (rs.next()) {
                count++;

            }
            System.out.println("Found rows: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
