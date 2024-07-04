(import jess.NodeEnum)

(deftemplate jess.JessNode (declare (from-class jess.JessNode)) )
(deftemplate jess.JessEdge (declare (from-class jess.JessEdge)) )
(deftemplate jess.JessPath (declare (from-class jess.JessPath)) )
(deftemplate jess.JessView (declare (from-class jess.JessView)) )


(deffunction find-id (?aFact)
"Given a fact format (eg, (Path (startnode 1) (endnode 5))), find the fact id of the given fact, if it exists
 Only works if the field corresponding to the shadow fact's slot is declared public in the Java class."
   (eval (str-cat "(defquery search ?f<-" ?aFact ")"))
   (bind ?result (run-query* search))
   (if (?result next) then
      (return (call (?result get f) getFactId))
   else
      (return nil)))

(deffunction remove-edge (?edgeid)
""
(bind ?params (str-cat "(jess.JessEdge (id " ?edgeid "))"))
(bind ?id (find-id ?params))
(if (integerp ?id) then
    (retract ?id)
 else
    (printout t "fail to remove or not found" crlf)))







;(defrule view100Rep
;"All Users with more than 100 reputation - an example of a node subscription. Return type is not specified but the view is
;on the node itself which is typically what we see in Neo4j"
;(logical
;    ?x <- (jess.JessNode)
;    (test (eq ( ((?x getSlotValue OBJECT) getLabel) toString) "User"))
;    (test ( > ( ( ?x getSlotValue OBJECT) getReputation) 100 ) )
;)
;=>
;
;    (bind ?p (new jess.JessPath ))
;    (?p addNode (?x getSlotValue OBJECT))
;    (add ?p)
;    (bind ?list (fetch view100Rep))
;    (?list add ?p)
;)

;(defrule test
;?x <- (jess.JessNode (id ?i) (label ?a&:(eq (?a toString) "User")) (views ?b&:(> ?b 500000)))
;?z <- (jess.JessEdge (start ?s&:(eq ?s ?i)))
;=>
;(printout t "haha" crlf)
;)

;(defrule two-hops-posts
;"All posts that are children of posts by users with >500,000 reputation on their profile"
;(logical
;    ?x <- (jess.JessNode (label ?a&:(eq (?a toString) "User")) (reputation ?b&:(> ?b 500000)) )
;    ?y <- (jess.JessNode)
;    (test (eq ( ((?y getSlotValue OBJECT) getLabel) toString) "Post"))
;    ?z <- (jess.JessEdge {start == x.id && end == y.id })
;    (test (eq ( ((?z getSlotValue OBJECT) getLabel) toString) "POSTED"))
;
;    ?w <- (jess.JessNode)
;    (test (eq ( ((?w getSlotValue OBJECT) getLabel) toString) "Post"))
;    ?u <- (jess.JessEdge {start == y.id && end == w.id })
;    (test (eq ( ((?u getSlotValue OBJECT) getLabel) toString) "PARENT_OF"))
;)
;=>
;    (bind ?path (new jess.JessPath ))
;    (?path addNode ?x)
;    (?path addNode ?y)
;    (?path addNode ?w)

;    (?path addEdge ?z)
;    (?path addEdge ?u)

;    (bind ?list (fetch two-hops-posts))
;    (add ?path)
;    (?list add ?path)
;)

(watch all)