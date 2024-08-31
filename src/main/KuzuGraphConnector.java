package main;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;
import java.util.regex.Pattern;

import com.kuzudb.*;

import jess.*;

public class KuzuGraphConnector {
    public HashMap<String, HashMap<String, List<Integer>>> nodeSet = new HashMap<>();
    public HashMap<String, HashMap<String, List<Integer>>> edgeSet = new HashMap<>();


    private String db_path = "./so_coffee_db"; //path to my local DB
    private KuzuDatabase db ;//= new KuzuDatabase(db_path);
    private static KuzuConnection conn;

    public KuzuGraphConnector() {
        db = new KuzuDatabase(db_path);
        conn = new KuzuConnection(db);
    }

    public KuzuGraphConnector(String db_path) {
        this.db_path = db_path;
        this.db = new KuzuDatabase(db_path);
        conn = new KuzuConnection(db);
    }

    public static void main(String[] args) {

    }

    private static void registerShutdownHook (final KuzuConnection conn) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                conn.interrupt();
                conn.destroy();
            } catch (KuzuObjectRefDestroyedException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public HashMap<String, HashMap<String, List<Integer>>> getNodeSet(){
        return nodeSet;
    }

    public HashMap<String, HashMap<String, List<Integer>>> getEdgeSet(){
        return edgeSet;
    }

    public HashMap<String,List<List<String>>> pathQuery(String query) throws KuzuObjectRefDestroyedException {
        System.out.println("Executing query: " + query);

        HashMap<String,List<List<String>>> pathRelationshipsSet= new HashMap<>();
        Object[] returnObjects = new Object[2];

        long start = System.currentTimeMillis();

        KuzuQueryResult result = conn.query(query);
        long executeEnd = System.currentTimeMillis();
        long numColumns = result.getNumColumns();
        int count = 0;

        // nodes
        HashMap<String,HashMap<String, Set<Integer>>> nodeIds = new HashMap<>();
        HashMap<String, HashMap<String, Set<Integer>>> edgeIds = new HashMap<>();
        String pathName;

        while(result.hasNext()) {
            KuzuFlatTuple row = result.getNext();
            // format of row is:
            // [list of NODES_LABELS], [list of NODES_IDS], [list of RELS_LABELS], [list of RELS_IDS]
            // with labels & ids corresponding
            int i = 0;
            while (i < numColumns) {
                // colName of format <pathName>_<id/label>
                pathName = result.getColumnName(i).split("_")[0];
                // node part of path
                if (i % 4 == 0) {
                    if (!nodeIds.containsKey(pathName)) {
                        nodeIds.put(pathName, new HashMap<>());
                    }
                    // iterate through node labels
                    for (int j = 0; j < KuzuValueListUtil.getListSize(row.getValue(i)); j++) {
                        String nodeLabel = KuzuValueListUtil.getListElement(row.getValue(i), j).toString();
                        if (!nodeIds.get(pathName).containsKey(nodeLabel)) {
                            nodeIds.get(pathName).put(nodeLabel, new HashSet<>());
                        }
                        // add corresponding node offset at i+1
                        if (KuzuValueListUtil.getListElement(row.getValue(i+1), j).toString().length() > 0) {
                            nodeIds.get(pathName).get(nodeLabel).add(
                                    Integer.parseInt(
                                            KuzuValueListUtil.getListElement(row.getValue(i + 1), j).toString().split(":")[1]
                                    )
                            );
                        }
                    }
                } else {
                    if (!pathRelationshipsSet.containsKey(pathName)) {
                        pathRelationshipsSet.put(pathName, new ArrayList<>());
                        edgeIds.put(pathName, new HashMap<>());
                    }
                    List<String> edgesInOnePath = new ArrayList<>();
                    for (int j = 0; j < KuzuValueListUtil.getListSize(row.getValue(i)); j++) {
                        String edgeLabel = KuzuValueListUtil.getListElement(row.getValue(i), j).toString();
                        if (!edgeIds.get(pathName).containsKey(edgeLabel)) {
                            edgeIds.get(pathName).put(edgeLabel, new HashSet<>());
                        }

                        if (KuzuValueListUtil.getListElement(row.getValue(i+1), j).toString().length() > 0) {
                            edgeIds.get(pathName).get(edgeLabel).add(
                                    Integer.parseInt(
                                            KuzuValueListUtil.getListElement(row.getValue(i + 1), j).toString().split(":")[1]
                                    )
                            );
                        }

                        count++;
                        edgesInOnePath.add(KuzuValueListUtil.getListElement(row.getValue(i+1), j).toString());
                    }
                    pathRelationshipsSet.get(pathName).add(edgesInOnePath);
                }
                i = i+2;
            }
        }

        long processPath = System.currentTimeMillis();
        // Nodes in relationshipNodes are stored in (static HashSet) nodeSet
        nodeSet = new HashMap<>();

        for (String path : nodeIds.keySet()) {
            HashMap<String, List<Integer>> subset = new HashMap<>();
            for (String table : nodeIds.get(path).keySet()) {
                subset.put(table, new ArrayList<>(nodeIds.get(path).get(table)));
            }
            nodeSet.put(path, subset);
        }

        edgeSet = new HashMap<>();
        for (String path : edgeIds.keySet()) {
            HashMap<String, List<Integer>> subset = new HashMap<>();
            for (String table : edgeIds.get(path).keySet()) {
                subset.put(table, new ArrayList<>(edgeIds.get(path).get(table)));
            }
            edgeSet.put(path, subset);
            subset.clear();
        }

        System.out.println("execute:" + (executeEnd-start) + "ms");
        System.out.println("process Path:" + (processPath - executeEnd) + "ms, num: " + count);

        /*System.out.println("Return set contains " + retset.size());
        for (Map.Entry<String,Set<KuzuValue>> entry : retset.entrySet()) {
            System.out.println("name:"+entry.getKey());
            System.out.println("size:"+entry.getValue().size());
        }
        */
        return pathRelationshipsSet;
    }

    // done
    public void shutdown() throws KuzuObjectRefDestroyedException {
        conn.destroy();
    }


    /**
     * Executes query and returns ids of nodes in return set
     * @param query query to execute
     * @return ids of nodes in return set
     * @throws KuzuObjectRefDestroyedException thrown by kuzu query execute
     */
    public HashMap<String,HashMap<String, List<Integer>>> executeQuery(String query) throws KuzuObjectRefDestroyedException {
        // nodeIds shape: node name -> table name -> [list of id offsets]
        HashMap<String,HashMap<String, Set<Integer>>> nodeIds = new HashMap<>();

        if (query.equals("")) return new HashMap<>();

        //if(query.contains("REMOVE") || query.contains("SET")) return nodeids; //todo remove when testing

        StringBuilder rowsSB = new StringBuilder();
        long numResults = 0;
        long startTime; long executionTime;
        // Case when the result set is NODE objects
        if (query.contains("RETURN DISTINCT LABEL(") && query.contains("OFFSET(ID(")) {
            KuzuQueryResult result = conn.query(query);
            System.out.println("Transaction execute: " + result.getQuerySummary().getExecutionTime() + " milliseconds");

            startTime = System.currentTimeMillis();

            long numColumns = result.getNumColumns();
            HashMap<String, Set<Integer>> nodeidsPerTable = new HashMap<>();
            Set<Integer> oneSet;
            String nodeName = ""; String labelName;
            ArrayList<String> nodeNames = new ArrayList<>();
            for (int col = 0; col < numColumns; col++) {
                nodeNames.add(result.getColumnName(col).split("_")[0]);
                col += 1;
            }

            while (result.hasNext()) {
                KuzuFlatTuple row = result.getNext();
                int i = 0;

                while (i < numColumns) {
                    // Extract node name directly from column name if possible
                    // Otherwise, you may need to keep a separate list of node names
                    nodeName = nodeNames.get(i/2);
                    // Retrieve or create the set for the current label
                    labelName = row.getValue(i).toString();
                    /*oneSet = nodeidsPerTable.computeIfAbsent(labelName, k -> new HashSet<>());
                    // Add the value to the set
                    oneSet.add(Integer.parseInt(row.getValue(i + 1).toString()));*/
                    // Move to the next label-offset pair
                    // Update nodeIds after processing all label-offset pairs in the row

                    if (!nodeIds.containsKey(nodeName)) {
                        nodeIds.put(nodeName, new HashMap<>());
                    }
                    if (!nodeIds.get(nodeName).containsKey(labelName)) {
                        nodeIds.get(nodeName).put(labelName, new HashSet<>());
                    }
                    nodeIds.get(nodeName).get(labelName).add(Integer.parseInt(row.getValue(i + 1).toString()));
                    i += 2;
                }


            }
            executionTime = System.currentTimeMillis() - startTime;
            System.out.println("While Loop: " + executionTime + " milliseconds");

            startTime = System.currentTimeMillis();

            executionTime = System.currentTimeMillis() - startTime;
            System.out.println("Transaction commit Time: " + executionTime + " milliseconds");
        }

        // Case when the result set is COUNT(NODES)
        else if (query.contains("RETURN COUNT")) {
            KuzuQueryResult result = conn.query(query);
            long numColumns = result.getNumColumns();
            long countIndex = findColumnIndex(result, "c");
            List<Long> counts = new ArrayList<>();
            while (result.hasNext()) {
                KuzuFlatTuple row = result.getNext();
                for (int i = 0; i < numColumns; i++) {
                    rowsSB.append(result.getColumnName(i));
                    rowsSB.append(": ");
                    rowsSB.append(row.getValue(i));
                    rowsSB.append("; ");
                }
                counts.add(Long.parseLong(row.getValue(countIndex).toString()));
            }
            System.out.println(rowsSB);
            counts.forEach(System.out::println);
            System.out.println(counts);
        }

        // OTHER CASES
        /*else {
            conn.beginWriteTransaction();
            long now = System.currentTimeMillis();

            // Start of the new method
            System.out.println("New method");
            List<String> IDLists = new ArrayList<>();
            Matcher m = Pattern.compile("\\[([^\\[\\]:]+)]").matcher(query);

            while (m.find()) {
                String content = m.group(1);
                if (!content.matches(".*[a-zA-Z].*")) {
                    IDLists.add(content);
                }
            }

            Map<String, KuzuValue> params = new HashMap<>();
            StringBuilder updatedQuery = new StringBuilder(query);
            String key;

            for (int i = 0; i < IDLists.size(); i++) {
                key = "ids" + (i + 1);

                List<Integer> intList = new ArrayList<>();
                for (String s : IDLists.get(i).split(",\\s*")) {
                    intList.add(Integer.valueOf(s.stripLeading()));
                }

                // TODO - debug
                params.put(key, new KuzuValue(intList));
                updatedQuery = new StringBuilder(updatedQuery.toString().replace("[" + IDLists.get(i) + "]", "$" + key));
            }
            KuzuPreparedStatement preparedStatement = conn.prepare(String.valueOf(updatedQuery));

            KuzuQueryResult result = conn.execute(preparedStatement, params);
            // End of the new method

            long duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to execute transaction");

            now = System.currentTimeMillis();
            long numColumns = result.getNumColumns();
            while (result.hasNext())
            {
                KuzuFlatTuple row = result.getNext();
                // TODO check
                for (long i = 0; i<numColumns; i++) {
                    List<String> oneSet;
                    String colName = result.getColumnName(i);
                    KuzuInternalID value = KuzuValueNodeUtil.getID(row.getValue(i));
                    if(!nodeIds.containsKey(colName)) {
                        oneSet = new ArrayList<>();
                    } else {
                        oneSet = nodeIds.get(colName);
                    }
                    oneSet.add(internalIdToString(value));
                    nodeIds.put(colName, oneSet);
                }
            }
            //get rid of duplicates
            for(Map.Entry<String,List<String>> entry: nodeIds.entrySet()) {
                nodeIds.put(entry.getKey(), new ArrayList<>(
                        new HashSet<>(entry.getValue())));
            }
            //numResults = result.getNumTuples();
            duration = System.currentTimeMillis() - now;

            System.out.println("There are " + numResults + " elements in the query");
            System.out.println("Took " + duration + " ms to process result rows");

            now = System.currentTimeMillis();
            conn.commit();
            duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to commit transaction");
        }*/

        System.out.println("Execution done");
        // To collect the row string builder that is huge
        //System.gc();
        // remove duplicates
        // TODO: make prettier
        HashMap<String,HashMap<String, List<Integer>>> nodeIdsList = new HashMap<> ();
        for (Map.Entry<String, HashMap<String, Set<Integer>>> outerEntry : nodeIds.entrySet()) {
            String outerKey = outerEntry.getKey();
            HashMap<String, List<Integer>> innerMap = new HashMap<>();

            for (Map.Entry<String, Set<Integer>> innerEntry : outerEntry.getValue().entrySet()) {
                String innerKey = innerEntry.getKey();
                Set<Integer> originalSet = innerEntry.getValue();
                List<Integer> list = new ArrayList<>(originalSet);
                innerMap.put(innerKey, list);
            }

            nodeIdsList.put(outerKey, innerMap);
        }
        return nodeIdsList;
    }

    /**
     * Method to execute a "parametrized" query
     * Replaces the parameters with the string values in the params Map if appropriate
     * @param query query to execute with parameters
     * @param params map indexed by name of parameter with their values
     * @return result of the query
     * @throws KuzuObjectRefDestroyedException if exception during execution
     */
    public static KuzuQueryResult executeWithParam(String query, Map<String, String> params) throws KuzuObjectRefDestroyedException {
        for (String key : params.keySet()) {
            int index = query.indexOf("$"+key);
            if (index == -1) {
                continue;
            }

            String value = params.get(key);
            query = query.replace("$"+key, value);
        }
        long nowstart = System.currentTimeMillis();
        KuzuQueryResult result =   conn.query(query);
        if (!result.isSuccess()) {
            System.out.println(result.getErrorMessage());
        }
        System.out.println("Took " + (System.currentTimeMillis() - nowstart) + " ms to execute transaction");
        return result;
    }

    // nb: wondering if there is a use for this as it doesn't return a Transaction (unlike Neo4j)
    public static void getTransaction() throws KuzuObjectRefDestroyedException {
    }

    // todo: check
    public static List<List<Integer>> extractIDLists(String query) {
        List<List<Integer>> result = new ArrayList<>();
        int startPos = query.indexOf("list_contains([");
        while (startPos != -1) {
            int endPos = query.indexOf("], ID(n))", startPos);
            String IDList = query.substring(startPos + "list_contains([".length(), endPos);
            List<Integer> ids = extractIDsFromString(IDList);
            result.add(ids);
            startPos = query.indexOf("list_contains([", endPos);
        }

        return result;
    }

    public static List<Integer> extractIDsFromString(String IDList) {
        // TODO - might have to change to KuzuInternalID
        List<String> idList = Arrays.asList(IDList.split(","));
        List<Integer> result = new ArrayList<>(idList.size());

        for (String s : idList) {
            result.add(Integer.valueOf(s.strip()));
        }

        return result;
    }

    // done
    public void executeDirectly(String query) throws KuzuObjectRefDestroyedException {
        KuzuQueryResult r = conn.query( query );
        System.out.println("Took " + r.getQuerySummary().getCompilingTime() + " ms to compile");
        System.out.println("Took " + r.getQuerySummary().getExecutionTime() + " ms to execute transaction");

    }

    public void executeProfileQuery(String query) throws KuzuObjectRefDestroyedException {
        KuzuQueryResult r = conn.query(query);
        System.out.println("Took " + r.getQuerySummary().getExecutionTime());
        System.out.println(r.getNext().getValue(0));
    }

    // done
    public long createViewOnGraph(String query) throws KuzuObjectRefDestroyedException {
        String rows = "";
        if(query.equals("")) return 0 ;

        StringBuilder rowsSB = new StringBuilder();
        long numResults = 0;

        // Case when the result set is NODE objects
        if(query.contains("RETURN DISTINCT ID(")){
            long now = System.currentTimeMillis();
            KuzuQueryResult result = conn.query( query );
            long duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to execute transaction");

            now = System.currentTimeMillis();
            /*while (result.hasNext())
            {
                KuzuFlatTuple row = result.getNext();
                for (Map.Entry<String, Object> column : row.entrySet())
                {
                    numResults ++;
                }
            }*/
            numResults = result.getNumColumns() * result.getNumTuples();
            result.destroy();
            duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to count result rows");

            now = System.currentTimeMillis();
            duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to commit transaction");
        }

        // OTHER CASES (Path view case)
        else {
            long now = System.currentTimeMillis();
            KuzuQueryResult result = conn.query( query );
            long duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to execute transaction");

            now = System.currentTimeMillis();

            // TODO : check if need to print result?
            /*Map<String, Object> row;
            while (result.hasNext()) {
                // Mohanna: Changed here to not have object initialization in every loop
                row = result.next();
                for (Map.Entry<String, Object> column : row.entrySet()) {
                    rowsSB.append(column.getKey() + ": " + column.getValue() + "; ");
                    numResults++;
                }
                rowsSB.append("\n");
            }
            rows = rowsSB.toString();*/
            numResults = result.getNumColumns() * result.getNumTuples();
            duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to process result rows");
            result.destroy();
            now = System.currentTimeMillis();
            duration = System.currentTimeMillis() - now;
            System.out.println("Took " + duration + " ms to commit transaction");

        }

        System.out.println("Execution done");
        return numResults;
    }

    // done
    public void clearViewnameProperty() throws KuzuObjectRefDestroyedException{
        // Remove viewname property from tables instead of iterating through nodes & removing property
        for (String nodeType : new String[]{"Tag", "User", "Post", "POSTED", "PARENT_OF", "HAS_TAG"}) {
            String query ="ALTER TABLE " + nodeType + "DROP viewname";
            System.out.println("ALTER TABLE " + nodeType + "DROP viewname");
            conn.query(query);
        }
    }

    public static String internalIdToString(KuzuInternalID id) {
        return id.tableId + ":" + id.offset;
    }

    public long executeBaselineQuery(String query) throws KuzuObjectRefDestroyedException {

        Set<String> nodeids = new HashSet<>();
        Set<String> edgeids = new HashSet<>();
        Set<KuzuValue> retset = new HashSet<>();
        String tmp;

        if(query.equals("")) return 0 ;

        // Uncomment for later
        System.out.println("Executing query: " + query);

        KuzuQueryResult result = conn.query(query);
        long numColumns = result.getNumColumns();
        System.out.println("Baseline transaction execution: " + result.getQuerySummary().getExecutionTime());
        // processing removed because all results already loaded in 'result'
        /*while (result.hasNext()) {
            KuzuFlatTuple row = result.getNext();
            for (long i = 0; i < numColumns; i++) {
                // check if path
                // Assume paths are stored as recursive_rels
                KuzuValue val = row.getValue(i);

                if (val.getDataType().getID() == KuzuDataTypeID.RECURSIVE_REL) {
                    KuzuValue rels = KuzuValueRecursiveRelUtil.getRelList(val);
                    for( int j = 0; j < KuzuValueListUtil.getListSize(rels); j++) {
                        KuzuValue rel = KuzuValueListUtil.getListElement(rels, j);
                        tmp = rel.toString();
                        int index = tmp.indexOf(",", tmp.indexOf("_ID") + 1);;
                        if (index == -1)
                            index = tmp.indexOf("}", tmp.indexOf("_ID") + 1);
                        String edgeId = tmp.substring(tmp.indexOf("_ID") + 5, index);
                        edgeids.add(edgeId);
                    }

                    KuzuValue nodes = KuzuValueRecursiveRelUtil.getNodeList(val);
                    for( int j = 0; j < KuzuValueListUtil.getListSize(nodes); j++) {
                        KuzuValue node = KuzuValueListUtil.getListElement(nodes, j);
                        KuzuInternalID id = KuzuValueNodeUtil.getID(node);
                        String nodeId = internalIdToString(id);
                        nodeids.add(nodeId);
                    }
                } else if (val.getDataType().getID() ==  KuzuDataTypeID.NODE){
                    KuzuInternalID id = KuzuValueNodeUtil.getID(val);
                    String nodeId = internalIdToString(id);
                    nodeids.add(nodeId);
                }
            }
        }*/
        return result.getNumTuples();
    }

    // Only used for non-materialized queries
    public int executeBasicNonMaterializedQuery(String query) throws KuzuObjectRefDestroyedException {
        Set<String> nodeids = new HashSet<>();
        Set<String> edgeids = new HashSet<>();
        String tmp;

        if(query.equals("")) return 0 ;

        System.out.println("Executing query: " + query);

        KuzuQueryResult result = conn.query(query);

        while (result.hasNext())
        {
            KuzuFlatTuple row = result.getNext();

            for (int i = 0; i < result.getNumColumns(); i++)
            {
                tmp = row.getValue(i).toString();
                //System.out.println(tmp);
                // TODO: change matching
                if (tmp.startsWith("Node")) {
                    nodeids.add(tmp.substring(tmp.indexOf("[") + 1, tmp.indexOf("]")));
                } else {
                    edgeids.add(tmp.substring(tmp.indexOf(",") + 1, tmp.indexOf("]")));
                }
            }
        }

        System.out.println("Execution done");
        System.out.println("Node size is " + nodeids.size());
        System.out.println("Edge size is " + edgeids.size());
        return nodeids.size() + edgeids.size();

    }


    // done
    public static long findColumnIndex(KuzuQueryResult result, String colName) throws KuzuObjectRefDestroyedException {
        for (long i = 0; i < result.getNumColumns(); i++) {
            if (result.getColumnName(i).equals(colName)) {
                return i;
            }
        }
        return -1;
    }

    // done
    public static void counts(String q) throws KuzuObjectRefDestroyedException {
        KuzuQueryResult result = conn.query(q);
        long index = findColumnIndex(result, "c");
        System.out.println(result.getNext().getValue(index));

        /*try (Transaction tx = db.beginTx()) {
            Result result = tx.execute(q);

            List<String> columns = result.columns();
            Iterator<Long> counts = result.columnAs("c");
            System.out.println(counts.next());
        }*/
    }

    // Add the nodes involved in a set of Relationships to nodeSet for future reference
    /*public void relationshipNodes( HashMap<String,Set<KuzuValue>> relationshipSet) throws KuzuObjectRefDestroyedException {
        HashMap<String,Set<String>> nodeIds = new HashMap<>();
        for(Map.Entry<String,Set<KuzuValue>> entry : relationshipSet.entrySet()){
            String pathName = entry.getKey();
            Set<KuzuValue> relations = entry.getValue();
            // iterate through relations
            for(KuzuValue r : relations){
                // get source and destination nodes
                KuzuInternalID idSrc = KuzuValueRelUtil.getSrcID(r);
                KuzuInternalID idDest = KuzuValueRelUtil.getDstID(r);
                // add source & destination nodes to set of nodes for that pathName
                for (KuzuInternalID n : new KuzuInternalID[]{idSrc, idDest}){
                    // If nodeid already exists in nodeids(HashSet) the item is not inserted
                    Set<String> nodeIdPath;
                    if(nodeIds.containsKey(pathName))
                        nodeIdPath = nodeIds.get(pathName);
                    else
                        nodeIdPath = new HashSet<>();
                    nodeIdPath.add(internalIdToString(n));
                    nodeIds.put(pathName,nodeIdPath);
                }
            }
        }

        nodeSet = new HashMap<>();
        for (String key : nodeIds.keySet()) {
            nodeSet.put(key, new ArrayList<>(nodeIds.get(key)));
        }
        // update nodeSet to hold the current nodeids
    }*/

    // TODO
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
    // done
    public static Set<JessNode> getNodes(String query, Rete engine) throws JessException, KuzuObjectRefDestroyedException {
        Set<JessNode> nodeSet = new HashSet<>();

        String after = (query.split(":")[1]);
        String label = after.split("\\)")[0];

        System.out.println("Label is " + label);

        NodeEnum lab = NodeEnum.User; //default
        if(label.equals("Post")) lab = NodeEnum.Post;
        else if (label.equals("Tag")) lab = NodeEnum.Tag;
        else if (label.equals("User"));
        else System.out.println("invalid label specified");


        KuzuQueryResult result = conn.query(query);
        while (result.hasNext()) {
            KuzuFlatTuple row = result.getNext();
            JessNode entry = new JessNode();
            entry.setLabel(lab);

            for (int i = 0; i < result.getNumColumns(); i++) {
                String colname = result.getColumnName(i);
                KuzuValue value = row.getValue(i);
                // TODO : check if == null is correct
                if (value == null) continue;

                // TODO: check if name "id"
                if (("" + colname).equals("id")) {
                    entry.setId(Integer.parseInt("" + row.getValue(i)));
                    continue;
                }


                switch (lab) {
                    case User -> {
                        switch ("" + colname) {
                            case "displayname" -> {
                                entry.setDisplayname("" + value);
                            }
                            case "userId" -> {
                                entry.setUserId(Integer.parseInt("" + value));
                            }
                            case "aboutme" -> {
                                entry.setAboutme("" + value);
                            }
                            case "reputation" -> {
                                entry.setReputation(Integer.parseInt("" + value));
                            }
                            case "upvotes" -> {
                                entry.setUpvotes(Integer.parseInt("" + value));
                            }
                            case "downvotes" -> {
                                entry.setDownvotes(Integer.parseInt("" + value));
                            }
                            case "views" -> { //doesn't work since the other middleware sets views to an array
                                entry.setViews(Integer.parseInt("" + value));
                            }
                            default -> {
                                System.out.println("Userdefaulted");
                            }
                        }

                    }
                    case Post -> {
                        switch ("" + colname) {
                            //Post type
                            case "body": {
                                entry.setBody("" + value);
                                break;
                            }
                            case "comments": {
                                entry.setComments(Integer.parseInt("" + value));
                                break;
                            }
                            case "postId": {
                                entry.setPostId(Integer.parseInt("" + value));
                                break;
                            }
                            case "score": {
                                entry.setScore(Integer.parseInt("" + value));
                                break;
                            }
                            case "tagId": {
                                entry.setTagId(("" + value));
                                break;
                            }
                            default: {
                                System.out.println("Postdefaulted");
                                break;
                            }
                        }

                    }
                    case Tag -> {
                        if (("" + colname).equals("tagId")) {
                            entry.setTagId("" + value);
                        }
                    }
                }

            }
            engine.add(entry);
        }
        return nodeSet;
    }

    // done
    public static Set<JessEdge> getEdges(String query, Rete engine) throws JessException, KuzuObjectRefDestroyedException {
        Set<JessEdge> edgeSet = new HashSet<>();

        String after = (query.split(":")[1]);
        String label = after.split("]")[0];

        System.out.println("Label is " + label);

        EdgeEnum lab = EdgeEnum.POSTED; //default
        if(label.equals("PARENT_OF")) lab = EdgeEnum.PARENT_OF;
        else if (label.equals("HAS_TAG")) lab = EdgeEnum.HAS_TAG;
        else if (label.equals("POSTED"));
        else System.out.println("invalid label found");

        KuzuQueryResult result = conn.query(query);

        while (result.hasNext()) {
            KuzuFlatTuple row = result.getNext();
            JessEdge entry = new JessEdge();

            // to do  manual label set
            entry.setLabel(lab);

            for (int i = 0; i < result.getNumColumns(); i++) {
                String colname = result.getColumnName(i);
                KuzuValue value = row.getValue(i);
                if(value == null) continue;
                boolean flag = true;

                switch(""+colname) {
                    case "identity": {
                        entry.setIdentity(Integer.parseInt(""+ value));
//                            if(Integer.parseInt(""+column.getValue())!=191317 && Integer.parseInt(""+column.getValue())!=1950652) flag = false;
                        break;
                    }
                    case "start": {
                        entry.setStart(Integer.parseInt("" + value));
                        break;
                    }
                    case "end": {
                        entry.setEnd(Integer.parseInt(""+ value));;
                        break;
                    }
                    default:{
                        System.out.println("defaulted");
                        break;
                    }
                }
            }
            engine.add(entry);
            // edgeSet.add(entry);
        }
        return edgeSet;
    }


}