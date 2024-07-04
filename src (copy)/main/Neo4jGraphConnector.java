package main;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import jess.*;
import org.neo4j.configuration.GraphDatabaseSettings;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.graphdb.*;

import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.Node;
import org.neo4j.io.fs.FileUtils;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

public class Neo4jGraphConnector {

    //NEO4J using enterprise edition 4.0.4 JARS (community will not work for multi databases)

    // Class Parameters
    public static GraphDatabaseService db;

    DatabaseManagementService databaseManagementService;

    //private File dbHomeBackup;

    // NOTE: This was previously static but I'm not sure of the reason
    public HashMap<String,List<Integer>> nodeSet = new HashMap<String,List<Integer>>();

    public Neo4jGraphConnector(String size) {

        try {
            // Getting the path od the input database
            String path = getDbPath(size);
            File dbHome = new File(path);

            File configFile = new File(path + "/conf/neo4j.conf");

            //dbHomeBackup = dbHome;

            // Start the embedded DBMS: instantiate a DatabaseManagementService and store the GraphDatabaseService instance in 'db'
            // The below lines is for the new version of neo4j (lib2 for Neo 5.3.0)
            databaseManagementService = new DatabaseManagementServiceBuilder(dbHome.toPath()).loadPropertiesFromFile(configFile.toPath()).build();

            db = databaseManagementService.database( DEFAULT_DATABASE_NAME );

            registerShutdownHook( databaseManagementService );


            // The below version is for the previous version of the Neo4j (lib for 4.4.0)
            /*databaseManagementService = new DatabaseManagementServiceBuilder(dbHome)
                .setConfig(GraphDatabaseSettings.default_database, "neo4j")
                .build();


            db = databaseManagementService.database("neo4j");*/

        } catch(Exception e) {
            System.out.println("Error in getting the databaseManagementService");
            e.printStackTrace();
        }

        System.out.println("neo4j graph connector set-up done.");

    }

    private static void registerShutdownHook( final DatabaseManagementService managementService )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                managementService.shutdown();
            }
        } );
    }

    public HashMap<String, List<Integer>> getNodeSet(){
        return nodeSet;
    }

    // Returning Object[]:
        // Index 0  -> Set<Relationship>, relationships of the path
        // Index 1 -> String path topology
    public Object[] pathQuery(String query){
        System.out.println("Executing query: " + query);

        HashMap<String,Set<Relationship>> retset = new HashMap<>();
        HashMap<String,List<List<Integer>>> pathRelationshipsSet= new HashMap<>();
        String tmp;
        Object[] returnObjects = new Object[2];

        try (Transaction tx = db.beginTx()) {
        	long start = System.currentTimeMillis();
            Result result = tx.execute(query);
        	long executeEnd = System.currentTimeMillis();
        	
        	int count = 0;
            while (result.hasNext()) {
                Map<String, Object> row = result.next();

                for (Map.Entry<String, Object> column : row.entrySet()) {
                	String pathName = column.getKey();
                    Path path = (Path)column.getValue();
                    if (path == null) continue;
                    List<List<Integer>> allEdges;
                    Iterable<Relationship> iterable = path.relationships();
                    if(pathRelationshipsSet.containsKey(pathName))
                    	allEdges = pathRelationshipsSet.get(pathName);
                    else
                    	allEdges = new ArrayList<>();
                    
	                Set<Relationship> relInOnePath;
	                if(retset.containsKey(pathName)  ) 
	                	relInOnePath = retset.get(pathName);
	                else
	                  	relInOnePath = new HashSet<>();
	                
	                List<Integer> EdgesInOnePath = new ArrayList<>();
	                for (Relationship rel : iterable) {
                    	count++;
                        tmp = rel.toString();
                        // TODO: Need to make sure this structure of rel.toString is always consistent
                        String edgeId = tmp.substring(tmp.indexOf(",") + 1, tmp.indexOf("]"));
                        EdgesInOnePath.add(Integer.parseInt(edgeId));
                        relInOnePath.add(rel);
                    }
	                allEdges.add(EdgesInOnePath);
	                pathRelationshipsSet.put(pathName, allEdges);
	                retset.put(pathName, relInOnePath);
                }

            }
            

        	long processPath = System.currentTimeMillis();

            // Nodes in relationshipNodes are stored in (static HashSet) nodeSet
            relationshipNodes(retset);

        	long processNode = System.currentTimeMillis();
        	System.out.println("execute:" + (executeEnd-start));
        	System.out.println("process Path:" + (processPath - executeEnd) + " num: " + count);
        	System.out.println("processNode" + (processNode-processPath));

            tx.commit();

        }

        System.out.println("Return set contains " + retset.size());
        for (Map.Entry<String,List<List<Integer>>> entry : pathRelationshipsSet.entrySet()) {
        	System.out.println("name:"+entry.getKey());
        	System.out.println("size:"+entry.getValue().size());
        }

        returnObjects[0] = retset;
        returnObjects[1] = pathRelationshipsSet;
        return returnObjects;
    }

    // Returns the edges/relationships of a path query
    public String getPathQueryRelationships(String query){
        StringBuilder nodeidsSB = new StringBuilder();
        StringBuilder rowsSB = new StringBuilder();

        String nodeids = "";
        String rows;

        if(query.equals("")) return nodeids ;

        System.out.println("Executing query: " + query + " in getPathQueryRelationships method");

        try( Transaction tx = db.beginTx()) {
            // Executing the query. Returns an iterable that contains the result set.
            Result result = tx.execute( query );
            while (result.hasNext())
            {
                Map<String, Object> row = result.next();

                for (Map.Entry<String, Object> column : row.entrySet())
                {
                    if (column.getKey().equals("path")){
                        String s = column.getValue().toString();
                        String[] slist = s.split(", |\\[|\\]");
                        String re = String.join("-", slist);
                        re = re.substring(1);
                        nodeidsSB.append(re);
                        nodeidsSB.append(" ");
                    } else {
                        rowsSB.append(column.getKey());
                        rowsSB.append(": ");
                        rowsSB.append(column.getValue());
                        rowsSB.append("; ");
                        rowsSB.append("\n");
                    }
                }
            }

            rows = rowsSB.toString();
            System.out.println(rows);
            tx.commit();
        } catch(Exception e) {
            System.out.println("An error occurred in executing the transaction to execute the query");
            e.printStackTrace();
        }

        nodeids = nodeidsSB.toString();

        System.out.println("Execution done");
        return nodeids.trim();

    }

    // Executes the query in a transaction on db:GraphDatabaseService
    public HashMap<String,List<Integer>> executeQuery(String query){

    	HashMap<String,List<Integer>> nodeids = new HashMap<String,List<Integer>>();

        if(query.equals("")) return nodeids ;

        // TODO: Commented by Mohanna to be able to do testing
        //if(query.contains("REMOVE") || query.contains("SET")) return nodeids; //todo remove when testing

        String rows = "";
        StringBuilder rowsSB = new StringBuilder();
        int numResults = 0;

        // Case when the result set is NODE objects
        if(query.contains("RETURN DISTINCT ID(")){
            try( Transaction tx = db.beginTx()) {
		//System.out.println("aaa");
                long startTime = System.currentTimeMillis();
                Result result = tx.execute( query );
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                System.out.println("Transaction execute: " + executionTime + " milliseconds");

                startTime = System.currentTimeMillis();
                while (result.hasNext())
                {
                    Map<String, Object> row = result.next();
                                        
                    for (Map.Entry<String, Object> column : row.entrySet())
                    {
                    	List<Integer> oneSet;
                    	if(!nodeids.containsKey(column.getKey())) 
                    		oneSet = new ArrayList<>();
                        else
                        	oneSet = nodeids.get(column.getKey());
                    	List<Integer> elementToAdd = Arrays.asList(Integer.parseInt(column.getValue().toString()));
                    	oneSet.add(Integer.parseInt(column.getValue().toString()));
                        nodeids.put(column.getKey(), oneSet);
                    }
                }
                //get rid of duplicates
                for(Map.Entry<String,List<Integer>> entry: nodeids.entrySet()) {
                	nodeids.put(entry.getKey(), new ArrayList<>(
                		      new HashSet<>(entry.getValue())));
                } 
                endTime = System.currentTimeMillis();
                executionTime = endTime - startTime;
                System.out.println("While Loop: " + executionTime + " milliseconds");

                startTime = System.currentTimeMillis();
                tx.commit();
                endTime = System.currentTimeMillis();
                executionTime = endTime - startTime;
                System.out.println("Transaction commit Time: " + executionTime + " milliseconds");
            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                e.printStackTrace();
            }

        }

        // Case when the result set is COUNT(NODES)
        else if(query.contains("RETURN COUNT")){
            try (Transaction tx = db.beginTx()) {
                Result result = tx.execute(query);

                while(result.hasNext()){
                    Map<String, Object> row = result.next();
                    for (Map.Entry<String, Object> column : row.entrySet()) {
                        rowsSB.append(column.getKey());
                        rowsSB.append(": ");
                        rowsSB.append(column.getValue());
                        rowsSB.append("; ");
                    }
                }

                rows = rowsSB.toString();
                System.out.println(rows);

                Iterator<Long> counts = result.columnAs("c");
                counts.forEachRemaining(System.out::println);
                System.out.println(counts);


            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                e.printStackTrace();
            }
        }

        // OTHER CASES
        else {
            try (Transaction tx = db.beginTx()) {
                long now = System.currentTimeMillis();

                // Start of the new method
                System.out.println("New method");
                List<String> IDLists = new ArrayList<>();
                Matcher m = java.util.regex.Pattern.compile("\\[([^\\[\\]:]+)]").matcher(query);

                while (m.find()) {
                	String content = m.group(1);
        	       	if(content.matches(".*[a-zA-Z].*"))
        	       		continue;
        	       	else
        	       		IDLists.add(content);
                }

                Map<String, Object> params = new HashMap<>();
                StringBuilder updatedQuery = new StringBuilder(query);
                String key;

                for (int i = 0; i < IDLists.size(); i++) {
                    key = "ids" + (i + 1);

                    List<Integer> intList = new ArrayList<>();
                    for (String s : IDLists.get(i).split(",\\s*")) {
                        intList.add(Integer.valueOf(s.stripLeading()));
                    }

                    params.put(key, intList);
                    updatedQuery = new StringBuilder(updatedQuery.toString().replace("[" + IDLists.get(i) + "]", "$"+key));
                }

                Result result = tx.execute(updatedQuery.toString(), params);
                // End of the new method
                
                //Result result = tx.execute(query);
                long duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to execute transaction");
                
                now = System.currentTimeMillis();
                while (result.hasNext())
                {
                    Map<String, Object> row = result.next();
                                        
                    for (Map.Entry<String, Object> column : row.entrySet())
                    {
                    	List<Integer> oneSet;
                    	if(!nodeids.containsKey(column.getKey())) 
                    		oneSet = new ArrayList<>();
                        else
                        	oneSet = nodeids.get(column.getKey());
                    	List<Integer> elementToAdd = Arrays.asList(Integer.parseInt(column.getValue().toString()));
                    	oneSet.add(Integer.parseInt(column.getValue().toString()));
                        nodeids.put(column.getKey(), oneSet);
                    }
                }
                //get rid of duplicates
                for(Map.Entry<String,List<Integer>> entry: nodeids.entrySet()) {
			System.out.println("size of "+ entry.getKey() + ":"+entry.getValue().size());
                	nodeids.put(entry.getKey(), new ArrayList<>(
                		      new HashSet<>(entry.getValue())));
                } 
                
                rows = rowsSB.toString();
                duration = System.currentTimeMillis() - now;

                System.out.println("There are " + numResults + " elements in the query");
                System.out.println("Took " + duration + " ms to process result rows");
                

                // profiling
                //profileResult(result);
                now = System.currentTimeMillis();
                tx.commit();
                duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to commit transaction");


            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                e.printStackTrace();
            }
        }

        System.out.println("Execution done");
        // To collect the row string builder that is huge
        //System.gc();
        return nodeids;
    }

    public static Result executeWithParam(String query, Map<String, Object> params,Transaction tx) {
    	Result  result = tx.execute(query, params);
    	return result;
    }
    
    public static Transaction getTransaction() {return db.beginTx();}
    
    public static List<List<Integer>> extractIDLists(String query) {
        List<List<Integer>> result = new ArrayList<>();

        int startPos = query.indexOf("ID(n) IN [");
        while (startPos != -1) {
            int endPos = query.indexOf("]", startPos);
            String IDList = query.substring(startPos + "ID(n) IN [".length(), endPos);
            List<Integer> ids = extractIDsFromString(IDList);
            result.add(ids);
            startPos = query.indexOf("ID(n) IN [", endPos);
        }

        return result;
    }

    public static List<Integer> extractIDsFromString(String IDList) {
        List<String> idList = Arrays.asList(IDList.split(","));
        List<Integer> result = new ArrayList<>(idList.size());

        for (String s : idList) {
            result.add(Integer.valueOf(s.strip()));
        }

        return result;
    }


	public void executeDirectly(String query, FileWriter myWriter) {
    	   try( Transaction tx = db.beginTx()) {
		//tx.execute("CREATE TEXT INDEX v1 FOR (n:User) on n.V1_1");
	    	long now = System.currentTimeMillis();
	        Result result = tx.execute( query );
		System.out.println("Took " + (System.currentTimeMillis()-now) + " ms to execute transaction");
		
		boolean process = true;
		Map<String, Object> row;
		int numResults = 0;
		HashMap<String,List<Integer>> nodeids = new HashMap<String,List<Integer>>();
		if(process){
		    while (result.hasNext()){
                        row = result.next();
                        numResults ++;
                     }
                 }

		//tx.execute("DROP INDEX v1");
		tx.commit();

	        long end = System.currentTimeMillis();
	        System.out.println("Took " + (end-now) + " ms to process the row");
		System.out.println("There are" + numResults + "elements");
		try{
			myWriter.write((end-now) + ",");
		} catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            	}

    	}
    }

    public int createViewOnGraph(String query) {
        Set<String> nodeids = new HashSet<String>();

        if(query.equals("")) return 0 ;

        String rows = "";
        StringBuilder rowsSB = new StringBuilder();
        int numResults = 0;

        // Case when the result set is NODE objects
        if(query.contains("RETURN DISTINCT ID(")){

            try( Transaction tx = db.beginTx()) {

                long now = System.currentTimeMillis();
                Result result = tx.execute( query );
                long duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to execute transaction");

                now = System.currentTimeMillis();
                while (result.hasNext())
                {
                    Map<String, Object> row = result.next();
                    for (Map.Entry<String, Object> column : row.entrySet())
                    {
                        numResults ++;
                    }
                }

                duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to count result rows");

                now = System.currentTimeMillis();
                tx.commit();
                duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to commit transaction");

            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                e.printStackTrace();
            }

        }

        // OTHER CASES (Path view case)
        else {
            try (Transaction tx = db.beginTx()) {
                long now = System.currentTimeMillis();
                Result result = tx.execute( query );
                long duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to execute transaction");
                
                now = System.currentTimeMillis();
                Map<String, Object> row;
                while (result.hasNext()) {
                    // Mohanna: Changed here to not have object initialization in every loop
                    row = result.next();
                    for (Map.Entry<String, Object> column : row.entrySet()) {
                        rowsSB.append(column.getKey() + ": " + column.getValue() + "; ");
                        numResults++;
                    }
                    rowsSB.append("\n");
                }
                rows = rowsSB.toString();
                duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to process result rows");
                
                now = System.currentTimeMillis();
                tx.commit();
                duration = System.currentTimeMillis() - now;
                System.out.println("Took " + duration + " ms to commit transaction");


            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                e.printStackTrace();
            }
        }

        System.out.println("Execution done");
        return numResults;
    }

    public void createIndex(String query) {
        try( Transaction tx = db.beginTx()) {
            Result result = tx.execute( query );
        
            while (result.hasNext())
            {
                Map<String, Object> row = result.next();

                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }

                System.out.println("**********");
            }
            tx.commit();
        } catch(Exception e) {
            System.out.println("An error occurred in executing the transaction to execute the query");
            System.out.println(e.getMessage());
        }
    }

    public void showIndexes () {

        try( Transaction tx = db.beginTx()) {
            Result result = tx.execute( "SHOW INDEXES" );
        
            while (result.hasNext())
            {
                Map<String, Object> row = result.next();

                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    System.out.println(entry.getKey() + ":" + entry.getValue());
                }

                System.out.println("**********");
            }
            tx.commit();
        } catch(Exception e) {
            System.out.println("An error occurred in executing the transaction to execute the query");
            System.out.println(e.getMessage());
        }

    }

    public void clearViewnameProperty() {

        // Removing viewname on nodes
        for (String nodeType : new String[]{"Tag", "User", "Post"}) {
            System.out.println("MATCH (n:" + nodeType +") REMOVE n.viewname RETURN n");

            try( Transaction tx = db.beginTx()) {
                Result result = tx.execute( "MATCH (n:" + nodeType +") REMOVE n.viewname RETURN n" );
                tx.commit();
            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        // Removing viewname on edges
        for (String edgeType : new String[]{"POSTED", "PARENT_OF", "HAS_TAG"}) {
            System.out.println("MATCH ()-[r:" + edgeType + "]-() REMOVE r.viewname RETURN r");
            
            try( Transaction tx = db.beginTx()) {
                Result result = tx.execute( "MATCH ()-[r:" + edgeType + "]-() REMOVE r.viewname RETURN r" );
                tx.commit();
            } catch(Exception e) {
                System.out.println("An error occurred in executing the transaction to execute the query");
                System.out.println(e.getMessage());
            }
        }
        
    }

    // Only used for measuring the baseline queries
    public int excuteBaselineQuery(String query) {

        Set<String> nodeids = new HashSet<String>();
        Set<String> edgeids = new HashSet<String>();
        Set<Relationship> retset = new HashSet<>();
        String tmp;

        if(query.equals("")) return 0 ;

        // Uncomment for later 
        System.out.println("Executing query: " + query);

        try( Transaction tx = db.beginTx()) {
            Result result = tx.execute( query );
        
            while (result.hasNext())
            {
                Map<String, Object> row = result.next();


                for (Map.Entry<String, Object> column : row.entrySet())
                {
                    // Check if it is a PATH query
                    try {
                        Path path = (Path)column.getValue();

                        Iterable<Relationship> edgeItr = path.relationships();
                        for (Relationship rel : edgeItr) {
                            tmp = rel.toString();
                            String edgeId = tmp.substring(tmp.indexOf(",") + 1, tmp.indexOf("]"));
                            System.out.println(tmp);
                            /*Added for debugging purposes */
                            /*String innerQuery = "MATCH ()-[r]-() WHERE ID(r) = " + edgeId + " RETURN r.viewname ";
                            try (Transaction innerTx = db.beginTx()) {
                                Result innerRes = innerTx.execute(innerQuery);
                                while (innerRes.hasNext()) {
                                    Map<String, Object> innerRow = innerRes.next();
                                    for (Map.Entry<String, Object> entry : innerRow.entrySet()) {
                                        if (!Objects.isNull(entry.getValue())) {
                                            if (entry.getKey().equals("r.viewname")){
                                                System.out.println(entry.getKey() + ":" + Arrays.toString((String[])entry.getValue()));
                                            } else {
                                                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
                                            }
                                        }
                                    }
                                    System.out.println("***********");
                                }
                            }*/
                            /*End of the added block */
                            edgeids.add(edgeId);
                        }

                        Iterable<Node> nodeItr = path.nodes();
                        for (Node node : nodeItr) {
                            tmp = node.toString();
                            String nodeId = tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]"));

                            /*Added for debugging purposes */
                            String innerQuery = "MATCH (n) WHERE ID(n) = " + nodeId + " RETURN n.userId, n.name ";
                            try (Transaction innerTx = db.beginTx()) {
                                Result innerRes = innerTx.execute(innerQuery);
                                while (innerRes.hasNext()) {
                                    Map<String, Object> innerRow = innerRes.next();
                                    for (Map.Entry<String, Object> entry : innerRow.entrySet()) {
                                        if (!Objects.isNull(entry.getValue())) {
                                            if (entry.getKey().equals("n.viewname")){
                                                System.out.println(entry.getKey() + ":" + Arrays.toString((String[])entry.getValue()));
                                            } else {
                                                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
                                            }
                                        }
                                    }
                                    System.out.println("***********");
                                }
                            }
                            /*End of the added block */
                            nodeids.add(nodeId);
                        }

                    } catch (Exception e) {
                        // Input is a NODE query
                        tmp = column.getValue().toString();
                        nodeids.add(tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")));
                    }

                }

            }
            tx.commit();
        } catch(Exception e) {
            System.out.println("An error occurred in executing the transaction to execute the query");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Execution done");
        System.out.println("Node size is " + nodeids.size());
        System.out.println("Edge size is " + edgeids.size());
        return nodeids.size() + edgeids.size();
    }

    // Only used for non-materialized queries 
    public int excuteBasicNoneMaterializedQuery(String query) {
        Set<String> nodeids = new HashSet<String>();
        Set<String> edgeids = new HashSet<String>();
        Set<Relationship> retset = new HashSet<>();
        String tmp;

        if(query.equals("")) return 0 ;

        System.out.println("Executing query: " + query);

        try( Transaction tx = db.beginTx()) {
            Result result = tx.execute( query );
            while (result.hasNext())
            {
                Map<String, Object> row = result.next();

                for (Map.Entry<String, Object> column : row.entrySet())
                {
                    tmp = column.getValue().toString();
                    //System.out.println(tmp);
                    if (tmp.startsWith("Node")) {
                        nodeids.add(tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")));
                    } else {
                        edgeids.add(tmp.substring(tmp.indexOf(",") + 1, tmp.indexOf("]")));
                    }
                }

            }
            tx.commit();
        } catch(Exception e) {
            System.out.println("An error occurred in executing the transaction to execute the query");
            e.printStackTrace();
        }

        System.out.println("Execution done");
        System.out.println("Node size is " + nodeids.size());
        System.out.println("Edge size is " + edgeids.size());
        return nodeids.size() + edgeids.size();
        
    }

    public void profileResult(Result result) {
        // Should write a recursive way to get all information of profiling
        ExecutionPlanDescription plan = result.getExecutionPlanDescription();
        System.out.println("Step Name : " + plan.getName());
        ExecutionPlanDescription.ProfilerStatistics	profiler = plan.getProfilerStatistics();
        System.out.println("DB hits : " + profiler.	getDbHits() + " Rows: " + profiler.getRows());
        
        // Printing the arguments
        for (Map.Entry<String, Object> entry : plan.getArguments().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("Start of Children Steps information  for step " + plan.getName());
        for (ExecutionPlanDescription child: plan.getChildren()) {
            // Goes all the way down the child-tree in this recursive function
            profileResultByPlan(child);
        }
        System.out.println("End of Children Steps information  for step " + plan.getName());
    }

    // A recursive function that outputs the profiling statistics of an execution plan and all its children
    public void profileResultByPlan(ExecutionPlanDescription plan) {
        // Should write a recursive way to get all information of profiling
        System.out.println("Step Name : " + plan.getName());
        ExecutionPlanDescription.ProfilerStatistics	profiler = plan.getProfilerStatistics();
        System.out.println("DB hits : " + profiler.	getDbHits() + " Rows: " + profiler.getRows());
        
        // Printing the arguments
        for (Map.Entry<String, Object> entry : plan.getArguments().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Recursion on children =
        System.out.println("Start of Children Steps information  for step " + plan.getName());
        for (ExecutionPlanDescription child: plan.getChildren()) {
            profileResultByPlan(child);
        }
        System.out.println("End of Children Steps information  for step " + plan.getName());
    }

    public static void counts(String q){

        try (Transaction tx = db.beginTx()) {
            Result result = tx.execute(q);

            List<String> columns = result.columns();
            Iterator<Long> counts = result.columnAs("c");
            System.out.println(counts.next());


        }
    }

    public void shutdown(){
        databaseManagementService.shutdown();
    }

    // Add the nodes involved in a set of Relationships to nodeSet for future reference
    public HashMap<String,List<Integer>> relationshipNodes( HashMap<String,Set<Relationship>> relationshipSet){

    	HashMap<String,List<Integer>> nodeids = new HashMap<String,List<Integer>>();
        for(Map.Entry<String,Set<Relationship>> entry : relationshipSet.entrySet()){
        	String pathName = entry.getKey();
        	Set<Relationship> relations = entry.getValue();
        	for(Relationship r : relations){
	            Node[] nodeSet = r.getNodes();
	            for (Node n : nodeSet){
	
	                // If nodeid already exists in nodeids(HashSet) the item is not inserted
	                List<Integer> nodeidPath; 
	                if(nodeids.containsKey(pathName))
	                	nodeidPath = nodeids.get(pathName);
	                else
	                	nodeidPath = new ArrayList<>();
	                nodeidPath.add((int)n.getId());
	                nodeids.put(pathName,nodeidPath);
	            }
        	}
        }

	for(Map.Entry<String,List<Integer>> entry: nodeids.entrySet()) {
            nodeids.put(entry.getKey(), new ArrayList<>(
                    new HashSet<>(entry.getValue())));
        }

        // update nodeSet to hold the current nodeids
        nodeSet = nodeids;
        return nodeids;
    }

    // Used to get the database path with the given size from the config file
    public String getDbPath(String size){
        String configPath = "./test/config";
        try {
            Scanner sc = new Scanner(new File(configPath));
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                if(line.startsWith(size.toLowerCase())){
                    return line.split("=")[1];
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }


    // For the Jess end [Not used in this version of the code]

    public static Set<JessNode> getNodes(String query, Rete engine) throws JessException {


        Set<JessNode> nodeSet = new HashSet<>();

        String after = (query.split(":")[1]);
        String label = after.split("\\)")[0];

        System.out.println("Label is " + label);

        NodeEnum lab = NodeEnum.User; //default
        if(label.equals("Post")) lab = NodeEnum.Post;
        else if (label.equals("Tag")) lab = NodeEnum.Tag;
        else if (label.equals("User"));
        else System.out.println("invalid label specified");


        try (Transaction tx = db.beginTx();
             Result result = tx.execute(query)) {
            while (result.hasNext()) {
                Map<String, Object> row = result.next();
                JessNode entry = new JessNode();

                //todo manual label set
                entry.setLabel(lab);




                for (Map.Entry<String, Object> column : row.entrySet()) {



                    if(column.getValue() == null) continue;

                    if((""+column.getKey()).equals("id")) {
                        entry.setId(Integer.parseInt(""+column.getValue()));
                        continue;
                    }

                    switch (lab){

                        case User:{

                            switch(""+column.getKey()) {
                                case "displayname": {
                                    entry.setDisplayname("" + column.getValue());
                                    break;
                                }
                                case "userId": {
                                    entry.setUserId(Integer.parseInt("" + column.getValue()));
                                    break;
                                }
                                case "aboutme": {
                                    entry.setAboutme("" + column.getValue());
                                    break;
                                }
                                case "reputation": {
                                    entry.setReputation(Integer.parseInt("" + column.getValue()));
                                    break;
                                }
                                case "upvotes": {
                                    entry.setUpvotes(Integer.parseInt("" + column.getValue()));
                                    break;
                                }
                                case "downvotes": {
                                    entry.setDownvotes(Integer.parseInt("" + column.getValue()));
                                    break;
                                }
                                case "views": { //doesn't work since the other middleware sets views to an array
                                    entry.setViews(Integer.parseInt("" + column.getValue()));
                                    break;
                                }
                                default:{
                                    System.out.println("Userdefaulted");
                                    break;
                                }
                            }

                            break;

                        }
                        case Post:{

                            switch(""+column.getKey()){

                                //Post type
                                case "body":{

                                    entry.setBody(""+column.getValue());
                                    break;
                                }
                                case "comments":{
                                    entry.setComments(Integer.parseInt(""+column.getValue()));
                                    break;
                                }
                                case "postId":{
                                    entry.setPostId(Integer.parseInt(""+column.getValue()));
                                    break;
                                }
                                case "score":{
                                    entry.setScore(Integer.parseInt(""+column.getValue()));
                                    break;
                                }
                                case "tagId":{
                                    entry.setTagId((""+column.getValue()));
                                    break;
                                }
                                default:{
                                    System.out.println("Postdefaulted");
                                    break;
                                }
                            }

                            break;

                        }
                        case Tag:{
                            if ((""+column.getKey()).equals("tagId")){
                                entry.setTagId(""+column.getValue());
                            }

                            break;

                        }

                    }


                }

                engine.add(entry);
//                nodeSet.add(entry);

            }
        }
        return nodeSet;
    }

    public static Set<JessEdge> getEdges(String query, Rete engine) throws JessException {


        Set<JessEdge> edgeSet = new HashSet<>();

        String after = (query.split(":")[1]);
        String label = after.split("]")[0];

        System.out.println("Label is " + label);

        EdgeEnum lab = EdgeEnum.POSTED; //default
        if(label.equals("PARENT_OF")) lab = EdgeEnum.PARENT_OF;
        else if (label.equals("HAS_TAG")) lab = EdgeEnum.HAS_TAG;
        else if (label.equals("POSTED"));
        else System.out.println("invalid label found");


        try (Transaction tx = db.beginTx();
             Result result = tx.execute(query)) {
            while (result.hasNext()) {


                Map<String, Object> row = result.next();
                JessEdge entry = new JessEdge();


//                System.out.println(row.toString());

                //todo manual label set
                entry.setLabel(lab);



                for (Map.Entry<String, Object> column : row.entrySet()) {

                    if(column.getValue() == null) continue;

                    boolean flag = true;



                    switch(""+column.getKey()) {
                        case "identity": {
                            entry.setIdentity(Integer.parseInt(""+ column.getValue()));
//                            if(Integer.parseInt(""+column.getValue())!=191317 && Integer.parseInt(""+column.getValue())!=1950652) flag = false;
                            break;
                        }
                        case "start": {
                            entry.setStart(Integer.parseInt("" + column.getValue()));
                            break;
                        }
                        case "end": {
                            entry.setEnd(Integer.parseInt(""+ column.getValue()));;
                            break;
                        }
                        default:{
                            System.out.println("defaulted");
                            break;
                        }
                    }



                }

                engine.add(entry);
//                edgeSet.add(entry);

            }
        }
        return edgeSet;
    }


    public static void main(String[] args){

        //main method not used, only for testing purposes

        Neo4jGraphConnector connector  = new Neo4jGraphConnector("small");

        test("MATCH(n:User) where n.reputation>100 return n");

        connector.shutdown();

    }

    public static void test(String query){

        String rows = "";

        long now = System.currentTimeMillis();


            try( Transaction tx = db.beginTx()){
                Result result = tx.execute( query );
                while (result.hasNext())
                {
//                    Map<String, Object> row = result.next();
//                    for (Map.Entry<String, Object> column : row.entrySet())
//                    {
//                        rows += column.getKey() + ": " + column.getValue() + "; ";
//                    }


                }
                tx.commit();
            }

        long nownow = System.currentTimeMillis();
        System.out.println("Execution done: total time = " + (nownow -now));

    }

}

