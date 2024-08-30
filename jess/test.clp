(import jess.NodeEnum)

(deftemplate Node (slot id (type INTEGER)) (slot label) (slot name) (slot metainfo))
(deftemplate Edge (slot nodeid1 (type INTEGER)) (slot nodeid2 (type INTEGER)) (slot label) )
(deftemplate Path (slot startnode (type INTEGER)) (slot endnode (type INTEGER)) (multislot edges) )

(deftemplate jess.JessNode (declare (from-class jess.JessNode)))

(deftemplate jess.JessEdge (declare (from-class jess.JessEdge)))

(deftemplate jess.JessPath (slot nodeid))

(defrule testrule
(logical
    ?x <- (Node{id > 5})
    ?y <- (Node{id < x.id})
)
=>
 (bind ?p (new jess.JessNode ))
 (?p setUserId 2)
 (printout t "made a new jess.JessNode" crlf)
 (add ?p)
)

(defrule uponrem
(retract ?x)
=>
(printout t "something retracted"  crlf)
)


(bind ?s (new jess.JessEdge))
(definstance jess.JessEdge ?s dynamic)

(defrule modifyobj
(logical
    ?x <- (jess.JessNode {userId < 4})
)
=>
(bind ?z ( ?x getSlotValue OBJECT )  )
(printout t "weeee" crlf)
(?s setNode1 ?z)
)

(assert (Node (id 10)))
(assert (Node (id 9)))
(facts)
(run)




(facts)



(printout t ((?s getNode1) getUserId) crlf)

(watch all)



(bind ?id (fact-id 2))
(modify ?id (id 4) )

;(facts)


;(printout t ((?s getNode1) getUserId) crlf)


(deffunction find-id (?aFact)
"Given a fact format (eg, (Path (startnode 1) (endnode 5))), find the fact id of the given fact, if it exists"
   (eval (str-cat "(defquery search ?f<-" ?aFact ")"))
   (bind ?result (run-query* search))
   (if (?result next) then
      (return (call (?result get f) getFactId))
   else
      (return nil)))

;(printout t (find-id "(jess.JessEdge (label nil))") crlf )

;(fact-id 10)

;(remove ?s)

(defrule view21
;todo right here, create a jess.JessView associated with view1 and on the RHS of the pattern match, add the path to the view's list
"All Users with more than 100 reputation - an example of a node subscription. Return type is not specified but the view is
on the node itself which is typically what we see in Neo4j"
(logical
    ?x <- (jess.JessNode {reputation > 100})
    (test (eq ( ((?x getSlotValue OBJECT) getLabel) toString) User))
)
=>
 (bind ?p (new jess.JessEdge ))
 (?p setId 200)
      (printout t "made a new jess.JessPath" crlf)

 (definstance jess.JessEdge ?p dynamic)
)


 (bind ?p (new jess.JessNode ))
 (?p setReputation 200)
 (add ?p)

 (facts)