package jess;

import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import jess.*;
import org.neo4j.configuration.GraphDatabaseSettings;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.database.Database;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import main.*;

public class GraphEngine {

    private Rete engine;
    private WorkingMemoryMarker marker;
    private Database database;

    //temp hardcoded size db, this file is not used
    private Neo4jGraphConnector connector = new Neo4jGraphConnector("small");

    private static Map<String, Set<JessPath>> viewMap = new ConcurrentHashMap<>();


    public GraphEngine(Database db) throws JessException {
        database = db;
        engine = new Rete();
        engine.reset();
        engine.batch("/jess/graph.clp");

        //Load all info from database
//        engine.addAll(database.);

    }

    public void maintainTable(){

    }





    public static void main(String[] args){

        //temp hardcoded size db, this file is not used
        Neo4jGraphConnector connector = new Neo4jGraphConnector("small");

        viewMap.put("view100Rep", new HashSet<JessPath>());
        viewMap.put("two-hops-posts", new HashSet<JessPath>());


        try {

            Rete engine = new Rete();
            engine.reset();
            engine.batch("jess/views.clp");

            engine.store("view100Rep", viewMap.get("view100Rep"));
            engine.store("two-hops-posts", viewMap.get("two-hops-posts"));








            //engine.eval("(defrule v2 (test (eq 1 1)) => (printout t crlf) )"); //how to add a rule



//
//            JessNode exampleNode = new JessNode();
//            exampleNode.setDisplayname("Joe");
//            exampleNode.setLabel(jess.NodeEnum.User);
//            exampleNode.setUserId(3);
//            exampleNode.setAboutme("I'm a man!");
//            exampleNode.setReputation(101);

            //put node in a table that maps fact id to node id?
            long now = System.currentTimeMillis();

//            Collection userCol =
                    connector.getNodes("MATCH (n:User) RETURN id(n) as id, n.displayname as displayname, " +
                    "n.upvotes as upvotes, n.downvotes as downvotes, n.aboutme as aboutme, n.reputation as reputation, n.userId as userId", engine);

//            Collection col = getNodes("MATCH (n:User) RETURN properties(n) LIMIT 25");


//            Collection postCol =
              connector.getNodes("MATCH (n:Post) RETURN id(n) as id, n.postId as postId, n.comments as comments, " +
                    "n.score as score, n.body as body", engine);

//            Collection tagCol =
                    connector.getNodes("MATCH (n:Tag) RETURN n.tagId as tagId, id(n) as id", engine);

//            engine.addAll(userCol);
//            engine.addAll(postCol);
//            engine.addAll(tagCol);

//            Collection pCol =
                    connector.getEdges("MATCH (q)-[r:POSTED]->(p) return id(q) as start, id(p) as end, id(r) as identity", engine);
//            Collection htCol =
                    connector.getEdges("MATCH (q)-[r:HAS_TAG]->(p) return id(q) as start, id(p) as end, id(r) as identity", engine);
//            Collection poCol =
                    connector.getEdges("MATCH (q)-[r:PARENT_OF]->(p) return id(q) as start, id(p) as end, id(r) as identity", engine);


//            engine.addAll(pCol);
//            engine.addAll(htCol);
//            engine.addAll(poCol);


            connector.shutdown();
            System.out.println("Done adding nodes, adding and then evaluating rule...");

            engine.run();

            long here = System.currentTimeMillis();

            engine.eval("(defrule two-hops-posts\n" +
                    "\"All posts that are children of posts by users with >500,000 views on their profile\"\n" +
                    "(logical\n" +
                    "    ?x <- (JessNode (id ?i1) (label ?a&:(eq (?a toString) \"User\")) (reputation ?b&:(> ?b 500000)))\n"+
                    "    ?y <- (JessNode (id ?i2) (label ?d&:(eq (?d toString) \"Post\")))\n" +
                    "    ?z <- (JessEdge (start ?s&:(eq ?s ?i1)) (end ?e&:(eq ?e ?i2)) (label ?lab&:(eq (?lab toString) \"POSTED\")))\n" +
                    "    ?w <- (JessNode (id ?i3) (label ?c&:(eq (?c toString) \"Post\")))\n" +
                    "    ?u <- (JessEdge (start ?s2&:(eq ?s2 ?i3)) (end ?e2&:(eq ?e2 ?i2)) (label ?lab2&:(eq (?lab2 toString) \"PARENT_OF\")))\n" +
                    ")" + "=>\n" +
                            "    (bind ?path (new JessPath ))\n" +
                            "    (?path addNode ?x)\n" +
                            "    (?path addNode ?y)\n" +
                            "    (?path addNode ?w)\n" +
                            "\n" +
                            "    (?path addEdge ?z)\n" +
                            "    (?path addEdge ?u)\n" +
                            "\n" +
                            "    (bind ?list (fetch two-hops-posts))\n" +
                            "    (add ?path)\n" +
                            "    (?list add ?path)\n" +
                            ")"
                    );

            long end = System.currentTimeMillis();

            System.out.println("To add all: " + (here - now) + "\nTo match rules:" + (end-here));

//            System.out.println("Number of facts added: " + col.size());

            System.out.println("Size of that list is " + viewMap.get("two-hops-posts").size());

            Iterator<JessPath> it = viewMap.get("two-hops-posts").iterator();
            while(it.hasNext()){

                ArrayList<JessNode> totalList = new ArrayList<>();

                JessPath jp = it.next();
                ArrayList<JessNode> list = jp.getNodes();

                for(JessNode jn : list){
                    totalList.add(jn);
                }


            }

            long endend = System.currentTimeMillis();

            System.out.println("endend-end" + (endend - end));

            JessNode dummy = new JessNode();
            dummy.setLabel(NodeEnum.User);
            dummy.setDisplayname("joeeee");
            dummy.setAboutme("im joe!");
            dummy.setUpvotes(3);
            dummy.setDownvotes(4);
            dummy.setReputation(33);
            dummy.setViews(500000);

            engine.add(dummy);

            long endendend = System.currentTimeMillis();
            System.out.println("to add a single element to graph : " + (endendend-endend));

            JessNode dummy2 = new JessNode();
            dummy2.setLabel(NodeEnum.User);
            dummy2.setDisplayname("joeeee");
            dummy2.setAboutme("im joe!");
            dummy2.setUpvotes(3);
            dummy2.setDownvotes(4);
            dummy2.setReputation(333333333);
            dummy2.setViews(500000);

            engine.add(dummy);
           // Fact f = engine.getShadowFactForObject(jp);

//            System.out.println("1: " + jp.toString());
//            System.out.println("before: " + f.getFactId());



//            Fact f = engine.getShadowFactForObject(jp); //blegh
//
//            System.out.println("2: " + jp.toString());
//            System.out.println("after: " + f.getFactId());
//            engine.batch("jess/scr.clp");
//
////
//            Iterator it = engine.listFacts();
//            while(it.hasNext()){
//                System.out.println(it.next().toString());
//            }
//
//            it = engine.listDefrules();
//            while(it.hasNext()){
//                System.out.println(it.next().toString());
//            }
        }
        catch(JessException e){
            System.out.println (e);
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
            connector.shutdown();
        }
    }
}
