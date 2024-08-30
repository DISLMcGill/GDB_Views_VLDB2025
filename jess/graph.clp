(import java.util.HashSet)
(import java.util.Set)
(import java.util.HashMap)
(import java.util.ArrayList)

(bind ?ViewMap (new java.util.HashMap))

(deftemplate Node (slot id (type INTEGER)) (slot label) (slot name) (slot metainfo))
(deftemplate Edge (slot nodeid1 (type INTEGER)) (slot nodeid2 (type INTEGER)) (slot label) )
(deftemplate Path (slot startnode (type INTEGER)) (slot endnode (type INTEGER)) (multislot edges) )

(deftemplate PathView (multislot edges))


(defrule edge-to-path
(logical ?x <- (Edge))
=> (assert (Path (startnode ?x.nodeid1) (endnode ?x.nodeid2))))


(defrule path-to-paths
(logical ?x <- (Path) ?y <- (Path {endnode == x.startnode})  )
=>
(assert (Path (startnode ?y.startnode) (endnode ?x.endnode)))
)


(deffunction find-id (?aFact)
"Given a fact format (eg, (Path (startnode 1) (endnode 5))), find the fact id of the given fact, if it exists"
   (eval (str-cat "(defquery search ?f<-" ?aFact ")"))
   (bind ?result (run-query* search))
   (if (?result next) then
      (return (call (?result get f) getFactId))
   else
      (return nil)))


(deffunction update-view-table ()

)

(deffunction remove-edge (?nodeid1 ?nodeid2 ?lab)
""
(bind ?params (str-cat "(Edge (nodeid1 " ?nodeid1 ") (nodeid2 " ?nodeid2 ") (label " ?lab "))"))
(bind ?id (find-id ?params))
(if (integerp ?id) then
    (retract ?id)
    (update-view-table)
 else
    (printout t "fail to remove or not found" crlf)))


(assert (Node (id 1) (label User) (name Bob)))
(assert (Node (id 2) (label Post)))
(assert (Node (id 3) (label Tag)))
(assert (Node (id 4) (label User)))
(assert (Node (id 5) (label Post)))
(assert (Node (id 6) (label User)))
(assert (Node (id 7) (label User)))
(assert (Node (id 8) (label User)))
(assert (Node (id 9) (label Post)))
(assert (Node (id 10) (label User)))
(assert (Node (id 11) (label Tag)))



(assert (Edge (nodeid1 1) (nodeid2 2) (label responds_to) ))
(assert (Edge (nodeid1 2) (nodeid2 3) (label contains_tag) ))
(assert (Edge (nodeid1 4) (nodeid2 2) (label responds_to) ))
(assert (Edge (nodeid1 6) (nodeid2 2) (label responds_to) ))
(assert (Edge (nodeid1 7) (nodeid2 10) (label follows) ))
(assert (Edge (nodeid1 7) (nodeid2 3) (label contains_tag) ))
(assert (Edge (nodeid1 4) (nodeid2 5) (label responds_to) ))
(assert (Edge (nodeid1 6) (nodeid2 5) (label responds_to) ))
(assert (Edge (nodeid1 7) (nodeid2 5) (label responds_to) ))
(assert (Edge (nodeid1 4) (nodeid2 10) (label follows) ))
(assert (Edge (nodeid1 10) (nodeid2 4) (label follows) ))
(assert (Edge (nodeid1 7) (nodeid2 8) (label follows) ))
(assert (Edge (nodeid1 8) (nodeid2 10) (label follows) ))
(assert (Edge (nodeid1 7) (nodeid2 9) (label responds_to) ))
(assert (Edge (nodeid1 8) (nodeid2 9) (label responds_to) ))
(assert (Edge (nodeid1 2) (nodeid2 11) (label contains_tag) ))
(assert (Edge (nodeid1 5) (nodeid2 11) (label contains_tag) ))
(assert (Edge (nodeid1 9) (nodeid2 11) (label contains_tag) ))

(run)
;(facts)

;Syntax for edge removal calls..
;(remove-edge 1 23 responds_to)


;creating a view:
;all users that have also responded to posts that Bob (id 1) has responded to
; in cypher: MATCH (n:User)-[:responds_to]->(p:Post)<-[:responds_to]-(m:User) WHERE n.name="Bob" RETURN m



(?ViewMap put "rule-1" (new java.util.ArrayList))
(defrule rule-1

 (logical

    ?node1 <- (Node {name == Bob && label == User})
    ?node2 <- (Node {label == Post})
    ?node3 <- (Node {label == User && id != node1.id})

    ?edge2 <- (Edge {nodeid1 == node3.id && nodeid2 == node2.id && label == responds_to})
    ?edge <- (Edge {nodeid1 == node1.id && nodeid2 == node2.id && label == responds_to})

    )
 =>
    (bind ?list (?ViewMap get "rule-1"))
    (printout t "node1: " (?node1 getFactId) " node2: " (?node2 getFactId) " node3: " (?node3 getFactId) " edge1: " (?edge getFactId)
        " edge2: " (?edge2 getFactId) crlf)
    (?list add (?node3 getFactId))
    (?ViewMap put "rule-1" ?list)
 )

;(facts)
(run)
;(facts)

(deffunction print-table ()
(foreach ?x (?ViewMap keySet)
    (bind ?valset (?ViewMap get ?x))
    (foreach ?i (?valset iterator)
        (printout t ?i crlf)
    )
))

(print-table)

(facts)


(print-table)

(defrule upon-delete
(logical
    ?x <- (retract))
=>
(printout t "a fact was deleted" ?x crlf)
)

(retract 1)
