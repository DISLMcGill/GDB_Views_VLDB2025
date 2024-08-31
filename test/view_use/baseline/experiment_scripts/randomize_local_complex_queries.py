import random

'''# Global list of available commands
commands = [
    'baseline U1 MATCH p=(po:Post)-[:PARENT_OF]-(m:Post)-[:POSTED]-(n:User) WHERE n.reputation < 850 AND m.score > 300 AND m.score < 450  RETURN m',
    'baseline U2 MATCH p=(n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE n.reputation < 850 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN betterPost',
    'baseline U3 MATCH p=(m:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post)-[:POSTED]-(n:User) WHERE n.userId < m.userId AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN p',
    "baseline U4 MATCH p=(n:User)-[:POSTED]-(po:Post)-[:HAS_TAG]-(t:Tag)-[:HAS_TAG]-(po2:Post)-[:POSTED]-(n) WHERE t.tagId = 'java' OR t.tagId = 'c#' AND n.upvotes > 400 RETURN n",
    'baseline U5 MATCH p=(n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 AND betterPost.score < 1500 AND betterPost.score > 10 RETURN betterPost'
]

index_list = list(range(5))

# Create 5 random lists of commands ordering and write the shuffled results to an output file 
f = open("test/view_use/baseline/experiment_scripts/baseline_local_complex.txt", "w")

for i in range(5):
    random.shuffle(index_list)
    for j in index_list:
        f.write(commands[j])
        f.write("\n")

f.close()'''

# Global list of available commands
new_commands = [
    'baseline U1 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score WITH m MATCH p=(m)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post) WHERE m.reputation < 850 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN DISTINCT m',
    'baseline U2 MATCH p=(n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 200 AND n.score > 200 AND n.score < 1500 RETURN DISTINCT n',
    "baseline U3 MATCH p=(n:User)-[:POSTED]-(po:Post)-[:HAS_TAG]-(t:Tag)-[:HAS_TAG]-(po2:Post)-[:POSTED]-(n) WHERE t.tagId = 'java' WITH CASE WHEN po.score > 38 AND po2.score > 38 THEN [po, po2] WHEN po.score > 38 THEN [po] WHEN po2.score > 38 THEN [po2] END AS result UNWIND result AS node RETURN DISTINCT node",
    "baseline U4 MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE po.score > 38 WITH COLLECT(po) AS firstPosts MATCH (n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 300 AND m.score < 450 WITH firstPosts, COLLECT(n) +  COLLECT(m) AS secondPosts WITH [x IN firstPosts WHERE x IN secondPosts] AS commonNodes UNWIND commonNodes AS commonNode RETURN DISTINCT commonNode",
    'baseline U5 MATCH (n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 AND n.upvotes > 400 RETURN DISTINCT n'
]

index_list = list(range(5))

# Create 5 random lists of commands ordering and write the shuffled results to an output file 
f = open("test/view_use/baseline/experiment_scripts/baseline_local_complex_new.txt", "w")

for i in range(5):
    random.shuffle(index_list)
    for j in index_list:
        f.write(new_commands[j])
        f.write("\n")

f.close()