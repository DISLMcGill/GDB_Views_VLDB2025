import random

commands = [
    'baseline V4 MATCH (n:User) WHERE n.reputation > 90000 RETURN DISTINCT n', 
    "baseline V5 MATCH (n:Tag) WHERE n.tagId = 'java' OR n.tagId = 'html' RETURN DISTINCT n", 
    'baseline V6 MATCH p = (n:User)-[:POSTED]-(po:Post) WHERE n.reputation < 500 RETURN p', 
    'baseline V7 MATCH (n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 100 AND m.score < 600 RETURN DISTINCT n', 
    'baseline V8 MATCH (n:User)-[:POSTED]-(po:Post)-[:PARENT_OF]-(po2:Post) WHERE n.upvotes > 800 AND po.comments > 10  RETURN DISTINCT po2', 
    "baseline V15 MATCH (p2:Post)-[:HAS_TAG]-(t) WHERE t.tagId='html' RETURN DISTINCT p2", 
    'baseline V16 MATCH p = (n:User)-[:POSTED]-(po:Post) WHERE n.reputation < 50000 RETURN DISTINCT p', 
    'baseline V17 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation RETURN DISTINCT m', 
    'baseline V18 MATCH (n:User)-[:POSTED]-(po:Post)-[:PARENT_OF]-(po2:Post) WHERE n.upvotes > 0 AND po.comments > 10 AND po2.comments<10  RETURN DISTINCT po2', 
    'baseline V19 MATCH (n:Post)-[:PARENT_OF]-(m:Post) WHERE n.score =15 AND m.score = 50 RETURN DISTINCT n', 
    "baseline V20 MATCH (n:Post)-[:HAS_TAG]-(t) WHERE n.postId = '1065111' RETURN DISTINCT t",
    'baseline L1_8 MATCH (n:User)-[:POSTED]-(po:Post)-[:PARENT_OF]-(po2:Post) WHERE n.upvotes > 800 AND po.comments > 10 AND po2.score > 350 RETURN DISTINCT po2',
    'baseline L18_19 MATCH (n:User)-[:POSTED]-(po:Post)-[:PARENT_OF]-(po2:Post) WHERE n.upvotes > 0 AND po.comments > 10 AND po2.comments<10  WITH po2 MATCH (po2)-[:PARENT_OF]-(po3:Post) WHERE po2.score =15 AND po3.score = 50 RETURN DISTINCT po2',
    "baseline G5 MATCH (n:Tag) WHERE n.tagId = 'java' OR n.tagId = 'html' WITH n MATCH(n)-[:HAS_TAG]-(p:Post) RETURN DISTINCT p",
    "baseline G15 MATCH (p:Post)-[:HAS_TAG]-(t) WHERE t.tagId='html' WITH p MATCH (p)-[:POSTED]-(m:User) RETURN DISTINCT m",
    "baseline G20 MATCH (n:Post)-[:HAS_TAG]-(t) WHERE n.postId = '1065111' WITH t MATCH (t)-[:HAS_TAG]-(p:Post) RETURN DISTINCT p"
]

index_list = list(range(len(commands)))

# Create 5 random lists of commands ordering and write the shuffled results to an output file 
f = open("test/view_use/baseline/experiment_scripts/thesis/baseline_warm_experiment.txt", "w")

for i in range(5):
    random.shuffle(index_list)
    for j in index_list:
        f.write(commands[j])
        f.write("\n")

f.close()