package main;

import gen.ViewBaseListener;
import gen.ViewParser;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class QueryParser extends ViewBaseListener {

    protected Set<String> viewsInSystem = new HashSet<>();

/**
 * First Section: All objects used for the DEPENDENCY TABLE
 * ********/
    //First: the dependency table.
    //Second: a list of labels that are affected: this will be added to during parsing and used at the end to
    //          attach all main.EntryData objects to the main.TableEntry object which are pointed to by the keys in this list.
    protected DependencyTable dependencyTable = new DependencyTable();
    protected List<String> labelsAffected = new LinkedList<>();
    final String NODESTARLABEL = "_ENTRY_SPECIAL_NODE_STAR_";
    final String RELSTARLABEL = "_ENTRY_SPECIAL_REL_STAR_";
    final String RELNONAME = "_ENTRY_SPECIAL_NO_REL_NAME_";

    private int REL_NO_NAME_COUNTER = 0;

    //required during parsing to create associations between variable names and their label key for the depend.table
    //the bottom two can ALSO be used for GRAPH UPDATES
    private Map<String, String> varLabels = new HashMap<>();
    private Map<String, Set<Condition>> varConditions = new HashMap<>(); //Stores variables and the set of conditions that are used.

    private Map<String, String> insertionVarLabels = new HashMap<>(); //The same as varLabels, but separate for insertion queries.

    //set of dependents for a view creation: this will always contain the view created and possibly more if the view refers to other views
    protected List<String> dependents;

/**
 * Second Section: All objects used to track GRAPH UPDATES
 * **/

    private Set<String> finalAffectedViews = new HashSet<>();

    private changeType change = changeType.DEFAULT;

    //First: Deletions
    String deletedVar = "";


    //Second: Updates
    List<EntryData> potentialAffected = new LinkedList<>();
    TableEntry affectedEntry = new TableEntry("");
    String affectedVar = "";
    String affectedAttribute = "";


    /**
     * PART 2.5: GRAPH INSERTION DATA STRUCTURE: SIMILAR TO varConditions BUT THIS IS ONLY FOR THE NEWLY CREATED GRAPH OBJECTS
     * */
    private Map<String, Set<Condition>> insertedAttributes = new HashMap<>();



    //Third: Insertions


    //Fourth: in the case of comparisons we have a list similar to varLabels and insertionVarLabels, except we use it to determine which types of views (upon init) MUST always
    //be reevaluated.
    private Map<String, SetTuple<String, HashSet<String>>> alwaysReevaluate = new HashMap<>(); //


    /**
     * Third section, miscellaneous tracking objects
     */


    private String setRemoveCGToken = ""; //local to exitChangeGraph, can be declared locally
    public List<String> outdatedViews = new LinkedList<>();
    private String setRemoveCGType = ""; //necessary for communication between View

    //View table - stores all views that return sets of nodes and the nodes that it should contain
    //key : view name. value : set of node identifiers

    public Set<String> viewInstants = new HashSet<>();

    public Set<String> orClauseViews = new HashSet<>();


    private String viewName;
    private boolean isViewInstant = false;
    private boolean isViewUse = false;

    private String returnValExpr = "";
    private String pathName = "";
    private LinkedList<ViewParser.BoolexprContext> conditions = new LinkedList<ViewParser.BoolexprContext>();

    private boolean viewScope = true; //true -> scope is purely the view for viewusage. default. false -> global db scope

    /*
     *  The following two tables are symbol tables used for view usage. Proper query syntax assumes that
     *  symbols are not repeated so this is not enforced in the middle-ware.
     *
     *  Upon a view usage, we must check whether the user wants to restrict the usage to the view or
     *  use entities of the view in a general query on the graph data-base.
     *
     * An example of a query restricted to a view:
     *       USE VIEW myPathView MATCH  p = (n:Person)-[:Follows]-(m:Person) WHERE n.age > 18 RETURN ...
     *   - Not specifying that p IN myPathView indicates that the user wants all paths that satisfy the age condition
     *       to be in the view myPathView. The Follows relationship must also be contained within the view. Essentially
     *       this query first retrieves all paths then filters by the age.
     *
     * An example of a query non-restricted to a view: (this is global)
     *       USE VIEW myPathView WITH VIEWS secondPathView MATCH (n:Person)-[:Follows]-(m:Person) WHERE n IN myPathView
     *           AND m IN secondPathView RETURN ...
     *   - This query means that while n must be a node contained within paths inside myPathView, we are interested in a
     *       general query over the whole graph database, but we want to begin with only nodes n,m inside myPathView and
     *       secondPathView. The two views used do not necessarily need to be path views; they can be node views as well.
     *       However this is supported with path views as well for added flexibility, since sometimes a user may want
     *       a generalized view that can be used for multiple purposes.
     *
     */
    private List<String> relSymbols = new LinkedList<>();
    private List<String> nodeSymbols = new LinkedList<>();


    /*
     * Following is used only to make sure the View.java class handled re-writing of view usage query
     */
    private boolean containsWhere = false;

    private Map<String, Set<ViewParser.BoolexprContext>> symbolTable = new ConcurrentHashMap<String, Set<ViewParser.BoolexprContext>>();
    //^Temporary. Upon exiting, it will attach these to meta-data table entries

    // TODO: Make sure the case PATHNODES is even used in the code and how it is used
    enum retType{
        DEFAULT,
        PATH,
        EDGE,
        PATHNODES,
        NODE,
        GRAPH
    }

    enum changeType{
        DELETION,
        INSERTION,
        UPDATE,
        DEFAULT
    }

    private retType returnType = retType.DEFAULT;

    private LinkedList<String> thisQueryViews = new LinkedList<String>();

    //Below: Used for view use. Mainly to re-write the node queries
    //Maps node identifiers to a set of views
    //
    // "MATCH (n)-[r]-(m) WHERE n IN view1" -> MATCH (n)-[r]-(m) WHERE id(n) IN [1, 2, ...]"
    // map would contain: key - n, value -view1
    public Map<String, Set<String>> addWhereClause = new ConcurrentHashMap<String, Set<String>>();

    private LinkedList<String> usedViews = new LinkedList<String>();

    public String changeGraphQuery = "";
    private boolean cg = false;


    @Override
    public void enterRoot(ViewParser.RootContext ctx){

        System.out.println("In enterRoot ctx is " + ctx.getText());
        if(ctx.getChild(0).getText().contains("WITH VIEWS")){
            //viewUse
            isViewUse = true;

            if (ctx.getText().contains("GLOBAL")) {
                viewScope = false; //set to global
            }
        }
        else if(ctx.getChild(0).getText().equals("CG") || ctx.getText().contains("SET") ||
                ctx.getText().contains("DELETE") ||
                ctx.getText().contains("REMOVE") ||
                (ctx.getText().contains("CREATE") && !ctx.getText().contains("VIEW AS"))){

            //Change Graph
            cg = true;
            System.out.println("Set cg to true");

            changeGraphQuery = ctx.getChild(0).getText();
            return;
        }
        else {
            //view init
            TerminalNode node = ctx.COMMAND();
            String name = node.getText();


            if (name.equals("CREATE VIEW AS")) {
                isViewInstant = true;

                int a = ctx.start.getStartIndex();
                int b = ctx.stop.getStopIndex();
                Interval interval = new Interval(a, b);
                String viewSql = ctx.start.getInputStream().getText(interval);
                System.out.println("ViewSql is " + viewSql);

                //Storing it in case we need to re-execute this view
                viewInstants.add(viewSql);
            }


            viewName = ctx.NAME().getText();
            usedViews.add(viewName);
            viewsInSystem.add(viewName);


            //Adding this View as an entry to the dependency table (this entry contains no conditions by itself.)
            dependencyTable.put(viewName, new TableEntry(viewName));
            dependents = new LinkedList<>();
            dependents.add(viewName);
        }

    }

    @Override
    public void exitRoot(ViewParser.RootContext ctx){

        System.out.println("VarLables of the input is " + varLabels);
        System.out.println("Return Type is " + returnType);

    }

    @Override
    public void enterChangegraph(ViewParser.ChangegraphContext ctx){

        String queryText = ctx.getText();
        if(queryText.contains("CREATE")) change = changeType.INSERTION;
        if(queryText.contains("DELETE")) {
            change = changeType.DELETION;
            deletedVar = ctx.getChild(4).getText();
        }
        if(queryText.contains("SET")){
            change = changeType.UPDATE;
            parseUpdate((ViewParser.SetattrContext) ctx.getChild(4));
        }
        if(queryText.contains("REMOVE")) {
            change = changeType.UPDATE;
            parseRemove((ViewParser.AttributeContext)ctx.getChild(4));
        }


    }

    @Override
    public void exitChangegraph(ViewParser.ChangegraphContext ctx){

        //At this point, all information should be ready for comparison.
        /*
        * Here is a list of information we should have acquired by now:
        *   1) Nodes/Relationships, and the labels associated with them in the changegraph
        *   2) if delete, then the node being deleted
        *   3) if creation, then the set of nodes/rels from the query and the set of nodes/rels in the creation
        *   4) if update, then the node/rel, attribute, and new value
        *
        *
        *
        * */


        switch(change){

            case DEFAULT:{
                System.out.println("Warning: graph change detected but not any of the possible changes");
                break;
            }

            case DELETION:{
                //In this case, we look at the token being deleted and associate it using varLabels and varConditions


                //then varConditions and varLabels contain those in the query for the graph change.
                //out of these, we only select the label that is in the delete (this part has been done by this step by the helper method @parseUpdate(Boolexprcontext)

                affectedVar = deletedVar;
                affectedEntry = dependencyTable.get(varLabels.get(affectedVar));



                //The step should be done in one part only: since a delete will only be on a single node or rel, then
                //it is safe to only consider the conditions attached to the deleted node/rel. Hence why we use affectedVar only.
                //With the conditions attached to affectedVar, we look for the set of conditions in all EntryNodes to see which EntryNodes are affected.


                if(affectedEntry == null){
                    //no way it is affected.
                    return;
                }

//              Otherwise we look for the conditions we have

                Set<String> referencedViews = new HashSet<>();

                Set<Condition> conditionList = varConditions.get(affectedVar);

                Set<EntryData> affectedEntryDatas = affectedEntry.filterIrrelevantEntryData(conditionList, orClauseViews, referencedViews);

                finalAffectedViews.addAll(referencedViews);

                for(EntryData d : affectedEntryDatas){
                    finalAffectedViews.addAll(d.dependents);
                }


                //final part to handle views that we almost always re-evaluate due to tough conditions ("a.attr1 > b.attr2")
                if(alwaysReevaluate.containsKey(varLabels.get(affectedVar))){

                    SetTuple<String, HashSet<String>> viewAndConditions = alwaysReevaluate.get(varLabels.get(affectedVar));
                    Set<String> listOfAttr = viewAndConditions.y;

                    for(Condition c : conditionList){
                        for(String s : listOfAttr){
                            if(c.attribute.equals(s)){
                                //reevaluate
                                finalAffectedViews.add(viewAndConditions.x);
                            }
                        }
                    }
                }

                break;
            }

            case UPDATE:{



                //affectedVar should be already set by @parseUpdate earlier.
                affectedEntry = dependencyTable.get(varLabels.get(affectedVar));

                if(affectedEntry == null){
                    //no way it is affected.
                    return;
                }


                //For an update, any view that contains a condition on the affected attribute must be updated - unless we find it to not be affected by
                //the conditions in the query. For this part, we can use the same method (filterIrrelevantEntryData).

                Set<String> referencedViews = new HashSet<>();

                Set<Condition> conditionList = varConditions.get(affectedVar);
                Set<EntryData> affectedEntryDatas = affectedEntry.filterIrrelevantEntryData(conditionList, orClauseViews, referencedViews);

                //filterIrrelevantEntryData will fill referencedViews with views that must be updated due to the OR conditions.
                finalAffectedViews.addAll(referencedViews);

                //Now that we have this, we only continue if the update itself applies to these main.EntryData

                for(EntryData d : affectedEntryDatas){
                    if(d.containsConditionOnAttribute(affectedAttribute)) {
                        finalAffectedViews.addAll(d.dependents);
                        System.out.println("added to dependents: " + d.dependents);
                    }
                }

                //final part to handle views that we almost always re-evaluate due to tough conditions ("a.attr1 > b.attr2")

                if(alwaysReevaluate.containsKey(varLabels.get(affectedVar))){

                    SetTuple<String, HashSet<String>> viewAndConditions = alwaysReevaluate.get(varLabels.get(affectedVar));
                    Set<String> listOfAttr = viewAndConditions.y; // example: [userid, reputation, ... ]

                    //if the graph change has no conditions on the removed attribute, then below will be null. In this case, then we have to
                    //re-evaluate as long as any view had a condition on the removed attribute.
                    if(conditionList == null){
                        for(String s : listOfAttr){
                            if(s.equals(affectedAttribute)){
                                finalAffectedViews.add(viewAndConditions.x);
                            }
                        }
                    }
                    else {
                        //below: conditionList helps filter out for those that do not match condition.
                        for (Condition c : conditionList) {
                            for (String s : listOfAttr) {
                                if (c.attribute.equals(s)) {
                                    //reevaluate
                                    finalAffectedViews.add(viewAndConditions.x);
                                }
                            }
                        }
                    }
                }


                break;
            }

            case INSERTION:{


                /*
                * Steps required:
                *   1) All variables in the original query (found in varLabels as keys)
                *   2) All variables in the inserted expression (found in insertedVarLabels as keys)
                *   3) Set difference (2)-(1) for NEWLY CREATED EXPRESSIONS
                *   4)      For all variables in (3), refer back to insertedVarLabels
                *           Some of these will have certain attributes. Need a structure to contain these attributes
                *   5) Match these against the main.TableEntry that are in the main.DependencyTable.
                *   6) For all matches, see if an main.EntryData contains a condition on any attribute found in (4).
                *   7) Invalidate all main.EntryData found in (6).
                * */

                //can optimize by converting these into HashSets for O(1) contains() complexity
                Set<String> queryKeys = varLabels.keySet();
//                System.out.println("Query vars: " + queryKeys);

                Set<String> insertedKeys = insertionVarLabels.keySet();

                insertedKeys.removeAll(queryKeys);

                //Unlike deletions and updates, MULTIPLE vars are affected here, so we must repeat the process for all variables in insertedKeys
                for(String var : insertedKeys){

                    if(insertionVarLabels.get(var)==null || !dependencyTable.containsKey(insertionVarLabels.get(var))) continue;

                    affectedEntry = dependencyTable.get(insertionVarLabels.get(var));

                    if(affectedEntry == null){
                        //no way it is affected.
                        continue;
                    }


                    //For an insertion, we have a slightly different criteria: We are interested in the values of the inserted attributes for each var
                    //and we only mark all EntryDatas whose conditions contain ALL attributes (with SATisfaction) on these attributes (since we assume all
                    //conditions are joined with an AND). (This is also because on an insertion we are assuming any non-specified attribute is NULL by default!)


                    /**
                     * Nevertheless we should be wary of these two cases
                     *
                     * CASE: The insertion does not include any attributes for which a view contains an attribute condition on.
                     *
                     *  For the second possibility, there are two sub-cases of possibilities.  First, the view may have zero
                     *   attribute conditions for the main.EntryData - in this case, we treat it similarly to the node* entry and immediately invalidate it.
                     *  Second, the view may contain other attribute conditions that are not on the
                     *   same attributes that were included during the insertion - in this case, we may safely assume that the
                     *  view in question does not require reevaluation.
                     *
                     * */


                    //At this point, all attributes and their variable are in insertedAttributes
                    Set<String> referencedViews = new HashSet<>();


                    Set<Condition> conditionList = insertedAttributes.get(var);
                    Set<EntryData> affectedEntryDatas = affectedEntry.filterWithInsertion(conditionList, orClauseViews, referencedViews);

                    finalAffectedViews.addAll(referencedViews);

                    //Now that we have this, we must continue since these are marked

                    for(EntryData d : affectedEntryDatas){
                        finalAffectedViews.addAll(d.dependents);
                    }


                    //final part to handle views that we almost always re-evaluate due to tough conditions ("a.attr1 > b.attr2")
                    if(alwaysReevaluate.containsKey(varLabels.get(affectedVar))){

                        SetTuple<String, HashSet<String>> viewAndConditions = alwaysReevaluate.get(varLabels.get(affectedVar));
                        Set<String> listOfAttr = viewAndConditions.y;

                        for(Condition c : conditionList){
                            for(String s : listOfAttr){
                                if(c.attribute.equals(s)){
                                    //reevaluate
                                    if(!finalAffectedViews.contains(viewAndConditions.x)) {
                                        finalAffectedViews.add(viewAndConditions.x);
                                        System.out.println("inv");
                                    }
                                }
                            }
                        }
                    }


                }

                break;
            }

        }


        //For all nodestar and relstar entries, we have to re-evaluate.

        if(dependencyTable.containsKey(NODESTARLABEL)){
            TableEntry te = dependencyTable.get(NODESTARLABEL);


            //this contains NODESTAR entries (i.e, nodes or rels that contain no label info). Deletions and Insertions MUST
            //trigger an always-reevaluate, but an update may not necessarily do so. TODO put this in writing

            if(change == changeType.UPDATE){
                //Don't check for conditions on the change; we should probably do this too.
                //Then check for all the entryData related to the tableentry if conditions match.
                for (EntryData e : te.getEntries()){
                    if(e.containsConditionOnAttribute(affectedAttribute)){
                        finalAffectedViews.addAll(e.dependents);
                    }
                }
            }
            else {
                for (EntryData e : te.getEntries()) {
                    finalAffectedViews.addAll(e.dependents);
                }
            }
        }
        if(dependencyTable.containsKey(RELSTARLABEL)){

            TableEntry te = dependencyTable.get(RELSTARLABEL);

            if (change == changeType.UPDATE){
                //todo is this the same as for nodestarlabel idk? [Mohanna: What should we do about it?]
            }
            else {
                for (EntryData e : te.getEntries()) {
                    finalAffectedViews.addAll(e.dependents);
                }
            }
        }

        for(String affected : finalAffectedViews) {
            for (String query : viewInstants) {
                if (query.contains("CREATE VIEW AS " +affected)){
                    outdatedViews.add(query);
                }
            }
        }

    }

    @Override
    public void enterRetval(ViewParser.RetvalContext ctx){
        if(isViewUse || cg) return;

        if(ctx.getText().contains("NODES(")){
            returnType = retType.PATHNODES;
        } else if(ctx.getText().contains("GRAPH")){
            returnType = retType.GRAPH;
        } else if (!ctx.attribute().isEmpty()){

            if(varLabels.containsKey(ctx.attribute().getText())) {
                if (!returnValExpr.equals("")) returnValExpr += ",";
                returnValExpr += ctx.attribute().getText();
                if(relSymbols.contains(ctx.attribute().getText()))
                	returnType = retType.EDGE;
                else
                	returnType = retType.NODE;
            }
            else{ //if its not a node or relationship, its gotta be the path
                returnValExpr = ctx.attribute().getText();
                returnType = retType.PATH;

            }
        }
        else{
            if(varLabels.containsKey(ctx.attribute().getText())) {
                if (!returnValExpr.equals("")) returnValExpr += ",";
                if (varLabels.containsKey(ctx.attribute().getText())) returnValExpr += ctx.NAME().getText();
                returnType = retType.NODE;
            }
        }

    }

    // Check if the query is of PATH type and if so set retType to PATH
    @Override
    public void enterQuery(ViewParser.QueryContext ctx){

        //Comparing returnexpr and the path (if it exists).

        //if(isViewUse || cg) return;
    	if(cg) return;

        //System.out.println("Query context is " + ctx.getText());
        // TODO: No reason to keep this line here, because returnContext is never used
        ViewParser.ReturnstmtContext returnContext = ctx.returnstmt();
        //System.out.println(returnContext.getText());
        //System.out.println("First child of queryCtx is " + ctx.getChild(1).getText());

        // Checking for the PATH query type
        if (ctx.getChild(1) instanceof ViewParser.PathContext) {
        	ViewParser.PathContext pathctx = (ViewParser.PathContext)ctx.getChild(1);
            pathName = pathctx.NAME().getText();;
            // TODO: I'm suspicious of the condition check below
            if(pathName.equals(returnValExpr)) returnType = retType.PATH;
        }
        /*else {
            //no pathname
        }*/

    }

    @Override
    public void exitQuery(ViewParser.QueryContext ctx){
        if(isViewUse || cg) return;
//        metaDataTable.get(viewName).setQuery(ctx);
    }

    @Override
    public void enterPath(ViewParser.PathContext ctx){
        if(isViewUse || cg) return;
        pathName = ctx.expr().getText();
        if(pathName.equals(returnValExpr)) returnType = retType.PATH;
    }


    @Override
    public void enterVariable(ViewParser.VariableContext ctx){

        // In the case of a view init:
        // Here we tag all variables with their labels (if any!). At the end of parsing, we can associate
        // each variable with their conditions as well.

        // System.out.println("Variable:" + ctx.getText() + ", " + ctx.getChildCount());

        if(isViewInstant || cg) {
            int numChildren = ctx.getChildCount();
            String nodeName = "";
            String nodeLabel = "";

            if(numChildren == 3){
                //then we only have nodeName
                // view itself contains nodes not specified with label conditions (in which case, the view is expected to contain a large set of nodes).
                //this falls under the special node* entry.
                nodeName = ctx.getChild(1).getText();
                nodeLabel = NODESTARLABEL;


            }
            if(numChildren == 5){
                //we have a label associated with nodeName
                nodeName = ctx.getChild(1).getText();
                nodeLabel = ctx.getChild(3).getText();
            }

            // We don't put duplicate lables in 'labelsAffected'
            if(!labelsAffected.contains(nodeLabel)) labelsAffected.add(nodeLabel);

            varLabels.put(nodeName, nodeLabel); //at the end, we will have a bunch and then re-organize by labels.

        }

        if(isViewUse || cg){
            nodeSymbols.add(ctx.nodeName().getText());
        }
    }

    @Override
    public void enterViewatom(ViewParser.ViewatomContext ctx){
        // Not used in this version of implementation
    }

    @Override
    public void exitViewatom(ViewParser.ViewatomContext ctx){
        // Not used in this version of implementation
    }

    @Override
    public void enterRelation(ViewParser.RelationContext ctx){

        if (ctx.getChildCount()==0) return;

        String relVar = "";
        String relLabel = "";

        // Step 1: Check if relVar is provided (Check this by searching if anything is there before ':' in ctx)
        if (ctx.getText().contains(":") && !ctx.getText().substring(0, ctx.getText().indexOf(':')).isEmpty()){
            // System.out.println("Rel val is provided with Type");
            relVar = ctx.relationValue().getText();
            relSymbols.add(relVar);
        } else if (!ctx.getText().contains(":") && ctx.getText().contains("*") && !ctx.getText().substring(0, ctx.getText().indexOf('*')).isEmpty()){
            // System.out.println("Rel val is provided with relation length");
            relVar = ctx.relationValue().getText();
            relSymbols.add(relVar);
        } else if (!ctx.getText().contains(":") && !ctx.getText().contains("*") && ctx.getChildCount() == 1) {
            // System.out.println("There is only the relVar provided");
            relVar = ctx.relationValue().getText();
            relSymbols.add(relVar);
        }
        else {
            // System.out.println("No rel Val Provided");
            REL_NO_NAME_COUNTER ++ ;
            relVar = RELNONAME + REL_NO_NAME_COUNTER;
        }

        // TODO: Not sure how relLabel should be saved for USE command -> Right now saving String TYPE_1|TYPE_2|TYPE_3|...|TYPE_N
        // Step 2: Check if Relation Type is provided (Only if there is ":" in ctx) and set relLabel
        if (ctx.getText().contains(":")) {
            // Having both relation type and length
            if (ctx.getText().contains("*")) {
                relLabel = ctx.getText().substring(ctx.getText().indexOf(':')+1, ctx.getText().indexOf('*'));
            }
            // Only having relation type and no length
            else {
                relLabel= ctx.getText().substring(ctx.getText().indexOf(':')+1);
            }
        } else {
            // There is no relation type provided
            relLabel = RELSTARLABEL;
        }

        if (relLabel.contains("|")) {
            // There are multiple relation types provided
            // Each one is added to varLabels seperately
            String[] types = relLabel.split("\\|");
            for (String t: types) {
                System.out.println(t);
                // if relVar starts with 'RELNONAME' =>
                    // make sure that another RELNONAME relation with the same label as value doesn't exist
                if (relVar.startsWith(RELNONAME) && !varLabels.containsValue(t)) {
                    // Incrementing the counter for each substring
                    varLabels.put(relVar, t);
                    REL_NO_NAME_COUNTER ++;
                    relVar = RELNONAME + REL_NO_NAME_COUNTER;
                }
                else if (!relVar.startsWith(RELNONAME)) {
                    varLabels.put(relVar, t);
                }
            }
        }
        // There is only one type provided for the relation
        else {
            // if relVar starts with 'RELNONAME' =>
            // make sure that another RELNONAME relation with the same label as value doesn't exist
            if (relVar.startsWith(RELNONAME) && !varLabels.containsValue(relLabel)) {
                varLabels.put(relVar, relLabel);
            }
            else if (!relVar.startsWith(RELNONAME)) {
                varLabels.put(relVar, relLabel);
            }
        }

    }

    @Override
    public void enterUsedviews(ViewParser.UsedviewsContext ctx){

        if(cg) return;


        List<TerminalNode> used = ctx.NAME();
        for ( TerminalNode node : used){
            usedViews.add(node.getText());
        }
    }

    public void createDependencies(ViewParser.ViewatomContext ctx){

    }

    @Override
    public void enterConditions(ViewParser.ConditionsContext ctx){

        if(isViewUse) {

            if(ctx.getChildCount()>0){
                containsWhere = true;
            }
        }

    }

    @Override
    public void exitConditions(ViewParser.ConditionsContext ctx){

        if(isViewUse || cg) return;

        //For deletions/updates, we handle parsing of conditions inside @enterBoolExpr.
        //For deletions and updates, we create the set of affected views inside @exitChangegraph


        //at this point all conditions should be set up in the table varConditions. (if it is a creation)
        //Below is code for handling view creation: this will add the relevant entries to the tables and structures used for the table later.

        Set<String> keys = varConditions.keySet(); //we iterate now through the list for all nodes and relationships such that
                                                    //they contain a condition

        keys = varLabels.keySet();
        for(String key : keys){

            Set<Condition> conditionList = varConditions.get(key);
            List<Condition> list = new LinkedList<>();
            if(conditionList!=null) list.addAll(conditionList);

            //just converting to a list from a set^


            //Check if there already exists an main.EntryData with the EXACT SAME CONDITIONS
            String label = varLabels.get(key); //this is the label used to search dependency table, for example :Person

//            System.out.println(key);

            if(dependencyTable.get(label)==null || dependencyTable.containsNoEntryData(label)){
                //if we are here, then there is no previous entry for this label at all: we can safely populate table without makign it messy.
                EntryData newEntryData = new EntryData();
                newEntryData.setConditions(list);
                newEntryData.setDependents(dependents);

                if(!dependencyTable.contains(label)){
                    dependencyTable.put(label, new TableEntry(label));
                }
                dependencyTable.get(label).addEntry(newEntryData);

//                System.out.println("Here");

            }
            else{
                //this entry already has a main.TableEntry

                //so we compare ALL main.EntryData from this tableentry and see if one has EXACT same conditions..

                if (dependencyTable.get(label).addSameDependents(conditionList, dependents)){
                    //nothing to do here, it has been done in the method. probably should just write "if !..." for clarity
                }
                else{
                    //as with above, then there is no existing entry so we add a new entry to the existing main.TableEntry

                    EntryData entryData = new EntryData();
                    entryData.setConditions(list);
                    entryData.setDependents(dependents);


                    dependencyTable.get(label).addEntry(entryData);


                }

            }
        }
    }

    @Override
    public void enterBoolexpr(ViewParser.BoolexprContext ctx){


        if(isViewUse) {

            //Using a view: this catches the case when we see "n IN v1"

            if(ctx.getText().contains("IN") && !ctx.getText().contains("AND") && !ctx.getText().contains("OR")) {
                String viewNodeName = ctx.NAME(0).getText(); //not necessarily node. can be path symbol too
                String viewUsedName;
                if(ctx.getText().contains("."))
                	viewUsedName = ctx.NAME(1).getText() + "." + ctx.NAME(2).getText();
                else
                	viewUsedName = ctx.NAME(1).getText();

                assert viewsInSystem.contains(viewUsedName);
                assert usedViews.contains(viewUsedName);

                if (addWhereClause.containsKey(viewNodeName)) {
                    addWhereClause.get(viewNodeName).add(viewUsedName);
                } else {
                    addWhereClause.put(viewNodeName, new HashSet<String>());
                    addWhereClause.get(viewNodeName).add(viewUsedName);
                }
                
                
                
            }

        }


        //Keep LHS of each condition inside the symbolTable
        else {
            //OR clause
            if(ctx.getText().contains("OR") && isViewInstant){
                if(!orClauseViews.contains(viewName)) orClauseViews.add(viewName);
            }

            //handling the exist cases
            if(ctx.getText().toLowerCase().contains("exists(")){
                return;
            }

            //ignoring the "not ctx" cases
            if(ctx.getText().toLowerCase().startsWith("not")) return;
            String sf =ctx.getText().toLowerCase();
            ParseTree child1 = ctx.getChild(0);
            ParseTree child2 = ctx.getChild(2);

            Object payload1 = child1.getPayload();
            Object payload2 = null;
            if(child2 != null) payload2 = child2.getPayload();
            //A note about these payloads... it is actually valid for us to have something like,
            //"WHERE a.name = b.name"
            //In this case (not yet covered in writing), we should have some default condition on name such that it is triggered whenever a change on name
            //appears. todo.

            if (payload1 instanceof ViewParser.AttributeContext) {

                ViewParser.AttributeContext aactx = ((ViewParser.AttributeContext) payload1);

                String keyname = attributeParse(aactx).split("\\.")[0];
                String attributename = "";
                if(attributeParse(aactx).contains(".")) attributename = attributeParse(aactx).split("\\.")[1];


                if (payload2 instanceof ViewParser.ValContext){
                    String rhsVal = ((ViewParser.ValContext)payload2).getText();

                    Condition condition = new Condition();
                    condition.setAttribute(attributename);
                    condition.setConditionString(attributename + ctx.getChild(1).getText()+rhsVal);

                    if(!varConditions.containsKey(keyname)) varConditions.put(keyname, new HashSet<Condition>());
                    varConditions.get(keyname).add(condition);


                }

                if (payload2 instanceof ViewParser.AttributeContext){

                    ViewParser.AttributeContext actx = ((ViewParser.AttributeContext)payload2);

                    String attributeInvolved = attributeParse(actx);

                    String rhsKey = attributeInvolved.split("\\.")[0];

                    //a.name = b.bio
                    //x.name > y.name
                    //hold a separate structure ; whenever name, bio, name, name for labels related to a,b,x,y are involved, then re-evaluate.

                    if(!alwaysReevaluate.containsKey(varLabels.get(keyname))) alwaysReevaluate.put(varLabels.get(keyname), new SetTuple<>(viewName,new HashSet<>()));
                    if(!alwaysReevaluate.containsKey(varLabels.get(rhsKey))) alwaysReevaluate.put(varLabels.get(rhsKey), new SetTuple<>(viewName,new HashSet<>()));

                    if(attributeInvolved.contains(".")) {
                        String rhsAttribute = attributeInvolved.split("\\.")[1];
                        if(!alwaysReevaluate.get(varLabels.get(keyname)).y.contains(attributename)) alwaysReevaluate.get(varLabels.get(keyname)).y.add(attributename);
                        if(!alwaysReevaluate.get(varLabels.get(rhsKey)).y.contains(rhsAttribute)) alwaysReevaluate.get(varLabels.get(rhsKey)).y.add(rhsAttribute);
                    }
                    

                    //System.out.println("ckqpt" + varLabels.get(keyname) + ", " + attributename + "\n" + varLabels.get(rhsKey) + ", " + rhsAttribute);

                }


            }
        }
    }

    @Override
    public void enterInsertionVar(ViewParser.InsertionVarContext ctx){
        /*
        TODO: (n:Person { attributes here } )
        need method to handle pairList and attach those as "conditions" for the insertion
        */


        if(cg) {

            int numChildren = ctx.getChildCount();
            String nodeName = "";
            String nodeLabel = "";

            if(numChildren == 3){
                //then we only have nodeName
                // view itself contains nodes not specified with label conditions (in which case, the view is expected to contain a large set of nodes).
                //this falls under the special node* entry.
                nodeName = ctx.getChild(1).getText();
                nodeLabel = NODESTARLABEL;
            }
            if(numChildren == 5){
                //we have a label associated with nodeName
                nodeName = ctx.getChild(1).getText();
                nodeLabel = ctx.getChild(3).getText();
            }
            if(numChildren == 8){
                //attributes have been inserted
                nodeName = ctx.getChild(1).getText();
                if(ctx.getChild(2).getText().equals(":")){
                    nodeLabel = ctx.getChild(3).getText();
                }
                handleInsertion(nodeName, (ViewParser.InsertAttributesContext) ctx.getChild(5).getPayload());
            }

            insertionVarLabels.put(nodeName, nodeLabel); //at the end, we will have a bunch and then re-organize by labels.
//            System.out.println("put into changes: " + nodeName + ", " + nodeLabel);

        }

    }

    @Override
    public void enterInsertrelation(ViewParser.InsertrelationContext ctx){

        if(cg){

            int numChildren = ctx.getChildCount();
            String rName = "";
            String rLabel = "";

            if(numChildren == 1){
                //then we only have rName
                //if this is a changeGraph then either this is an unlabeled relationship (RELSTARLABEL) or it refers to a rel already in the query
                //in case it is the first, we must mark it with RELSTARLABEL (and if it is not, then we will not end up using this mark)
                rName = ctx.getChild(0).getText();
                rLabel = RELSTARLABEL;
            }
            if(numChildren == 3){
                //then we only have rName
                //if this is a changeGraph then either this is an unlabeled relationship (RELSTARLABEL) or it refers to a rel already in the query
                //in case it is the first, we must mark it with RELSTARLABEL (and if it is not, then we will not end up using this mark)
                rName = ctx.getChild(0).getText();
                rLabel = ctx.getChild(2).getText();
            }
            insertionVarLabels.put(rName, rLabel);
        }


    }

    public void handleInsertion(String insertedVarName, ViewParser.InsertAttributesContext ctx){

        //this is used to handle the inserted attributes. in particular we attach attribute key-value pairs to the variable "insertedVarName"
        //this method is recursive (check if there are more) since ctx is technically a pointer containing pointers.
        int numChildren = ctx.getChildCount();

        if(!insertedAttributes.containsKey(insertedVarName)) insertedAttributes.put(insertedVarName, new HashSet<>());

        String attributeName = ctx.NAME().getText();
        String attributeValue = ctx.getChild(2).getText();

        Condition newCondition = new Condition();
        newCondition.setAttribute(attributeName);
        newCondition.setConditionString(attributeName+"="+attributeValue);

        insertedAttributes.get(insertedVarName).add(newCondition);


        if(numChildren > 3){
            handleInsertion(insertedVarName, (ViewParser.InsertAttributesContext)ctx.getChild(4));
        }

    }

    //Helper method for UPDATE change-graphs that will keep track of the update
    public void parseUpdate(ViewParser.SetattrContext ctx){
        //ASSUMES THE FORMAT: a.x = b(.y)?
        //KEYWORD expr conditions 'SET' boolexpr
        /*
        attribute '=' attribute |
              attribute '=' val
         */

        assert ctx.getText().contains("=");

        boolean doWeInvalidateOnSameAttribute = false;

        String assignment = ctx.getText();

        String LHS = assignment.split("=")[0];
        String RHS = assignment.split("=")[1];

        String varNameLHS = LHS.split("\\.")[0];
        String attributeNameLHS = LHS.split("\\.")[1];

        String varNameRHS = "";
        String attributeNameRHS = "";

        if(ctx.getChild(2).getPayload() instanceof ViewParser.AttributeContext) {
            doWeInvalidateOnSameAttribute = true; //any attribute condition of a view on the same attribute will be flagged
            varNameRHS = RHS.split("\\.")[0];
            attributeNameRHS = RHS.split("\\.")[1];
        }


        String affected = varLabels.get(varNameLHS);
        if(affected != null) affectedEntry = dependencyTable.get(affected);

        affectedVar = varNameLHS;
        affectedAttribute = attributeNameLHS;



    }

    public String attributeParse(ViewParser.AttributeContext ctx){

        if(ctx.getChildCount()==1){
            return ctx.getText();
        }
        if(ctx.getChild(1).getText().equals(".")){
            //NAME . NAME
            return ctx.getText();
        }
        if(ctx.getChildCount()==3){

            if (ctx.getChild(0).getPayload() instanceof ViewParser.AttributeContext){
                return attributeParse((ViewParser.AttributeContext)ctx.getChild(0).getPayload());
            }
            if(ctx.getChild(2).getPayload() instanceof ViewParser.AttributeContext){
                return attributeParse((ViewParser.AttributeContext)ctx.getChild(2).getPayload());
            }

        }
        return "ERROR";

    }

    //HELPER METHOD for REMOVE update graph changes
    public void parseRemove(ViewParser.AttributeContext ctx){
        //assumes format REMOVE x.attr

        assert ctx.getText().contains(".");
        String varName = ctx.getText().split("\\.")[0];
        String attrName = ctx.getText().split("\\.")[1];

        affectedVar = varName;
        affectedAttribute = attrName;

        System.out.println(affectedVar + ",:"+affectedAttribute);

    }

    public boolean getViewScope(){
        return viewScope;
    }

    public void clearAll(){
        /* first these three*/
        labelsAffected = new LinkedList<>();
        varConditions = new HashMap<>();
        varLabels = new HashMap<>();


        insertionVarLabels = new HashMap<>();

        finalAffectedViews = new HashSet<>();
        change = changeType.DEFAULT;
        deletedVar = "";
        affectedVar = "";


        affectedEntry = new TableEntry("");
        affectedAttribute = "";
        //does not clear viewInstants; this should be called separately

        insertedAttributes = new HashMap<>();


        usedViews = new LinkedList<String>();
        thisQueryViews = new LinkedList<String>();

        relSymbols = new LinkedList<>();
        nodeSymbols = new LinkedList<>();

        viewName = "";
        isViewInstant = false;
        isViewUse = false;
        viewScope = true;


        returnValExpr = "";
        pathName = "";
        returnType = retType.DEFAULT;
        symbolTable = new ConcurrentHashMap<String, Set<ViewParser.BoolexprContext>>();
        addWhereClause = new ConcurrentHashMap<>();

        setRemoveCGToken = "";
        setRemoveCGType = "";

        cg = false;
        changeGraphQuery = "";
        containsWhere = false;


        removeOutdated();
    }

    public void changeGraph(){
        clearAll();

    }

    public void removeOutdated(){
        outdatedViews = new LinkedList<>();
    }

    public Set<String> getViewInstants(){
        return viewInstants;
    }

    public String getViewName(){
        return viewName;
    }
    
    public String getPathName(){
        return pathName;
    }

    public boolean containsWhere(){
        return containsWhere;
    }

    public boolean isViewInstant(){
        return isViewInstant;
    }

    public boolean isViewUse() {
        return isViewUse;
    }

    public List<String> relationSymbols(){
        return relSymbols;
    }

    public List<String> nodeSymbols(){
        return nodeSymbols;
    }

    public LinkedList<String> usedViews(){
        return usedViews;
    }

    public String getChangeGraphQuery(){
        return changeGraphQuery;
    }

    public boolean isCg(){
        return cg;
    }

    public String getReturnValExpr() {
        return returnValExpr;
    }

    public retType getReturnType() {
        return returnType;
    }

    public Set<String> getFinalAffectedViews(){
        return finalAffectedViews;
    }

    public void resetAfterGraphUpdate(){
        //todo there is a bug that one graph update will cause other graph updates to update more views than necessary
        finalAffectedViews = new HashSet<>();
        change = changeType.DEFAULT;
        deletedVar = "";
        affectedAttribute = "";
        affectedVar = "";
        affectedEntry = new TableEntry("");
    }

    public void printDependencies(){

        Set<String> keys = dependencyTable.keySet();

        for( String key : keys){

            System.out.println("---------" + key + ":" + "-----");

            TableEntry te = dependencyTable.get(key);
            int i = 1;
            for(EntryData ed : te.getEntries()){
                System.out.print(i + ":\n");
                System.out.print("Dependents: " + ed.dependents + "\nConditions: ");
                for(Condition c : ed.getConditions()){
                    System.out.println(c.getConditionString());
                }
                System.out.println("\n-------------------\n\n");
                i++;
            }

        }
    }

    public void printOrClauseViews(){
        for (String s : orClauseViews){
            System.out.println(s);
        }
    }
}
