package main;

import org.apache.age.jdbc.base.Agtype;
import org.postgresql.util.PGobject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/* Found all nodes using node view*/
public class propertyBasedPathSetPathView {

    static HashMap<String,HashMap<String, HashSet<String>>> nodeTable = new HashMap<>();
    static HashMap<String,HashMap<String, HashSet<String>>> edgeTable = new HashMap<>();
    static HashMap<String,HashMap<String, List<String[]>>> pathTable = new HashMap<>();

    public static void VSS_declare(Statement stmt){
        try {

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MERGE (v:View{ViewName:'VSS'})" +
                    "  $$) as (v agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e1:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Internet Explorer' AND id(po) <1970400000000000"+
                    "WITH DISTINCT f,e1,po CREATE (f)-[rr1:VSS{coID:ID(e1)}]->(po)" +
                    " $$) as (e1 agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e1:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Internet Explorer' AND id(po) <1970400000000000" +
                    " WITH DISTINCT f " +
                    " MATCH (v:View {ViewName:'VSS'}) MERGE (v)-[:CONTAIN_NODE]->(f)"+
                    " $$) as (e1 agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VSS_local(Statement stmt){
        try {
            int count = 0;
            ResultSet    rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View {ViewName:'VSS'})-[:CONTAIN_NODE]->(f:Forum)-[rr1:VSS]->(po:Post)" +
                    "WITH f,po,rr1.coID AS id1 " +
                    "MATCH p = (f)-[e1:CONTAINER_OF]->(po) " +
                    "WHERE ID(e1)= id1 " +
                    "RETURN p $$) as (p agtype);");
            while (rs.next()) {
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VSL_declare(Statement stmt){
        try {

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MERGE (v:View{ViewName:'VSL'})" +
                    "  $$) as (v agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e1:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'uz'"+
                    "WITH DISTINCT f,e1,po CREATE (f)-[rr1:VSL{coID:ID(e1)}]->(po)" +
                    " $$) as (e1 agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (f:Forum)-[e1:CONTAINER_OF]->(po:Post) WHERE po.browserUsed = 'Chrome' AND po.language = 'uz'" +
                    " WITH DISTINCT f " +
                    " MATCH (v:View {ViewName:'VSL'}) MERGE (v)-[:CONTAIN_NODE]->(f)"+
                    " $$) as (e1 agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VSL_local(Statement stmt){
        try {
            int count = 0;
            ResultSet    rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View {ViewName:'VSL'})-[:CONTAIN_NODE]->(f:Forum)-[rr1:VSL]->(po:Post)" +
                    "WITH f,po,rr1.coID AS id1 " +
                    "MATCH p = (f)-[e1:CONTAINER_OF]->(po) " +
                    "WHERE ID(e1)= id1 " +
                    "RETURN p $$) as (p agtype);");
            while (rs.next()) {
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void VMS_declare(Statement stmt){
        try {

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MERGE (v:View{ViewName:'VMS'})" +
                    "  $$) as (v agtype);");

            System.out.println("Finishc ");
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer' AND id(c) <844630000000000  " +
                    " WITH DISTINCT c,m,s,e1,e2 CREATE (c)-[rr1:VMS{coID:ID(e1)}]->(m)-[rr2:VMS{coID:ID(e2)}]->(s)" +
                    "WITH c,rr1,rr2 SET rr2.prevID=ID(rr1) "+
                    " $$) as (e1 agtype);");

            System.out.println("Finishc2 ");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer' AND id(c) <844630000000000 " +
                    " WITH DISTINCT c " +
                    " MATCH (v:View {ViewName:'VMS'}) MERGE (v)-[:CONTAIN_NODE]->(c)"+
                    " $$) as (e1 agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VMS_local(Statement stmt){
        try {
            int count = 0;
            //stmt.executeQuery("CREATE UNIQUE INDEX previdIndex ON ldbc01.\"VMS\"((lower(properties::varchar::jsonb->>'prevID')));");
//            stmt.executeQuery("CREATE INDEX previdIndex ON ldbc01.\"VMS\" USING BTREE (ag_catalog.agtype_access_operator(properties, '\"prevID\"'::ag_catalog.agtype));");

            //time 4538
            ResultSet    rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View {ViewName:'VMS'})-[:CONTAIN_NODE]->(c:Comment)-[rr1:VMS]->(m:Comment)-[rr2:VMS ]->(s:Person)" +
                    "WHERE ID(rr1)= rr2.prevID "+
                    "WITH c,rr1.coID AS id1,m,rr2.coID AS id2,s " +
                    "MATCH p = (c)-[e1:REPLY_OF]->(m)<-[e2:LIKES]-(s) " +
                    "WHERE ID(e1)= id1 AND  ID(e2)=id2 " +
                    "RETURN p $$) as (p agtype);");


//            stmt.executeQuery("DROP INDEX ldbc01.coidIndex;");
//            stmt.executeQuery("DROP INDEX ldbc01.previdIndex;");


            while (rs.next()) {
                // Returning Result as Agtype
                //System.out.println(rs.getObject(1, Agtype.class).getString());
                count++;

            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VML_declare(Statement stmt){
        try {

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MERGE (v:View{ViewName:'VML'})" +
                    "  $$) as (v agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) WHERE s.gender='male' AND m.browserUsed = 'Chrome' AND id(c) <844640000000000 " +
                    " WITH DISTINCT c,m,s,e1,e2 CREATE (c)-[rr1:VML{coID:ID(e1)}]->(m)-[rr2:VML{coID:ID(e2)}]->(s)" +
                    "WITH c,rr1,rr2 SET rr2.prevID=ID(rr1) "+
                    " $$) as (e1 agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person) WHERE s.gender='male' AND m.browserUsed = 'Chrome' AND id(c) <844640000000000" +
                    " WITH DISTINCT c " +
                    " MATCH (v:View {ViewName:'VML'}) MERGE (v)-[:CONTAIN_NODE]->(c)"+
                    " $$) as (e1 agtype);");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VML_local(Statement stmt){
        try {
            int count = 0;
            ResultSet    rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View {ViewName:'VML'})-[:CONTAIN_NODE]->(c:Comment)-[rr1:VML]->(m:Comment)-[rr2:VML]->(s:Person)" +
                    "WHERE ID(rr1)= rr2.prevID "+
                    "WITH c,rr1.coID AS id1,m,rr2.coID AS id2,s " +
                    "MATCH p = (c)-[e1:REPLY_OF]->(m)<-[e2:LIKES]-(s) " +
                    "WHERE ID(e1)= id1 AND  ID(e2)=id2 " +
                    "RETURN p $$) as (p agtype);");

            while (rs.next()) {
                count++;
            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VCS_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MERGE (v:View{ViewName:'VCS'})" +
                    "  $$) as (v agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) " +
		    "WHERE s.gender='female' AND m.browserUsed = 'Firefox'  AND id(c) <844425000000000 AND t.language = 'ar'" +
                    " WITH DISTINCT c,m,s,t,e1,e2,e3 CREATE (c)-[rr1:VCS{coID:ID(e1)}]->(m)-[rr2:VCS{coID:ID(e2)}]->(s)-[rr3:VCS{coID:ID(e3)}]->(t)" +
                    "WITH c,rr1,rr2,rr3 SET rr2.prevID=ID(rr1),rr3.prevID=ID(rr2) "+
                    " $$) as (e1 agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) " +
		    "WHERE s.gender='female' AND m.browserUsed = 'Firefox'  AND id(c) <844425000000000 AND t.language = 'ar'" +
                    " WITH DISTINCT c " +
                    " MATCH (v:View {ViewName:'VCS'}) MERGE (v)-[:CONTAIN_NODE]->(c)"+
                    " $$) as (e1 agtype);");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void VCS_local(Statement stmt){
        try {
            int count = 0;

	    ResultSet    rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View {ViewName:'VCS'})-[:CONTAIN_NODE]->(c:Comment)-[rr1:VCS]->(m:Comment)-[rr2:VCS]->(s:Person)-[rr3:VCS]->(t:Post)" +
                    "WHERE ID(rr1)= rr2.prevID AND ID(rr2)= rr3.prevID "+
                    "WITH c,rr1.coID AS id1,m,rr2.coID AS id2,s,rr3.coID AS id3,t " +
                    "MATCH p = (c)-[e1:REPLY_OF]->(m)<-[e2:LIKES]-(s)<-[e3:HAS_CREATOR]-(t) " +
                    "WHERE  ID(e1)= id1 AND  ID(e2)= id2 AND  ID(e3)=id3 " +
                    "RETURN p $$) as (p agtype);");


            while (rs.next()) {
                // Returning Result as Agtype
                //System.out.println(rs.getObject(1, Agtype.class).getString());
                count++;

            }
            System.out.println("Found paths: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void VCL_declare(Statement stmt){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MERGE (v:View{ViewName:'VCL'})" +
                    "  $$) as (v agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) " +
                    "WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer'  AND id(c) <844425000000000 AND t.language = 'tk'" +
                    " WITH DISTINCT c,m,s,t,e1,e2,e3 CREATE (c)-[rr1:VCL{coID:ID(e1)}]->(m)-[rr2:VCL{coID:ID(e2)}]->(s)-[rr3:VCL{coID:ID(e3)}]->(t)" +
                    "WITH c,rr1,rr2,rr3 SET rr2.prevID=ID(rr1),rr3.prevID=ID(rr2) "+
                    " $$) as (e1 agtype);");

            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH(c:Comment)-[e1:REPLY_OF]->(m:Comment)<-[e2:LIKES]-(s:Person)<-[e3:HAS_CREATOR]-(t:Post) " +
                    "WHERE s.gender='male' AND m.browserUsed = 'Internet Explorer'  AND id(c) <844425000000000 AND t.language = 'tk'" +
                    " WITH DISTINCT c " +
                    " MATCH (v:View {ViewName:'VCL'}) MERGE (v)-[:CONTAIN_NODE]->(c)"+
                    " $$) as (e1 agtype);");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void VCL_local(Statement stmt){
        try {
            int count = 0;


            ResultSet    rs = stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View {ViewName:'VCL'})-[:CONTAIN_NODE]->(c:Comment)-[rr1:VCL]->(m:Comment)-[rr2:VCL]->(s:Person)-[rr3:VCL]->(t:Post)" +
                    "WHERE ID(rr1)= rr2.prevID AND ID(rr2)= rr3.prevID "+
		    "WITH c,rr1.coID AS id1,m,rr2.coID AS id2,s,rr3.coID AS id3,t " +
                    "MATCH p = (c)-[e1:REPLY_OF]->(m)<-[e2:LIKES]-(s)<-[e3:HAS_CREATOR]-(t) " +
		    "WHERE  ID(e1)= id1 AND  ID(e2)= id2 AND  ID(e3)=id3 " +
                    "RETURN p $$) as (p agtype);");

            while (rs.next()) {
                count++;
            }
            System.out.println("Found nodes: "+ count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanup(Statement stmt,String viewname){
        try {
            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH (v:View)-[e:CONTAIN_NODE]->(c)" +
                    " DETACH DELETE v$$) as (idc agtype);");


            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
                    "$$ MATCH p=()-[e:"+viewname+ "]->()" +
                    //"WITH e LIMIT 10000"+
                    " DELETE e $$) as (idc agtype);");

//            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
//                    "$$ MATCH (n) " +
//                    " WHERE n.VMS_1 IS NOT NUll REMOVE n.VMS_1 $$) as (idc agtype);");
//
//            stmt.executeQuery("SELECT * from cypher('ldbc01', " +
//                    "$$ MATCH (n) " +
//                    " WHERE n.VMS_2 IS NOT NUll REMOVE n.VMS_2 $$) as (idc agtype);");

            System.out.println("cleanup done ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
