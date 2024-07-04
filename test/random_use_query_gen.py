import random

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
    'CREATE VIEW AS V5 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN betterPost',
    'CREATE VIEW AS V6_1 MATCH (n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN n',
    'CREATE VIEW AS V6_2 MATCH (n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE n.reputation < 850 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN n',
    'CREATE VIEW AS V7_1 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation RETURN m',
    'CREATE VIEW AS V7_2 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score RETURN m',
    'CREATE VIEW AS V8_1 MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE n.reputation < 850 RETURN p',
    'CREATE VIEW AS V8_2 MATCH p=(n:User)-[:POSTED]-(po:Post) WHERE po.score > 38 RETURN p',
    'CREATE VIEW AS V9_1 MATCH p=(n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 200 AND n.score > 200 RETURN p',
    'CREATE VIEW AS V9_2 MATCH p=(n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 300 AND m.score < 450 RETURN p',
    'CREATE VIEW AS V10 MATCH p=(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN p',
    'CREATE VIEW AS V11_1 MATCH p=(n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN p',
    'CREATE VIEW AS V11_2 MATCH p=(n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]->(worstPost:Post) WHERE n.reputation < 850 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN p',
    'CREATE VIEW AS V12_1 MATCH p=(n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation RETURN p',
    'CREATE VIEW AS V12_2 MATCH p=(n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score RETURN p',
    "CREATE VIEW AS V14_1 MATCH p=(n:User)-[:POSTED]-(po:Post)-[:HAS_TAG]-(t:Tag)-[:HAS_TAG]-(po2:Post)-[:POSTED]-(n) WHERE t.tagId = 'java' RETURN p",
    "CREATE VIEW AS V14_2 MATCH p=(n:User)-[:POSTED]-(po:Post)-[:HAS_TAG]-(t:Tag)-[:HAS_TAG]-(po2:Post)-[:POSTED]-(n) WHERE t.tagId = 'java' OR t.tagId = 'c#' RETURN p"
]

index_list = list(range(24))

f = open("test/use_experiment.txt", "w")

# First add the CREATE commands to run on the system before using any of them 
for c in commands:
    f.write(c)
    f.write("\n")

# Then adding the use queries in the same order as the create_experiment stuff 
with open("test/create_experiment.txt",'r') as data_file:
    for line in data_file:
        view_name = line.split(" ")[3]
        f.write(f"WITH VIEWS {view_name} LOCAL MATCH (n) RETURN n\n")

f.close()
data_file.close()