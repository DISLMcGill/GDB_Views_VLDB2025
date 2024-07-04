import random

# Global list of available commands

# V7_1: MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId < m.userId AND n.reputation > m.reputation RETURN m
# V7_2: MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score RETURN m
# V5: MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN betterPost
# V6_2: MATCH (n:User)-[:POSTED]-(betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE n.reputation < 850 AND worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN n

# U6 -> WITH VIEWS V7_1 GLOBAL MATCH (n:User)-[:POSTED]-(m:Post) WHERE n IN V7_1 RETURN m
# U7 -> WITH VIEWS V7_2 GLOBAL MATCH (n:User)-[:POSTED]-(m:Post) WHERE n IN V7_2 RETURN m
# U8 -> WITH VIEWS V5 GLOBAL MATCH (p1:Post)-[:PARENT_OF]->(p2:Post) WHERE p1 IN V5 RETURN p2
# U9 -> WITH VIEWS V5 V6_2 GLOBAL MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post) WHERE p1 IN V5 AND n IN V6_2 RETURN p2
# U10 -> WITH VIEWS V5 V7_1 GLOBAL MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post) WHERE p1 IN V5 AND n IN V7_1 RETURN p2

commands = [
    'baseline U6 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId < m.userId AND n.reputation > m.reputation WITH m MATCH (m)-[:POSTED]-(po:Post) RETURN DISTINCT po',
    'baseline U7 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND p1.score < p2.score WITH m MATCH (m)-[:POSTED]-(po:Post) RETURN DISTINCT po',
    'baseline U8 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 WITH betterPost MATCH (betterPost)-[:PARENT_OF]->(p2:Post) RETURN DISTINCT p2',
    "baseline U9 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 WITH betterPost MATCH (n:User)-[:POSTED]-(bp:Post)-[:PARENT_OF]-(wp:Post) WHERE n.reputation < 850 AND wp.score < 10 AND bp.score > wp.score * 10 WITH n, betterPost MATCH (n)-[:POSTED]-(betterPost)-[:PARENT_OF]-(p2:Post) RETURN DISTINCT p2",
    'baseline U10 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 WITH betterPost MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId < m.userId AND n.reputation > m.reputation WITH m, betterPost MATCH (m)-[:POSTED]-(betterPost)-[:PARENT_OF]-(po:Post) RETURN DISTINCT po'
]

index_list = list(range(5))

# Create 5 random lists of commands ordering and write the shuffled results to an output file 
f = open("test/view_use/baseline/experiment_scripts/baseline_global.txt", "w")
fcold = open("test/view_use/baseline/experiment_scripts/baseline_cold_global.txt", "w")

for i in range(5):
    if i < 2:
        random.shuffle(index_list)
        for j in index_list:
            f.write(commands[j])
            f.write("\n")
            fcold.write(commands[j])
            fcold.write("\n")
    else:
        random.shuffle(index_list)
        for j in index_list:
            f.write(commands[j])
            f.write("\n")

f.close()
fcold.close()