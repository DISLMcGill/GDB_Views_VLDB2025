# This script is used to make the use experiemtn based on the random 
# order already generated in the create experiment

##########################################################################
# Local Basic Warm Experiment
##########################################################################

create_dir_path = 'test/view_creation'
baseline_scripts_dir_path = 'test/view_use/baseline/experiment_scripts'
nonmaterialized_scripts_dir_path = 'test/view_use/non_materialized/experiment_scripts'

# TODO: The following lists/dicts needs to be maintained manually for now
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

local_complex_queries = {
    'U1': 'WITH VIEWS V3_1 V4_2 LOCAL MATCH (n) WHERE n IN V3_1 AND n IN V4_2 RETURN n',
    'U2': 'WITH VIEWS V5 V8_1 LOCAL MATCH (n) WHERE n IN V5 AND n IN V8_1 RETURN n',
    'U3': 'WITH VIEWS V11_1 V12_2 LOCAL MATCH (n) WHERE n IN V11_1 AND n IN V12_2 RETURN n',
    'U4': 'WITH VIEWS V14_2 V1_2 LOCAL MATCH (n) WHERE n IN V14_2 AND n IN V1_2 RETURN n',
    'U5': 'WITH VIEWS V2_2 V11_1 LOCAL MATCH (n) WHERE n IN V2_2 AND n IN V11_1 RETURN n'
}

global_queries = { 
    'U6': 'WITH VIEWS V7_1 GLOBAL MATCH (n:User)-[:POSTED]-(m:Post) WHERE n IN V7_1 RETURN m',
    'U7': 'WITH VIEWS V7_2 GLOBAL MATCH (n:User)-[:POSTED]-(m:Post) WHERE n IN V7_2 RETURN m',
    'U8': 'WITH VIEWS V5 GLOBAL MATCH (p1:Post)-[:PARENT_OF]->(p2:Post) WHERE p1 IN V5 RETURN p2',
    'U9': 'WITH VIEWS V5 V6_2 GLOBAL MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post) WHERE p1 IN V5 AND n IN V6_2 RETURN p2',
    'U10': 'WITH VIEWS V5 V7_1 GLOBAL MATCH (n:User)-[:POSTED]-(p1:Post)-[:PARENT_OF]-(p2:Post) WHERE p1 IN V5 AND n IN V7_1 RETURN p2'
}

f = open(f"{nonmaterialized_scripts_dir_path}/local_basic_warm_experiment.txt", "w")

for c in commands:
    f.write(c + "\n")

# Writing the USE commands in the file

with open(f"{create_dir_path}/create_experiment.txt",'r') as data_file:

    for line in data_file:
        view_name = line.split()[3]
        f.write(f"WITH NON_MATERIALIZED VIEWS LOCAL BASIC {view_name} LOCAL MATCH (n) RETURN n" + "\n")

f.close()

##########################################################################
# Local Basic Cold Experiment
##########################################################################
with open(f"{nonmaterialized_scripts_dir_path}/local_basic_warm_experiment.txt", 'r') as f_in:
    lines = f_in.readlines()

    with open(f"{nonmaterialized_scripts_dir_path}/local_basic_cold_experiment.txt", 'w') as f_out:
        for line in lines[len(commands):len(commands) + 2*len(commands)]:
            f_out.write(line)

f_out.close()


##########################################################################
# Local Complex Warm Experiment
##########################################################################

# The order is the same as in the baseline experiment
f = open(f"{nonmaterialized_scripts_dir_path}/local_comlpex_warm_experiment.txt", "w")

for c in commands:
    f.write(c + "\n")

# Writing the USE commands in the file

with open(f"{baseline_scripts_dir_path}/baseline_local_complex.txt",'r') as data_file:

    for line in data_file:
        use_query_name = line.split()[1]
        use_query = local_complex_queries[use_query_name]
        first_view = use_query.split()[2]
        second_view = use_query.split()[3]
        f.write(f"WITH NON_MATERIALIZED VIEWS LOCAL COMPLEX {first_view} {second_view} LOCAL MATCH (n) WHERE n IN {first_view} AND n IN {second_view} RETURN n" + "\n")

f.close()

##########################################################################
# Local Complex Cold Experiment
##########################################################################
with open(f"{nonmaterialized_scripts_dir_path}/local_comlpex_warm_experiment.txt", 'r') as f_in:
    lines = f_in.readlines()

    with open(f"{nonmaterialized_scripts_dir_path}/local_comlpex_cold_experiment.txt", 'w') as f_out:
        for line in lines[len(commands):len(commands) + 2*len(local_complex_queries)]:
            f_out.write(line)

f_out.close()


##########################################################################
# Global Warm Experiment
##########################################################################

f = open(f"{nonmaterialized_scripts_dir_path}/global_warm_experiment.txt", "w")

for c in commands:
    f.write(c + "\n")

with open(f"{baseline_scripts_dir_path}/baseline_global.txt",'r') as data_file:

    # WITH VIEWS V7_1 GLOBAL MATCH (n:User)-[:POSTED]-(m:Post) WHERE n IN V7_1 RETURN m
    for line in data_file:
        use_query_name = line.split()[1]
        use_query = global_queries[use_query_name]
        views = use_query[use_query.find('VIEWS') + len('VIEWS'):use_query.find('GLOBAL')].strip()
        query_susbtring = use_query[use_query.find('MATCH'):].strip()
        f.write(f"WITH NON_MATERIALIZED VIEWS GLOBAL {views} {query_susbtring} \n")

f.close()

