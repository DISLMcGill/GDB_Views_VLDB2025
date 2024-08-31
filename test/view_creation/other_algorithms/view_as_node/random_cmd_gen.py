import random

dir_path = 'test/view_creation/other_algorithms/view_as_node'

# Global list of available commands
commands = [
    'CREATE VIEW AS V1_1 MATCH (n: User) WHERE n.upvotes > 1000 RETURN n',
    'CREATE VIEW AS V1_2 MATCH (n: User) WHERE n.upvotes > 400 RETURN n',
    'CREATE VIEW AS V2_1 MATCH (n:Post) WHERE n.score < 1500 AND n.score > 20 RETURN n',
    'CREATE VIEW AS V2_2 MATCH (n:Post) WHERE n.score < 1500 AND n.score > 10 RETURN n',
    'CREATE VIEW AS V3_1 MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE n.reputation < 850 RETURN po',
    'CREATE VIEW AS V3_2 MATCH p = (n:User)-[:POSTED]-(po:Post) WHERE po.score > 38 RETURN po',
    'CREATE VIEW AS V4_1 MATCH p=(n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 200 AND n.score > 200 RETURN n',
    'CREATE VIEW AS V4_2 MATCH p=(n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 300 AND m.score < 450 RETURN m', 
    'CREATE VIEW AS V6_1 MATCH (n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN n',
    'CREATE VIEW AS V6_2 MATCH (n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE n.reputation < 850 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN n',
    'CREATE VIEW AS V7_1 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation RETURN m',
    'CREATE VIEW AS V7_2 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score RETURN m'
]

index_list = list(range(len(commands)))

# Create 5 random lists of commands ordering and write the shuffled results to an output file 
f = open(f"{dir_path}/create_experiment.txt", "w")

f.write("view wipe\n")

for i in range(1):
    random.shuffle(index_list)
    for j in index_list:
        f.write(commands[j])
        f.write("\n")

f.close()