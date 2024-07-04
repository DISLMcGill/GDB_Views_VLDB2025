package main;

import org.apache.commons.lang.StringUtils;

import java.util.*;


/*
    Data structure description:

        an instance of TableEntry is what the DependencyTable stores as a value given a key. This entry corresponds to label information (e.g POST, USER, etc)
        This also contains a list of pairs, in which each pair contains (1) a list of conditions on the label, and (2) a list of keys in the dependency table for which the pair
           would affect. This is basically the implementation of the dependency table described in Chapter 5 of the thesis.


    Most (?) (all?) of the maintenance work is done within this class. Ignore commented lines as they were probably there for troubleshooting.

 */

public class TableEntry {

    private String myKey;
    private List<EntryData> entries;

    public TableEntry(String k){
        myKey=k;
        entries = new LinkedList<>();
    }


    public boolean isEmpty(){
        return entries.size() == 0;
    }

    public void addEntry(EntryData e){
        entries.add(e);
    }

    public List<EntryData> getEntries() {
        return entries;
    }


    public boolean addSameDependents(Set<Condition> edConds, List<String> deps){

        for(EntryData myED : entries){


//            System.out.println("Mine: ");
//            for(main.Condition c : myED.getConditions()) c.printCondition();
//            System.out.println("Compared with : ");


            Set<Condition> myConds = new HashSet<>(myED.getConditions());
//            for(main.Condition d : myConds) d.printCondition();

            if(myConds.equals(edConds)){

                int i = entries.indexOf(myED);
                entries.get(i).addDependent(deps);
                return true;
            }

        }

        return false;

    }

    public Set<EntryData> getSameDependents(Set<Condition> edConds){

        Set<EntryData> returnSet = new HashSet<>();

        for(EntryData myED : entries){

            Set<Condition> myConds = new HashSet<>(myED.getConditions());

            if(myConds.equals(edConds)){
                returnSet.add(myED);
            }

        }

        return returnSet;
    }

    //Used for INSERTIONS
    public Set<EntryData> filterWithInsertion(Set<Condition> attributeKeyValuePairs, Set<String> viewsWithOr, Set<String> extraOrViews){
        //Given conditions as input, which main.EntryData will be NOT affected by an insertion of a node with these
        //properties?
        /**
         * THREE CASES
         *  FIRST CASE: Insertion includes an attribute for which this ED contains a condition on
         *              >Use same logic for DELETION and UPDATE: only if it "overlaps" do we continue with update.
         *  SECOND CASE: main.EntryData contains a condition on an attribute which is not defined upon insertion
         *              >Immediately filter (i.e we do not re-evaluate, we do not return this ED)
         *  THIRD CASE: main.EntryData contains no conditions
         *              >Immediately continue (i.e we MUST re-evaluate, we MUST keep this in the returnSet)
         *
         *  FOURTH CASE: A dependent found in this main.TableEntry has an OR clause. So if ANYTHING matches then IMMEDIATELY CONTINUE
         *
         *  SPECIAL CASE: If the inserted node has no attributes at all (say it is just a label) then
         *                  all "no condition" main.EntryData must be marked - this is handled by case 3
         *
         */
        Set<EntryData> returnSet = new HashSet<>(entries);

        // EDGE CASE: insertion has no conditions, in which case input is null
        if(attributeKeyValuePairs == null){
            //check for each entry if they also have no conditions
            for (EntryData myED : entries){
                Set<Condition> myConds = new HashSet<>(myED.getConditions());
                if(!myConds.isEmpty()){
                    //todo there is a bug where condition in BetterParentPost2 is apparently empty, so it is not being removed in this case..
//                    System.out.println(myKey + ", " + myED.getConditions().toString());
                    returnSet.remove(myED);
                }
            }

            //return
            return returnSet;
        }



        //for ease, build set of attribute names on the insertion and set of attribute names on view condition
        Set<String> attributeNamesInserted = new HashSet<>();
        for(Condition temp : attributeKeyValuePairs){
            attributeNamesInserted.add(temp.attribute);
        }




        for (EntryData myED : entries) {

            Set<Condition> myConds = new HashSet<>(myED.getConditions());

            //Case 3
            if (myConds.isEmpty()) continue;



            Set<String> attributeNamesView = new HashSet<>();

            for(Condition viewCondition : myConds){
                attributeNamesView.add(viewCondition.attribute);
            }

            //Case 2
            if(!attributeNamesInserted.containsAll(attributeNamesView)) {
//                System.out.println("continued and skipped...");
                returnSet.remove(myED);
                continue; //All attributes in the view are included in the insert
            }

            //else we should check for Case 1
            boolean breakLoop = false;
            for(Condition myCond : myConds){
                if(breakLoop) break;
                for(Condition insert : attributeKeyValuePairs){

                    if(myCond.attribute.equals(insert.attribute)){
                        //insert.conditionString always contains "="
                        String valTheir = insert.conditionString.split("=")[1];

                        if(myCond.conditionString.contains(">")){
                            String valMine = myCond.conditionString.split(">")[1];
                            //view: a.attribute > 10
                            //view  {a:12}: then this is affected so we don't change anything.


                            if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                if((Integer.parseInt(valMine) >= Integer.parseInt(valTheir))){
                                    //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                    returnSet.remove(myED);
                                    breakLoop = true;
                                    break;
                                }
                            }

                        }
                        if(myCond.conditionString.contains("<")){
                            //view: a.attribute < 10
                            //view  {a:3}: continue.
                            String valMine = myCond.conditionString.split("<")[1];

//                            System.out.println(valMine + ", " + valTheir);

                            if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                if((Integer.parseInt(valMine) <= Integer.parseInt(valTheir))){
                                    //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                    returnSet.remove(myED);
                                    breakLoop = true;
                                    break;
                                }
                            }


                        }
                        if(myCond.conditionString.contains("=")){
                            String valMine = myCond.conditionString.split("=")[1];
                            if(!valMine.equals(valTheir)){
                                returnSet.remove(myED);
                                breakLoop = true;
                                break;
                            }

                        }

                    }


                }
            }



        }


        //FOURTH CASE
        for(String orView : viewsWithOr){
            for(EntryData myED : entries){

                Set<Condition> myConds = new HashSet<>(myED.getConditions());

                Set<String> attributeNamesView = new HashSet<>();

                for(Condition viewCondition : myConds){
                    attributeNamesView.add(viewCondition.attribute);
                }

                if(!Collections.disjoint(attributeNamesInserted,(attributeNamesView))) { //disjoint returns false if they have any elements in common
//                System.out.println("continued and skipped...");
                    if(!extraOrViews.contains(orView)) {
                        extraOrViews.add(orView);
                    }

                }
            }
        }



//        System.out.println("returning " + returnSet);

        return returnSet;





    }


    //Used so FAR for DELETIONS and UPDATES
    public Set<EntryData> filterIrrelevantEntryData(Set<Condition> conditions, Set<String> viewsWithOr, Set<String> extraOrViews){

        //Given the conditions as input, which main.EntryData will ABSOLUTELY not be affected by
        //purely these conditions? ASSUMING all condition sets are joined by AND clauses.
        //>> As long as there is a MISMATCH of a condition, it can be filtered out. Otherwise, it should be
        //marked.


        //First thing to check: mismatches of conditions (done)
        //Second thing to check: does the change contain any on attributes not present in an main.EntryData?
        //      if so we FORCE these to be invalidated (keep them!) aka distinct sets. (DONE since
        //              DONE since DEFAULT is ENTIRE SET and in case of disjoint sets LOOP WILL DO NOTHING
        //Third thing to check: what if we don't have any conditions in our main.EntryData?
        //      if so then we must be safe an re-evaluate (DONE since DEFAULT IS THE ENTIRE SET)
        //Final thing to check: if the view contains an OR clause then ANY match -> re-evaluate (keep in set)

        Set<EntryData> returnSet = new HashSet<>(entries);


        //if conditions is empty (i.e, the graph change has no conditions) then we return the full set
        if(conditions == null || conditions.isEmpty()) return returnSet;

        for(EntryData myED : entries){

            Set<Condition> myConds = new HashSet<>(myED.getConditions());

            if(myConds.isEmpty()) continue;

            boolean breakLoop = false;

            for(Condition cMine : myConds){

                if(breakLoop) break;

                for(Condition cTheir : conditions){

                    if(cMine.attribute.equals(cTheir.attribute)){
                        if(cMine.conditionString.contains(">")){
                            if(cTheir.conditionString.contains(">")){
                                //view: a>3. change: a>4.
                                //should be affected, don't filter it out
                            }
                            if(cTheir.conditionString.contains("<")){

                                System.out.println(cTheir.conditionString + ", " + cMine.conditionString);

                                //view: a>3. change: a<2. filter.
                                //view: a>3. change: a<4. don't filter.

                                String valMine = cMine.conditionString.split(">")[1];
                                String valTheir = cTheir.conditionString.split("<")[1];

                                if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                   System.out.println(Integer.parseInt(valMine) >= Integer.parseInt(valTheir));

                                    if((Integer.parseInt(valMine) >= Integer.parseInt(valTheir))){

                                        //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                        returnSet.remove(myED);
                                        System.out.println("Removing the ED would result in removing the following: " + myED.dependents);
                                        breakLoop = true;
                                        break;
                                    }
                                }
                            }
                            if(cTheir.conditionString.contains("=")){
                                //same as above

                                String valMine = cMine.conditionString.split(">")[1];
                                String valTheir = cTheir.conditionString.split("=")[1];

                                if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                    if((Integer.parseInt(valMine) >= Integer.parseInt(valTheir))){
                                        //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                        returnSet.remove(myED);
                                        breakLoop = true;
                                        break;
                                    }
                                }
                            }
                        }


                        if(cMine.conditionString.contains("<")){
                            if(cTheir.conditionString.contains(">")){

                                //view: a<3. change: a>4. filter.
                                //view: a<3. change: a>2. don't filter.

                                String valMine = cMine.conditionString.split("<")[1];
                                String valTheir = cTheir.conditionString.split(">")[1];

                                if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                    if((Integer.parseInt(valMine) <= Integer.parseInt(valTheir))){
                                        //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                        returnSet.remove(myED);
                                        breakLoop = true;
                                        break;
                                    }
                                }
                            }
                            if(cTheir.conditionString.contains("=")){

                                //view: a<3. change: a>4. filter.
                                //view: a<3. change: a>2. don't filter.

                                String valMine = cMine.conditionString.split("<")[1];
                                String valTheir = cTheir.conditionString.split("=")[1];

                                if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                    if((Integer.parseInt(valMine) <= Integer.parseInt(valTheir))){
                                        //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                        returnSet.remove(myED);
                                        breakLoop = true;
                                        break;
                                    }
                                }
                            }
                            if(cTheir.conditionString.contains("<")){
                                //view: a<3. change: a<4.
                                //should be affected, don't filter it out
                            }
                        }


                        if(cMine.conditionString.contains("=")){
                            if(cTheir.conditionString.contains(">")){
                                //view: a=3. change: a>4. filter.
                                //view: a=3. change: a>2. don't filter.
                                String valMine = cMine.conditionString.split("=")[1];
                                String valTheir = cTheir.conditionString.split(">")[1];

                                if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                    if((Integer.parseInt(valMine) <= Integer.parseInt(valTheir))){
                                        //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                        returnSet.remove(myED);
                                        breakLoop = true;
                                        break;
                                    }
                                }
                            }
                            if(cTheir.conditionString.contains("<")){
                                //view: a=3. change: a<2. filter.
                                //view: a=3. change: a<4. don't filter.
                                String valMine = cMine.conditionString.split("=")[1];
                                String valTheir = cTheir.conditionString.split("<")[1];

                                if(StringUtils.isNumeric(valMine) && StringUtils.isNumeric(valTheir)){
                                    if((Integer.parseInt(valMine) >= Integer.parseInt(valTheir))){
                                        //Then WE FILTER THIS. Since this main.EntryData SHOULD NOT BE AFFECTED
                                        returnSet.remove(myED);
                                        breakLoop = true;
                                        break;
                                    }
                                }


                            }
                            if(cTheir.conditionString.contains("=")){

                                //if they're the same value then we don't filter. If they're not the same value
                                //then this view should not be affected, so filter.
                                String valMine = cMine.conditionString.split("=")[1];
                                String valTheir = cTheir.conditionString.split("=")[1];

                                if(!valMine.equals(valTheir)){
                                    returnSet.remove(myED);
                                    breakLoop = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        //for ease, build set of attribute names on the deletion and set of attribute names on view condition
        Set<String> attributeNamesInserted = new HashSet<>();
        for(Condition temp : conditions){
            attributeNamesInserted.add(temp.attribute);
        }

        for(String orView : viewsWithOr){
            for(EntryData myED : entries){

                Set<Condition> myConds = new HashSet<>(myED.getConditions());

                Set<String> attributeNamesView = new HashSet<>();

                for(Condition viewCondition : myConds){
                    attributeNamesView.add(viewCondition.attribute);
                }

                //Case 2
                if(!Collections.disjoint(attributeNamesInserted,(attributeNamesView))) { //disjoint returns false if they have any elements in common
                    if(myED.dependents.contains(orView)){
                        extraOrViews.add(orView);
                    }

                }
            }
        }

        return returnSet;


    }

}
