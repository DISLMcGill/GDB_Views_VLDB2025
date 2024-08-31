## Excluded queries
- V14_1: matches 0 nodes
- V14_2: makes system crash
- V5: return set of 1.05 million nodes, too large to handle
- V10: 2.3 million nodes and 1.4 million edges, too large to handle
- V8_2, V11_1, V12_1, V12_2: string too long UNWIND nodes(p) baseline query
- 


### For reference 
CREATE VIEW AS V5 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN betterPost
WITH VIEWS V5 LOCAL MATCH (n) RETURN n
baseline V5 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN DISTINCT betterPost

CREATE VIEW AS V10 MATCH p=(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN p
WITH VIEWS V10 LOCAL MATCH (n) RETURN n
baseline V10 MATCH p=(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 UNWIND NODES(p) AS nodes RETURN DISTINCT nodes

CREATE VIEW AS V8_2 MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE po.score > 38 RETURN p
WITH VIEWS V8_2 LOCAL MATCH (n) RETURN n
baseline V8_2 MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE po.score > 38 UNWIND NODES(p) AS nodes RETURN DISTINCT nodes

CREATE VIEW AS V11_1 MATCH p=(n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN p
WITH VIEWS V11_1 LOCAL MATCH (n) RETURN n
baseline V11_1 MATCH p=(n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 UNWIND NODES(p) AS nodes RETURN DISTINCT nodes


CREATE VIEW AS V12_1 MATCH p=(n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation RETURN p
baseline V12_1 MATCH p=(n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation UNWIND NODES(p) AS nodes RETURN DISTINCT nodes
WITH VIEWS V12_1 LOCAL MATCH (n) RETURN n

CREATE VIEW AS V12_2 MATCH p=(n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score RETURN p
baseline V12_2 MATCH p=(n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score UNWIND NODES(p) AS nodes RETURN DISTINCT nodes
WITH VIEWS V12_2 LOCAL MATCH (n) RETURN n


