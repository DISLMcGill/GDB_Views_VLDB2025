package main;

import gen.ViewLexer;
import gen.ViewParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import com.kuzudb.*;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class MainKuzu {
    // node/edgeTable : view name -> {nodeName -> {tableName -> [list of ids]}}
    protected static Map<String, HashMap<String, HashMap<String, List<Integer>>>> nodeTable = new ConcurrentHashMap<>();
    protected static Map<String, HashMap<String, HashMap<String, List<Integer>>>> edgeTable = new ConcurrentHashMap<>();

    // pathRelTable: map view name -> {pathName -> {list {list of ids in a single path}}}
    protected static Map<String, HashMap<String,List<List<String>>>> pathRelTable = new ConcurrentHashMap<>();
    protected static Map<String, String> typeTable = new ConcurrentHashMap<>();

    // Added for keeping track of the view queries for non-materialized views
    protected static Map<String, String> viewQueryTable = new ConcurrentHashMap<>();
    protected static Map<String, String> viewReturnVarTable = new ConcurrentHashMap<>();

    public static ParseTreeWalker walker = new ParseTreeWalker();
    public static QueryParser vql = new QueryParser();

    public static KuzuGraphConnector connector;
    public static long totalTime = 0;

    public static void main(String[] args) {
        try {
            // TODO: add database size when implemented
            connector = new KuzuGraphConnector("/Users/owenlipchitz/Owen/programming/databaseV1");
            System.out.println("Num args: " + args.length);


            // size could be set to any of the ["small", "medium", "large"]
            String size = "large";

            // Getting the Connector object to Kuzu
            System.out.println(args.length);
            // TODO: The below two function calls should be removed [if not used]
            //loadTablesFromFiles(size);
            //createMetaInfoFromQueries("./test/initFileExample.txt");

            // Check if any inputs are given [This is to facilitate running long tests]

            // Running commands of a file without clearing the cache
            if (args.length == 1) {
                String fileName = args[0];
                ArrayList<String> commands_in_order = getExperimentCommands(fileName);

                // NOTE: Need to set this per experiment: Setting System.out to a file for easier processing
                //PrintStream o = new PrintStream(new File("./test/view_use/baseline/outputs/global/warm_medium_new.txt"));

                //PrintStream console = System.out;

                //System.setOut(o);

                for (String cmd: commands_in_order) {
                    if (cmd.equals("quit")) {
                        break;
                    }
                    else {
                        System.out.println(cmd);
                        processCommand(cmd);
                        System.out.println("*********************************");

                    }
                }
            } else if (args.length == 2 && args[0].equals("cold")) {
                String cmd = args[1];
                // This is to warm up the system with bringing the post index into the memory
                //String simplecmd = "baseline test MATCH (n:Post) WHERE n.postId = '3468801' RETURN n";
                String simplecmd = "CREATE VIEW AS V_test MATCH (n:Post) WHERE n.postId = '3468801' RETURN n";
                processCommand(simplecmd);
                System.out.println(cmd);
                processCommand(cmd);
            } else if (args.length == 2 && (args[0].equals("method1") || args[0].equals("method2"))) {
                String fileName = args[1];
                ArrayList<String> commands_in_order = getExperimentCommands(fileName);

                for (String cmd: commands_in_order) {
                    if (cmd.equals("quit")) {
                        break;
                    }
                    else {
                        System.out.println(cmd);
                        processCommand(cmd);
                        System.out.println("*********************************");
                    }
                }
            } else if (args.length == 2 && args[0].equals("cold_use")) {
                PrintStream o = new PrintStream(new File("./test/tmp.txt"));

                PrintStream console = System.out;

                // Sending create log stuff to a tmp file
                System.setOut(o);

                // Create all the views in the system
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader("test/views.txt"));
                    String line = reader.readLine();

                    while (line != null) {
                        System.out.println(line);
                        processCommand("CREATE VIEW AS " + line);
                        line = reader.readLine();
                    }

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Send use logs to the console
                System.setOut(console);
                // Run the "View Use" command that is the input
                String cmd = args[1];
                System.out.println(cmd);
                processCommand(cmd);
            } else if (args.length == 2 && args[0].equals("cold_use_old")) {
                PrintStream o = new PrintStream(new File("./test/tmp.txt"));

                PrintStream console = System.out;

                // Sending create log stuff to a tmp file
                System.setOut(o);

                // Create all the views in the system
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new FileReader("test/views_old.txt"));
                    String line = reader.readLine();

                    while (line != null) {
                        System.out.println(line);
                        processCommand("CREATE VIEW AS " + line);
                        line = reader.readLine();
                    }

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Send use logs to the console
                System.setOut(console);
                // Run the "View Use" command that is the input
                String cmd = args[1];
                System.out.println(cmd);
                processCommand(cmd);
            } else if (args.length == 2 && args[0].equals("test")) {
                // add an argument to an existing node
                String query = "baseline test MATCH (n:Post) WHERE n.postId = '3468801' RETURN n";
                processCommand(query);
            }

            // Big interactive loop to process the input commands
            else {
                // Reading the first input command from the terminal
                InputStreamReader isReader = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isReader);
                System.out.print(">> ");
                String command = br.readLine();
                try {
                    while (!command.startsWith("quit")) {
                        processCommand(command);
                        System.out.print(">> ");
                        command = br.readLine();
                    }
                    isReader.close();
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                connector.shutdown();
            } catch (KuzuObjectRefDestroyedException e) {
                System.out.println("Connector was destroyed");
                System.exit(1);
            }
        }
    }


    /**
     * Method to process an input command and call appropriate query function
     * Note: previously terminal/experiment_terminal merged
     * @param command command to process
     */
    public static void processCommand(String command){

        // NOTE These parameters should be set manually by the user
        boolean materialized = false;
        boolean debug = false;

        try {
            if (command.startsWith("printOrView")) {
                vql.printOrClauseViews();
            } else if (command.startsWith("printDependencies")) {
                vql.printDependencies();
            } else if (command.startsWith("printNode")) {
                System.out.println(nodeTable.toString());
            } else if (command.startsWith("clear")) {
                vql.clearAll();
            } else if (command.startsWith("wipe")) {
                // don't add views attribute?
                //connector.executeQuery("MATCH (n) REMOVE n.views");
                System.out.println("Should wipe n.views");
            } else if (command.startsWith("view wipe")) {
                //connector.executeInternalBaselineQuery("MATCH (n) DETACH DELETE n");
                System.out.println("Should wipe views");
            } else if (command.startsWith("debug switch")) {
                debug = !debug;
            } else if (command.startsWith("count")) {
                Set<String> keys = nodeTable.keySet();
                for (String key : keys) {
                    System.out.println(key + ":" + nodeTable.get(key).size());
                }
            } else if (command.startsWith("delete all views")) {
                // Used for deleting view's arguments in the system
                connector.clearViewnameProperty();
            } else if (command.startsWith("baseline")) {
                String query = command.substring(command.indexOf(" ", command.indexOf(" ") + 1) + 1);
                long start = System.currentTimeMillis();
                long querySize = connector.executeBaselineQuery(query);
                long duration = System.currentTimeMillis() - start;
                System.out.println("Took " + duration + " ms to execute baseline query");
                System.out.println("Baseline returned " + querySize + " nodes (or edges)");
            } else if (command.startsWith("original")) {
                String query = command.split("original")[1];
                connector.executeDirectly(query);
            } else if (command.startsWith("range")) {
                // For conducting the nodeid range experiments
                int limit  =  Integer.parseInt(command.substring(command.indexOf(" ", command.indexOf(" ") + 1) + 1));
                List<String> nodeIds = new ArrayList<>();
                for (int i = 1; i < limit + 1; i++) {
                    nodeIds.add(Integer.toString(i));
                }
                String nodeIdsString = mergeIdsToString(nodeIds);
                String query = "MATCH (n) WHERE list_contains(" + nodeIdsString + ", string(ID(n))) RETURN DISTINCT n";
                long start = System.currentTimeMillis();
                long querySize = connector.executeBaselineQuery(query);
                long duration = System.currentTimeMillis() - start;
                System.out.println("Took " + duration + " ms to execute baseline query");
                System.out.println("Baseline returned " + querySize + " nodes (or edges)");
            } else if (command.startsWith("profile")) {
                connector.executeProfileQuery(command);
            }
            else {

                // Break up the input stream of characters into vocabulary symbols for a parser
                ViewLexer VL = new ViewLexer(CharStreams.fromString(command));
                CommonTokenStream tokens = new CommonTokenStream(VL);
                ViewParser parser = new ViewParser(tokens);

                ParseTree tree = parser.root();
                walker.walk(vql, tree);

                if (vql.isViewInstant()) {
                    long now = System.currentTimeMillis();
                    // Changed temporarily
                    processMainView(command, materialized);
                    //processMainViewMethod1(command, materialized);

                    long total = System.currentTimeMillis() - now;
                    System.out.println("Took " + total + "ms to create views");
                } else if (vql.isViewUse()) {
                    long now = System.currentTimeMillis();
                    String viewName = command.split(" ")[2];

                    // Break into multiple queries if containing "WITH"
                    String[] breakByWith = command.split("WITH");
                    int partNum = breakByWith.length;
                    String subCommand = command;
                    HashMap<String,List<String>> intermediateResult = new HashMap<>();
                    //if there is no MATCH within WITH, then no need to split
                    for(int i = breakByWith.length-1; i > 1  ;i--) {
                        if(!breakByWith[i].contains("IN "+ viewName))
                            partNum--;
                    }

                    if(partNum >  2) {
                        //if enter here, really need to split
                        for(int i = 1; i < breakByWith.length-1 ;i++) {
                            subCommand = "MATCH" + breakByWith[i].split("MATCH")[1]+"RETURN DISTINCT ";
                            String[] returnVals = breakByWith[i+1].split("MATCH")[0].trim().split(",");
                            for(String returnVal : returnVals) {
                                subCommand += "string(ID(" +returnVal + ")), ";
                            }
                            subCommand = subCommand.substring(0,subCommand.length()-2);
                            intermediateResult = processUseView(subCommand,intermediateResult);
                        }
                        subCommand = "MATCH" + breakByWith[breakByWith.length-1].split("MATCH")[1];
                        String subBefore = subCommand.split("RETURN")[0];
                        String subReturn = subCommand.split("RETURN")[1];
                        for(Map.Entry<String,List<String>> entry : intermediateResult.entrySet()) {
                            String paramName = entry.getKey();
                            if(subBefore.contains("WHERE"))
                                subBefore = subBefore + " AND list_contains(" + entry.getValue() + ", string(ID(" + paramName + ")))";
                            else
                                subBefore = subBefore + " WHERE list_contains(" + entry.getValue() + ", string(ID(" + paramName + ")))";
                        }
                        subCommand = subBefore + " RETURN " +subReturn;
                        intermediateResult = processUseView(subCommand,intermediateResult);
                    }else {
                        processUseView(subCommand, null);
                    }

                    long total = System.currentTimeMillis() - now;
                    System.out.println("Took " + total + "ms to use view");
                } else if (vql.isCg()) {
                    long now = System.currentTimeMillis();
                    changeGraph(command);
                    long total = System.currentTimeMillis() - now;
                    System.out.println("Took " + total + "ms to change graph and update view(s)");
                }

                if (!debug) vql.clearAll();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Extracts commands from file
     * @param filePath file containing commands
     * @return list of commands
     */
    public static ArrayList<String> getExperimentCommands(String filePath){
        ArrayList<String> commands = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(filePath));
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                commands.add(line);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return commands;
    }

    /**
     * Extracts all substrings between given character delimiters
     * @param input full string
     * @param startChar starting delimiter
     * @param endChar end delimiter
     * @return list of substrings in input between delimiters
     */
    public static List<String> getSubstringsBetweenCharacters(String input, char startChar, char endChar) {
        List<String> substrings = new ArrayList<>();
        int startIndex = -1;

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == startChar) {
                startIndex = i;
            } else if (input.charAt(i) == endChar && startIndex != -1) {
                substrings.add(input.substring(startIndex + 1, i));
                startIndex = -1;
            }
        }

        return substrings;
    }

    // not used
    public static String createPathQueryNonMaterializedTerm(String query) {
        String finalRes = query + " WITH ";
        List<String> nodeVars = getSubstringsBetweenCharacters(query, '(', ':');
        StringBuilder pathNodeCollectTerm = new StringBuilder();
        for (String s: nodeVars) {
            pathNodeCollectTerm.append("COLLECT(DISTINCT ID(").append(s).append(")) + ");
        }
        // Trimming the extra characters
        pathNodeCollectTerm = new StringBuilder(pathNodeCollectTerm.substring(0, pathNodeCollectTerm.length() - 2));

        finalRes += pathNodeCollectTerm;
        return finalRes;
    }


    public static void processMainView(String cmd, boolean materialized) throws KuzuObjectRefDestroyedException {
        String viewname = vql.getViewName();
        String fullQuery = cmd.split(viewname)[1];

        String returnSymbol = vql.getReturnValExpr();
        String mainQuery = fullQuery.split("RETURN")[0];
        System.out.println(fullQuery);
        String orderBy = "";
        if (fullQuery.contains("ORDER BY")) {
            orderBy = fullQuery.split("ORDER BY")[1];
        }
        String returns = fullQuery.split("RETURN")[1].trim();
        returns = returns.split("ORDER BY")[0].trim();


        String[] returnArray = returns.split(",");

        String makeMiddlewareView = "";

        // Storing the view with its returnType
        typeTable.put(viewname, vql.getReturnType().toString());
        viewQueryTable.put(viewname, mainQuery.trim());
        viewReturnVarTable.put(viewname, returns.trim());

        // Building the makeMiddlewareView and fullQuery strings
        switch(vql.getReturnType()){
            // for node & edges, extract label and offset in the table
            case NODE, EDGE:{
                String[] returnSymbols = returnSymbol.split(",");
                StringBuilder updateReturnClause = new StringBuilder( returns );
                // e.g. RETURN n, m becomes RETURN LABEL(n), OFFSET(ID(n)), LABEL(m), OFFSET(ID(m))
                for(String retSym : returnSymbols) {
                    updateReturnClause = new StringBuilder(updateReturnClause.toString().replace(
                            retSym, "LABEL("+retSym + ") as " + retSym + "_label, OFFSET(ID(" + retSym + ")) as " +
                            retSym + "_offset")
                    );
                }
                makeMiddlewareView = mainQuery + "RETURN DISTINCT " + updateReturnClause;
                if (orderBy.length() > 0) {
                    makeMiddlewareView += " ORDER BY " + orderBy;
                }

                break;
            }
            case PATHNODES:{
                break;
            }
            // for paths, extract labels and ID (full) of nodes and edges in the path
            case PATH: {
                String unwindString = "";
                StringBuilder returnString = new StringBuilder();
                for (String pathName : returnArray) {
                    //unwindString += " UNWIND(properties(NODES(" + pathName + "), \"_id\")) as " + pathName + "_nodes" +
                     //       " UNWIND(properties(RELS(" + pathName + "), \"_id\")) as " + pathName + "_rels ";

                    // e.g. RETURN p becomes RETURN properties(NODES(p), "_labels") as p_nodesLabels, properties(NODES(p), "_id") as p_nodesIds,
                    // properties(RELS(p), "_labels") as p_relsLabels, properties(RELS(p), "_id") as p_relsIds
                    if (!returnString.toString().equals("")) { returnString.append(", ");}
                    returnString.append("(properties(NODES(").append(pathName).append("), \"_label\")) as ").append(pathName).append("_nodesLabels, ").
                            append("(properties(NODES(").append(pathName).append("), \"_id\")) as ").append(pathName).append("_nodesIds, ").
                            append("(properties(RELS(").append(pathName).append("), \"_label\")) as ").append(pathName).append("_relsLabels, ").
                            append("(properties(RELS(").append(pathName).append("), \"_id\")) as ").append(pathName).append("_relsIds ");
                }

                makeMiddlewareView = mainQuery + unwindString + "RETURN " + returnString;
                if (orderBy.length() > 0) {
                    makeMiddlewareView += " ORDER BY " + orderBy;
                }
            }
            case DEFAULT: {
                break;
            }
        }
        System.out.println(fullQuery);

        // NODE VIEW: Call connector.executeQuery() with makeMiddlewareView as input and update nodeTable with the result nodes
        if(vql.getReturnType() == QueryParser.retType.NODE) {
            System.out.println("View query: " + makeMiddlewareView);
            HashMap<String, HashMap<String, List<Integer>>> nodes = connector.executeQuery(makeMiddlewareView);
            nodeTable.put(viewname, nodes);

            int resultSize = 0;
            HashMap<String, HashMap<String, List<Integer>>> nodesInViewDiffName = nodeTable.get(viewname);
            for(HashMap<String, List<Integer>> part :nodesInViewDiffName.values()) {
                for (List<Integer> ids : part.values())
                    resultSize += ids.size();
            }
            System.out.println("There are " + resultSize + " nodes");

        }
        if(vql.getReturnType() == QueryParser.retType.EDGE) {
            HashMap<String, HashMap<String, List<Integer>>> edges = connector.executeQuery(makeMiddlewareView);
            edgeTable.put(viewname, edges);

            int resultSize = 0;
            HashMap<String, HashMap<String, List<Integer>>> edgesInViewDiffName = edgeTable.get(viewname);
            for(HashMap<String, List<Integer>> part :edgesInViewDiffName.values()) {
                for (List<Integer> ids : part.values())
                    resultSize += ids.size();
            }
            System.out.println("There are " + resultSize + " edges");

        }

        // PATH VIEW: Call
        if(vql.getReturnType() == QueryParser.retType.PATH ){
            // Getting relationships of the path query
            long start = System.currentTimeMillis();
            System.out.println("View query: " + makeMiddlewareView);
            HashMap<String,List<List<String>>> allRlist = connector.pathQuery(makeMiddlewareView);
            long executeEnd = System.currentTimeMillis();

           // HashMap<String,List<List<String>>> allrlist = (HashMap<String,List<List<String>>>)processedPath[1];

            pathRelTable.put(viewname, allRlist);

            // Storing edges
            HashMap<String,HashMap<String,List<Integer>>> edgeIds = connector.getEdgeSet();
            edgeTable.put(viewname, edgeIds);

            HashMap<String, HashMap<String, List<Integer>>> nodeIds = connector.getNodeSet();
            int num = 0;
            for(Entry<String, HashMap<String, List<Integer>>> entry :nodeIds.entrySet()){
                for (Entry<String, List<Integer>> entry2 : entry.getValue().entrySet()) {
                    num += entry2.getValue().size();
                }
            }

            System.out.println("There are " + num + " nodes");

            // Storing nodes
            nodeTable.put(viewname, nodeIds);
            long processEnd = System.currentTimeMillis();
            System.out.println("Execution time: " + (executeEnd-start));
            System.out.println("Process time: " + (processEnd-executeEnd));
        }

        // Uncomment for testing purposes
        /* System.out.println("Path Table is + " + pathTable.toString());
        System.out.println("Node Table is + " + nodeTable.toString());
        System.out.println("Edge Table is + " + edgeTable.toString());
        System.out.println("PathNode Table is + " + pathnodeTable.toString()); */

    }

    /**
     * Method to process queries that use views with a LOCAL MATCH
     * @param cmd input query
     * @param fullQuery query without view part
     * @param idMap map of ids to complete with view
     * @param rlist list of edgeIds to complete
     * @return rewritten fullQuery
     */
    public static String processUseViewLocal(String cmd, String fullQuery, HashMap<String,List<Integer>> idMap,
                                             List<List<String>> rlist) {
        System.out.println("Scope:LOCAL");
        //local, so there are omissions for set membership. if there are omissions then it has to be a single view usage

        LinkedList<String> usedViews = vql.usedViews();
        List<String> nodeIdentifiers = vql.nodeSymbols();

        if(!cmd.contains("*") && !vql.addWhereClause.keySet().isEmpty()) {
            for (String nodeName : vql.addWhereClause.keySet()) {
                for (String viewName : vql.addWhereClause.get(nodeName)) {
                    String target = nodeName + " IN " + viewName;
                    StringBuilder replacement;

                    if(viewName.contains(".")) {
                        if (vql.getPathName().equals(nodeName)) {
                            //first add name to the relations, if not exists
                            Matcher m = java.util.regex.Pattern.compile("\\[([^\\[\\]]+)]").matcher(fullQuery);
                            int i = 1;
                            StringBuilder edgeCondition = new StringBuilder();
                            while (m.find()) {
                                String content = m.group(1);
                                if(content.matches("^:.*")) {
                                    while(fullQuery.contains("r"+i)) i++;
                                    fullQuery = fullQuery.replace(content, "r"+i+content);
                                    edgeCondition.append("ID(r").append(i).append(") = edges[").append(i - 1).append("] AND ");
                                    rlist.addAll(pathRelTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]));
                                }
                            }
                            replacement = new StringBuilder(edgeCondition.substring(0, edgeCondition.length() - 5));
                            fullQuery = "UNWIND $edges AS edges " + fullQuery.replace(target, replacement.toString());
                        } else {
                            HashMap<String, List<Integer>> offsetsPerTable;
                            if (vql.nodeSymbols().contains(nodeName)) {
                                 offsetsPerTable = nodeTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]);
                            } else if (vql.relationSymbols().contains(nodeName)) {
                                offsetsPerTable = edgeTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]);
                            } else {
                                System.out.println("No such item in the view");
                                continue;
                            }
                            replacement = new StringBuilder();
                            if (!offsetsPerTable.isEmpty()) {
                                if (offsetsPerTable.keySet().size() == 1) {
                                    String tableName = offsetsPerTable.keySet().toArray()[0].toString();
                                    fullQuery = fullQuery.replaceFirst(nodeName, (nodeName + ":" + tableName));
                                    idMap.put(viewName.split("\\.")[0] + "Node" + viewName.split("\\.")[1] + tableName, offsetsPerTable.get(tableName));
                                    if (!replacement.toString().equals("")) {
                                        replacement.append(" OR ");
                                    }
                                    replacement.append("(LABEL(").append(nodeName).append(") = \"").append(tableName).
                                            append("\" AND list_contains($").append(viewName.split("\\.")[0] + "Node" + viewName.split("\\.")[1] + tableName).
                                            append(", OFFSET(ID(").append(nodeName).append("))))");
                                } else {
                                    for (Entry<String, List<Integer>> entry : offsetsPerTable.entrySet()) {
                                        fullQuery = fullQuery.replaceFirst(nodeName, (nodeName + ":" + entry.getKey()));
                                        idMap.put(viewName.split("\\.")[0] + "Node" + viewName.split("\\.")[1] + entry.getKey(), entry.getValue());
                                        if (!replacement.toString().equals("")) {
                                            replacement.append(" OR ");
                                        }
                                        replacement.append("(LABEL(").append(nodeName).append(") = \"").append(entry.getKey()).
                                                append("\" AND list_contains($").append(viewName.split("\\.")[0] + "Node" + viewName.split("\\.")[1] + entry.getKey()).
                                                append(entry.getKey()).
                                                append(", OFFSET(ID(").append(nodeName).append("))))");
                                    }
                                }
                            }
                            //replacement = "ID(" + nodeName + ") IN $" + viewName.split("\\.")[0] + "Node" + viewName.split("\\.")[1];
                            fullQuery = fullQuery.replace(target, replacement.toString());
                        }
                    } else {

                        HashMap<String, List<Integer>> idsPerTable = new HashMap<>();
                        if(vql.nodeSymbols().contains(nodeName)){
                            for (Map.Entry<String, HashMap<String, List<Integer>>> entry : nodeTable.get(viewName).entrySet()) {
                                for (Map.Entry<String, List<Integer>> innerEntry : entry.getValue().entrySet()) {
                                    if (!idsPerTable.containsKey(innerEntry.getKey())) {
                                        idsPerTable.put(innerEntry.getKey(), innerEntry.getValue());
                                    } else {
                                        idsPerTable.get(innerEntry.getKey()).addAll(innerEntry.getValue());
                                    }
                                }
                            }

                        }else if (vql.relationSymbols().contains(nodeName)){
                            for (Map.Entry<String, HashMap<String, List<Integer>>> entry : edgeTable.get(viewName).entrySet()) {
                                for (Map.Entry<String, List<Integer>> innerEntry : entry.getValue().entrySet()) {
                                    if (!idsPerTable.containsKey(innerEntry.getKey())) {
                                        idsPerTable.put(innerEntry.getKey(), innerEntry.getValue());
                                    } else {
                                        idsPerTable.get(innerEntry.getKey()).addAll(innerEntry.getValue());
                                    }
                                }
                            }
                        }

                        if(!idsPerTable.isEmpty()) {
                            if (idsPerTable.keySet().size() == 1) {
                                replacement = new StringBuilder();
                                String tableName = idsPerTable.keySet().toArray()[0].toString();
                                fullQuery = fullQuery.replaceFirst(nodeName, (nodeName + ":" + tableName));
                                idMap.put(viewName + tableName, idsPerTable.get(tableName));
                                if (!replacement.toString().equals("")) {
                                    replacement.append(" OR ");
                                }
                                replacement.append("(list_contains($").append(viewName).append(tableName).
                                        append(", OFFSET(ID(").append(nodeName).append("))))");
                            } else {
                                // if ids are all from the same table
                                replacement = new StringBuilder();
                                for (Entry<String, List<Integer>> entry : idsPerTable.entrySet()) {
                                    fullQuery = fullQuery.replaceFirst(nodeName, (nodeName + ":" + entry.getKey()));
                                    idMap.put(viewName + entry.getKey(), entry.getValue());
                                    if (!replacement.toString().equals("")) {
                                        replacement.append(" OR ");
                                    }
                                    replacement.append("(LABEL(").append(nodeName).append(") = \"").append(entry.getKey()).
                                            append("\" AND list_contains($").append(viewName).append(entry.getKey()).
                                            append(", OFFSET(ID(").append(nodeName).append("))))");
                                }
                            }
                            fullQuery = fullQuery.replace(target, replacement.toString());
                        }
                    }
                }
            }
        } else if (vql.addWhereClause.keySet().isEmpty()) {
            if(!fullQuery.contains("p=")){
                String nodeName = nodeIdentifiers.get(0);
                String viewName = usedViews.get(0);

                HashMap<String, List<Integer>> idsPerTable = new HashMap<>();
                if(vql.nodeSymbols().contains(nodeName)){
                    for (Map.Entry<String, HashMap<String, List<Integer>>> entry : nodeTable.get(viewName).entrySet()) {
                        for (Map.Entry<String, List<Integer>> innerEntry : entry.getValue().entrySet()) {
                            if (!idsPerTable.containsKey(innerEntry.getKey())) {
                                idsPerTable.put(innerEntry.getKey(), innerEntry.getValue());
                            } else {
                                idsPerTable.get(innerEntry.getKey()).addAll(innerEntry.getValue());
                            }
                        }
                    }

                }else if (vql.relationSymbols().contains(nodeName)){
                    for (Map.Entry<String, HashMap<String, List<Integer>>> entry : edgeTable.get(viewName).entrySet()) {
                        for (Map.Entry<String, List<Integer>> innerEntry : entry.getValue().entrySet()) {
                            if (!idsPerTable.containsKey(innerEntry.getKey())) {
                                idsPerTable.put(innerEntry.getKey(), innerEntry.getValue());
                            } else {
                                idsPerTable.get(innerEntry.getKey()).addAll(innerEntry.getValue());
                            }
                        }
                    }
                }
                StringBuilder replacement = new StringBuilder();
                if(!idsPerTable.isEmpty()) {
                    if (idsPerTable.keySet().size() == 1) {
                        String tableName = idsPerTable.keySet().toArray()[0].toString();
                        fullQuery = fullQuery.replaceFirst(nodeName, (nodeName + ":" + tableName));
                        idMap.put(viewName + tableName, idsPerTable.get(tableName));
                        if (!replacement.toString().equals("")) {
                            replacement.append(" OR ");
                        }
                        replacement.append("(list_contains($").append(viewName).append(tableName).
                                append(", OFFSET(ID(").append(nodeName).append("))))");
                    } else {
                        // if ids are all from the same table
                        for (Entry<String, List<Integer>> entry : idsPerTable.entrySet()) {
                            fullQuery = fullQuery.replaceFirst(nodeName, (nodeName + ":" + entry.getKey()));
                            idMap.put(viewName + entry.getKey(), entry.getValue());
                            if (!replacement.toString().equals("")) {
                                replacement.append(" OR ");
                            }
                            replacement.append("(LABEL(").append(nodeName).append(") = \"").append(entry.getKey()).
                                    append("\" AND list_contains($").append(viewName).append(entry.getKey()).
                                    append(", OFFSET(ID(").append(nodeName).append("))))");
                        }
                    }
                }

                fullQuery = fullQuery.split("RETURN")[0] + "WHERE "+ replacement +" RETURN" + fullQuery.split("RETURN")[1];
            }
        }
        System.out.println(fullQuery);
        return fullQuery;
    }

    /**
     * Method to process queries that use views with a GLOBAL MATCH
     * @param fullQuery query without view part
     * @param idMap map of ids to complete with view
     * @return rewritten fullQuery
     */
    public static String processUseViewGlobal(String fullQuery, HashMap<String,List<Integer>> idMap) {
        System.out.println("Scope:GLOBAL");

        for (String nodeName : vql.addWhereClause.keySet()) {
            for (String viewName : vql.addWhereClause.get(nodeName)) {
                String target = nodeName + " IN " + viewName;
                StringBuilder replacement = new StringBuilder();

                //String replacement;
                if(!fullQuery.contains(target)) {
                    continue;
                }

                List<String> ids = new ArrayList<>();
                if (viewName.contains(".")) {
                    System.out.println(vql.nodeSymbols());;
//                    System.out.println(vql.());;
                    //When the ViewName is V_IC6.friend, it needs to be split into IC6 and friend, and then the nodeTable is accessed
                    //It isn't doing that right now

                    HashMap<String, List<Integer>> offsetsPerTable;
                    if (vql.nodeSymbols().contains(nodeName)) {
                        offsetsPerTable = nodeTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]);
                    } else if (vql.relationSymbols().contains(nodeName)) {
                        offsetsPerTable = edgeTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]);
                    } else {
                        System.out.println("No such item in the view");
                        continue;
                    }
                    for (Entry<String, List<Integer>> entry : offsetsPerTable.entrySet()) {
                        idMap.put(viewName.split("\\.")[0] + "Node"  + viewName.split("\\.")[1] + entry.getKey(), entry.getValue());
                        if (!replacement.toString().equals("")) {
                            replacement.append(" OR ");
                        }
                        replacement.append("(LABEL(").append(nodeName).append(") = \"").append(entry.getKey()).
                                append("\" AND list_contains($").append(viewName.split("\\.")[0] + "Node"  + viewName.split("\\.")[1] + entry.getKey()).
                                append(", OFFSET(ID(").append(nodeName).append("))))");
                    }
                    //replacement = "ID(" + nodeName + ") IN $" + viewName.split("\\.")[0] + "Node" + viewName.split("\\.")[1];
                    fullQuery = fullQuery.replace(target, replacement.toString());
                } else if(vql.nodeSymbols().contains(nodeName)) {
                    HashMap<String, List<Integer>> idsPerTable = new HashMap<>();
                    for (Map.Entry<String, HashMap<String, List<Integer>>> entry : nodeTable.get(viewName).entrySet()) {
                        for (Map.Entry<String, List<Integer>> innerEntry : entry.getValue().entrySet()) {
                            if (!idsPerTable.containsKey(innerEntry.getKey())) {
                                idsPerTable.put(innerEntry.getKey(), innerEntry.getValue());
                            } else {
                                idsPerTable.get(innerEntry.getKey()).addAll(innerEntry.getValue());
                            }
                        }
                    }
                    if(!idsPerTable.isEmpty()) {
                        // if ids are all from the same table
                        replacement = new StringBuilder();
                        for (Entry<String, List<Integer>> entry : idsPerTable.entrySet()) {
                            idMap.put(viewName + entry.getKey(), entry.getValue());
                            if (!replacement.toString().equals("")) {
                                replacement.append(" OR ");
                            }
                            replacement.append("(LABEL(").append(nodeName).append(") = \"").append(entry.getKey()).
                                    append("\" AND list_contains($").append(viewName).append(entry.getKey()).
                                    append(", OFFSET(ID(").append(nodeName).append("))))");
                        }
                        fullQuery = fullQuery.replace(target, replacement.toString());
                    }
                } else if(vql.relationSymbols().contains(nodeName)) {
                    HashMap<String, List<Integer>> idsPerTable = new HashMap<>();
                    for (Map.Entry<String, HashMap<String, List<Integer>>> entry : edgeTable.get(viewName).entrySet()) {
                        for (Map.Entry<String, List<Integer>> innerEntry : entry.getValue().entrySet()) {
                            if (!idsPerTable.containsKey(innerEntry.getKey())) {
                                idsPerTable.put(innerEntry.getKey(), innerEntry.getValue());
                            } else {
                                idsPerTable.get(innerEntry.getKey()).addAll(innerEntry.getValue());
                            }
                        }
                    }
                    if(!idsPerTable.isEmpty()) {
                        // if ids are all from the same table
                        replacement = new StringBuilder();
                        for (Entry<String, List<Integer>> entry : idsPerTable.entrySet()) {
                            idMap.put(viewName + entry.getKey(), entry.getValue());
                            if (!replacement.toString().equals("")) {
                                replacement.append(" OR ");
                            }
                            replacement.append("(LABEL(").append(nodeName).append(") = \"").append(entry.getKey()).
                                    append("\" AND list_contains($").append(viewName).append(entry.getKey()).
                                    append(", OFFSET(ID(").append(nodeName).append("))))");
                        }
                        fullQuery = fullQuery.replace(target, replacement.toString());
                    }
                }
            }
        }
        return fullQuery;
    }

    public static HashMap<String,List<String>> processUseView(String cmd, Map<String, List<String>> intermediateResult) throws KuzuObjectRefDestroyedException {
        long now = System.currentTimeMillis();
        String fullQuery;
        if(intermediateResult != null){
            fullQuery = "MATCH" + cmd.split("MATCH")[0];
        }else{
            fullQuery = cmd.substring(cmd.indexOf("MATCH"));
        }

        //next three data structure are only for path view usage
        List<List<String>> rlist = new ArrayList<>();
        HashMap<String,List<Integer>> idMap = new HashMap<>();
        int edgeNum = 0;

        System.out.println("FullQuery:: "+ fullQuery);
        System.out.println("cmd:: "+ cmd);

        // If view scope is LOCAL
        if(vql.getViewScope()) {
            fullQuery = processUseViewLocal(cmd, fullQuery, idMap, rlist);
        } else {
            fullQuery = processUseViewGlobal(fullQuery, idMap);
        }

        if(fullQuery.contains("IN null")){
            System.out.println("Nothing in view");
            return new HashMap<>();
        }

        File logger = new File("./test/log.txt");
        try{
            FileWriter l = new FileWriter(logger);
            l.write(fullQuery);
        }
        catch(Exception e) {e.printStackTrace();}

        LinkedList<String> usedViews = vql.usedViews();
        String singleViewName =  usedViews.getFirst();

        long startMergingIds = System.currentTimeMillis();
        Map<String, String> params = new HashMap<>(mergeMapOffsetsToString(idMap));
        System.out.println("Took " + (System.currentTimeMillis() - startMergingIds) + " to merge ids into string");

        // TODO
        if (intermediateResult != null) {
            for (Map.Entry<String, List<String>> entry : intermediateResult.entrySet()) {
                params.put(entry.getKey(), mergeIdsToString(entry.getValue()));
                //fullQuery = fullQuery.replace("WHERE", "WHERE ID(" + entry.getKey() + ") IN $" + entry.getKey() + " AND ");
                fullQuery = fullQuery.replace("WHERE", "WHERE list_contains($" + entry.getKey() + ", string(ID(" + entry.getKey() + "))) AND ");
            }
        }

        // Only checking the path pattern correctness if the USE query itsels has a path pattern in it
        if (typeTable.get(singleViewName).equals("PATH") & cmd.contains("p=")){
            //instead of using pathRelTable to check, try to get result from there
            // make a params map for each eage pairs
            if(cmd.contains("*") && vql.addWhereClause.keySet().isEmpty()) {
                //it's using a view containing paths. first find how many
                //edges are included by counting the "-"
                HashMap<String,List<List<String>>> allPathReturned= pathRelTable.get(singleViewName);

                for(Map.Entry<String,List<List<String>>> entry : allPathReturned.entrySet()){
                    String appendedToQuery = "";
                    rlist = entry.getValue();
                    edgeNum = rlist.get(0).size();
                    for(int i = 1; i <= edgeNum; i++) {
                        appendedToQuery = appendedToQuery + "()-[r" + i + "]-";
                    }
                    appendedToQuery += "() WHERE ";
                    for(int i = 1; i <= edgeNum; i++) {
                        appendedToQuery = appendedToQuery + "string(ID(r" + i + ")) = list_element(edges," + i + ") AND ";
                    }
                    String beforeStar = fullQuery.split("\\*")[0];
                    String afterStar = fullQuery.split("\\*")[1];
                    String afterReturn = afterStar.split("RETURN")[1];
                    // kuzu change : remove distinct after return
                    String newQuery ="UNWIND $edges AS edges " + beforeStar + appendedToQuery.substring(0,appendedToQuery.length()-5) + " RETURN " + afterReturn;
                    return iterateThroughEdges(newQuery, rlist,params);
                }

            }else if(cmd.contains("*")) {
                String newQuery = "";
                for (String nodeName : vql.addWhereClause.keySet()) {
                    for (String viewName : vql.addWhereClause.get(nodeName)) {
                        String appendedToQuery = "";
                        List<List<String>> toIntersect = pathRelTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]);
                        edgeNum = toIntersect.get(0).size();
                        if(newQuery.equals("")) {
                            for(int i = 1; i <= edgeNum; i++) {
                                appendedToQuery = appendedToQuery + "()-[r" + i + "]-";
                            }
                            appendedToQuery += "() WHERE ";
                            for(int i = 1; i <= edgeNum; i++) {
                                appendedToQuery = appendedToQuery + "string(ID(r" + i + ")) = list_element(edges," + i + ") AND ";
                            }
                            String beforeStar = fullQuery.split("\\*")[0];
                            String afterStar = fullQuery.split("\\*")[1];
                            String afterReturn = afterStar.split("RETURN")[1];
                            // Kuzu change: removing DISTINCT after return statement
                            newQuery ="UNWIND $edges AS edges " + beforeStar + appendedToQuery.substring(0,appendedToQuery.length()-5) + " RETURN " + afterReturn;
                            rlist = toIntersect;
                        }else {
                            List<List<String>> afterIntersect = new ArrayList<>();
                            for(List<String> entry : toIntersect) {
                                if(rlist.contains(entry))
                                    afterIntersect.add(entry);
                            }
                            rlist = afterIntersect;
                        }
                    }
                }
                return iterateThroughEdges(newQuery, rlist,params);
            }else {
                String subQuery = fullQuery;
                String newQuery = "";
                for (String nodeName : vql.addWhereClause.keySet()) {
                    for (String viewName : vql.addWhereClause.get(nodeName)) {
                        if(!fullQuery.contains(nodeName)) continue;
                        List<List<String>> toIntersect;
                        if(viewName.contains("."))
                            toIntersect = pathRelTable.get(viewName.split("\\.")[0]).get(viewName.split("\\.")[1]);
                        else {
                            HashMap<String,List<List<String>>> allIds = pathRelTable.get(viewName.split("\\.")[0]);
                            List<List<String>> ids = new ArrayList<>();
                            for(Entry<String,List<List<String>>> entry: allIds.entrySet()) {
                                ids.addAll(entry.getValue());
                            }
                            toIntersect = ids;
                        }
                        toIntersect = new ArrayList<>(new HashSet<>(toIntersect));
                        List<String> sampleRlist = toIntersect.get(0);
                        edgeNum = sampleRlist.size();
                        String pathTarget = nodeName +" IN " + viewName;
                        String pathReplacement = "";
                        for(int i = 1; i < 2*edgeNum; i++) {
                            String beforeEdge = subQuery.split("-")[0];
                            String afterEdge = fullQuery.substring(fullQuery.indexOf(beforeEdge)+beforeEdge.length()+2);
                            subQuery = afterEdge;
                            if(i % 2 == 0)  continue;

                            // nb: list_element(list, index) fetches element of list at index, starting at 1
                            if(afterEdge.startsWith(":")){
                                //there is no name to the edge
                                fullQuery = fullQuery.substring(0,fullQuery.indexOf(beforeEdge)+beforeEdge.length()) + "-[r" + i + afterEdge;

                                pathReplacement += "string(ID(r" + i + ")) = list_element(edges,"+ (i+1)/2 + ") AND ";
                            }else{
                                String thisEdge = afterEdge.split("]")[0];
                                if (thisEdge.contains(":")){
                                    String edgeName = afterEdge.split(":")[0];
                                    pathReplacement += "string(ID(" + edgeName + ")) = list_element(edges,"+ (i+1)/2 + ") AND ";
                                } else {
                                    pathReplacement += "string(ID(" + thisEdge + ")) = list_element(edges,"+ (i+1)/2 + ") AND ";
                                }
                            }
                        }

                        fullQuery = fullQuery.replace(pathTarget, pathReplacement.substring(0,pathReplacement.length() - 5));
                        // ! cannot have "DISTINCT <path>" as path comparison is not supported
                        /*if(!fullQuery.contains("DISTINCT") && !fullQuery.contains("ORDER BY")) {
                            newQuery = "UNWIND $edges AS edges " + fullQuery.replace("RETURN", " RETURN DISTINCT");
                        } else {
                            newQuery = "UNWIND $edges AS edges " + fullQuery;
                        }*/
                        newQuery = "UNWIND $edges AS edges " + fullQuery;
                        rlist = toIntersect;
                    }
                }
                return iterateThroughEdges(newQuery, rlist,params);
            }


        } else {
            System.out.println("Before calling execute query");

            KuzuQueryResult result = KuzuGraphConnector.executeWithParam(fullQuery,params);
            long nowstart = System.currentTimeMillis();
            long numResults = result.getNumTuples();
            long duration = System.currentTimeMillis() - nowstart;
            System.out.println("There are " + numResults + " elements in the query");
            System.out.println("Took " + duration + " ms to process result rows");

            System.out.println("After the call to execute query");
        }

        // edit end from here

        return new HashMap<>();


    }

    public static void changeGraph(String command){
        //if this is called, then the change-graph has already walked through the parser
        System.out.println(vql.getFinalAffectedViews());

        //re-evaluate all necessary instants...
        Set<String> instantiations = new HashSet<>(vql.outdatedViews);
        vql.clearAll();
        for(String cmd : instantiations){
            vql.viewInstants.remove(cmd);
            ViewLexer VL = new ViewLexer(CharStreams.fromString(cmd));
            CommonTokenStream tokens = new CommonTokenStream(VL);
            ViewParser parser = new ViewParser(tokens);

            ParseTree tree = parser.root();
            walker.walk(vql, tree);

            long now = System.currentTimeMillis();
            //  processMainView(cmd, false); //todo uncomment. this actually re-evals but for correctness I just want to know what views are being re-evald
            //  System.out.println("TIME TO RE-EVAL VIEW: " + (System.currentTimeMillis()-now));

            totalTime += System.currentTimeMillis()-now;
        }

        System.out.println("TOTAL TIME FOR ALL: " + totalTime);
        totalTime = 0;

        vql.resetAfterGraphUpdate();

    }

    // TODO check what should be returned

    /**
     * Given a path query extract all node ids in the returned path
     * @param fullQuery "parametrized" path query using "UNWIND $edges" to execute
     * @param rlist list of edge ids for the path to match
     * @param params map of parameters and their values
     * @return map of path name and list of node ids
     * @throws KuzuObjectRefDestroyedException if ref destroyed during execution
     */
    public static HashMap<String,List<String>> iterateThroughEdges(String fullQuery, List<List<String>> rlist, Map<String, String> params) throws KuzuObjectRefDestroyedException {
        long start = System.currentTimeMillis();

        String rows = "";
        StringBuilder rowsSB = new StringBuilder();
        int numResults = 0;
        HashMap<String,List<String>> nodeIds = new HashMap<>();
        if(fullQuery.equals("")) return nodeIds ;

//        KuzuGraphConnector.getTransaction();
        //build a list of list:[[1,2],[2,3]] as edges
        params.put("edges",mergeRelsToString(rlist));
        KuzuQueryResult result = KuzuGraphConnector.executeWithParam(fullQuery,params);

        System.out.println(fullQuery);
        if (!result.isSuccess()) {
            System.out.println(result.getErrorMessage());
            System.exit(0);
        }

        KuzuFlatTuple row;


        long numColumns = result.getNumColumns();
        while (result.hasNext()){
            row = result.getNext();
            for (int i = 0; i < numColumns; i++){
                // key should be path name

                String key = result.getColumnName(i);

                if(key.contains("(")) {

                    key = key.substring(key.indexOf("(") + 1, key.indexOf(")"));
                }
                List<String> oneSet = nodeIds.containsKey(key) ? nodeIds.get(key) : new ArrayList<>();

                if(row.getValue(i).getDataType().getID() == KuzuDataTypeID.RECURSIVE_REL ) {
                    // for all relationships in the path, add source and destination ids to oneSet
                    KuzuValue rels = KuzuValueRecursiveRelUtil.getRelList(row.getValue(i));

                    for (int j = 0; j < KuzuValueListUtil.getListSize(rels); j++) {


                        KuzuValue rel = KuzuValueListUtil.getListElement(rels, j); //When this line gets run, we get a JRE
                        oneSet.add(KuzuGraphConnector.internalIdToString(KuzuValueRelUtil.getSrcID(rel)));
                        oneSet.add(KuzuGraphConnector.internalIdToString(KuzuValueRelUtil.getDstID(rel)));
                    }
                    nodeIds.put(key, oneSet);
                }else if(row.getValue(i).getDataType().getID() == KuzuDataTypeID.NODE){
                   oneSet.add(KuzuValueNodeUtil.getID(row.getValue(i)).toString());
                     nodeIds.put(key, oneSet);

                } else{
                    System.out.println("Exception: "  + row.getValue(i));
                }
            }
            numResults++;
        }
        //get rid of duplicates
        nodeIds.replaceAll((k, v) -> new ArrayList<>(
                new HashSet<>(v)));
        System.out.println("Number of results: " + result.getNumTuples());
        System.out.println("iterateThroughEdges took " + (System.currentTimeMillis() - start) + " to execute");
        return nodeIds;
    }

    /**
     * Method that transforms list of node ids to a string representing that list
     * e.g. [1,2,3] -> "["1","2","3"]"
     * @param map indexed by view, contains list of node ids for that view
     * @return map indexed by view, with list of node ids as a single string
     */
    public static Map<String, String> mergeMapIdsToString(Map<String, List<String>> map) {
        HashMap<String, String> idStringMap = new HashMap<>();
        for (String key : map.keySet()) {
            String listIds = String.join("\",\"", map.get(key));
            listIds = "[\"" + listIds + "\"]";
            idStringMap.put(key, listIds);
        }
        return idStringMap;
    }

    public static Map<String, String> mergeMapOffsetsToString(Map<String, List<Integer>> map) {
        HashMap<String, String> idStringMap = new HashMap<>();
        map.forEach((key, value) -> {
            String listIds = value.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", ", "[", "]"));
            idStringMap.put(key, listIds);
        });
        return idStringMap;
    }

    /**
     * Method that transforms list of node ids to a string representing that list
     * e.g. [1,2,3] -> "["1","2","3"]"
     * @param ids list of ids
     * @return string representation of list of ids
     */
    public static String mergeIdsToString(List<String> ids) {
        String listIds = String.join("\",\"", ids);
        listIds = "[\"" + listIds + "\"]";
        return listIds;
    }

    /**
     * Method that transforms list of list of edge ids to a string representing that list
     * e.g. [[1,2],[2,3]] -> "[["1","2"],["2","3"]]
     * @param relations list of list of edges
     * @return map indexed by view, with list of node ids as a single string
     */
    public static String mergeRelsToString(List<List<String>> relations) {
        List<String> flattenedRels = new ArrayList<>();
        // for every list of edges, put as string
        for (List<String> rel : relations) {
            String listIds = String.join("\",\"", rel);
            listIds = "[\"" + listIds + "\"]";
            flattenedRels.add(listIds);
        }
        // join list of strings into one big string
        String allRels = String.join(",", flattenedRels);
        allRels = "[" + allRels + "]";

        return allRels;
    }
}

