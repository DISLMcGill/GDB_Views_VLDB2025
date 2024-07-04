##########################################################################
# Thesis Warm Experiment
##########################################################################

baseline_scripts_dir_path = 'test/view_use/baseline/experiment_scripts'
materialized_scripts_dir_path = 'test/view_use/materialized/experiment_scripts'
nonmaterialized_scripts_dir_path = 'test/view_use/non_materialized/experiment_scripts'

# TODO: The following lists/dicts needs to be maintained manually for now
create_commands = [
    "CREATE VIEW AS V1 MATCH (n:Post) WHERE n.score > 350 RETURN n",
    "CREATE VIEW AS V2 MATCH (n:Post) WHERE n.score < 800 AND n.score > 350 RETURN n",
    "CREATE VIEW AS V3 MATCH (n:User) WHERE n.upvotes>1000 RETURN n",
    "CREATE VIEW AS V4 MATCH (n:User) WHERE n.reputation > 90000 RETURN n",
    "CREATE VIEW AS V5 MATCH (n:Tag) WHERE n.tagId = 'java' OR n.tagId = 'html' RETURN n",
    "CREATE VIEW AS V6 MATCH p = (n:User)-[:POSTED]-(po:Post) WHERE n.reputation < 500 RETURN p",
    "CREATE VIEW AS V7 MATCH (n:Post)-[:PARENT_OF]-(m:Post) WHERE m.score > 100 AND m.score < 600 RETURN n",
    "CREATE VIEW AS V8 MATCH (n:User)-[:POSTED]-(po:Post)-[:PARENT_OF]-(po2:Post) WHERE n.upvotes > 800 AND po.comments > 10  RETURN po2",
    "CREATE VIEW AS V9 MATCH (n:User)-[:POSTED]-(p:Post) WHERE n.userId = 19 RETURN p",
    "CREATE VIEW AS V10 MATCH (betterPost:Post)-[:PARENT_OF]-(worstPost:Post) WHERE worstPost.score < 10 AND betterPost.score > worstPost.score * 10 RETURN betterPost",
    "CREATE VIEW AS V11 MATCH (n:User)-[:POSTED]-(p:Post) WHERE n.upvotes > 1000 OR p.score > 350 RETURN p",
    "CREATE VIEW AS V12 MATCH (p1:Post)-[:HAS_TAG]-(t:Tag) WITH p1, COUNT(t) as numberOfTags WHERE numberOfTags > 20 RETURN p1",
    "CREATE VIEW AS V14 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<50 RETURN m",
    "CREATE VIEW AS V15 MATCH (p2:Post)-[:HAS_TAG]-(t) WHERE t.tagId='html' RETURN p2",
    "CREATE VIEW AS V16 MATCH p = (n:User)-[:POSTED]-(po:Post) WHERE n.reputation < 50000 RETURN p",
    "CREATE VIEW AS V17 MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post)-[:POSTED]-(m:User) WHERE n.userId<m.userId AND n.reputation>m.reputation RETURN m",
    "CREATE VIEW AS V18 MATCH (n:User)-[:POSTED]-(po:Post)-[:PARENT_OF]-(po2:Post) WHERE n.upvotes > 0 AND po.comments > 10 AND po2.comments<10  RETURN po2",
    "CREATE VIEW AS V19 MATCH (n:Post)-[:PARENT_OF]-(m:Post) WHERE n.score =15 AND m.score = 50 RETURN n",
    "CREATE VIEW AS V20 MATCH (n:Post)-[:HAS_TAG]-(t) WHERE n.postId = '1065111' RETURN t",
]

f = open(f"{nonmaterialized_scripts_dir_path}/thesis/warm_experiment.txt", "w")

for c in create_commands:
    f.write(c + "\n")

# Writing the USE commands in the file

with open(f"{materialized_scripts_dir_path}/thesis/warm_experiment.txt",'r') as data_file:

    for line in data_file:
        if 'GLOBAL' in line:
            view_name = line.split()[2]
            f.write(f"WITH NON_MATERIALIZED VIEWS GLOBAL {view_name}{line.split('GLOBAL', 1)[1]}")
        elif line.split()[3] == 'LOCAL':
            view_name = line.split()[2]
            f.write(f"WITH NON_MATERIALIZED VIEWS LOCAL BASIC {view_name} LOCAL MATCH (n) RETURN n" + "\n")
        elif line.split()[4] == 'LOCAL':
            f.write(f"WITH NON_MATERIALIZED VIEWS LOCAL COMPLEX {line.split()[2]} {line.split()[3]} LOCAL MATCH (n) WHERE n IN {line.split()[2]} AND n IN {line.split()[3]} RETURN n" + "\n")

f.close()