import random

materialized_scripts_dir_path = 'test/view_use/materialized/experiment_scripts'
commands = [
    "WITH VIEWS V1 V8 LOCAL MATCH (n) WHERE n IN V1 AND n IN V8 RETURN n", 
    "WITH VIEWS V4 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V5 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V5 GLOBAL MATCH(n)-[:HAS_TAG]-(p:Post) WHERE n IN V5 RETURN p",
    "WITH VIEWS V6 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V7 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V8 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V15 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V15 GLOBAL MATCH (n)-[:POSTED]-(m:User) WHERE n IN V15 RETURN m",
    "WITH VIEWS V16 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V17 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V18 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V18 V19 LOCAL MATCH (n) WHERE n IN V18 AND n IN V19 RETURN n",
    "WITH VIEWS V19 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V20 LOCAL MATCH (n) RETURN n",
    "WITH VIEWS V20 GLOBAL MATCH (n)-[:HAS_TAG]-(p:Post) WHERE n IN V20 RETURN p"
]

f = open(f"{materialized_scripts_dir_path}/thesis/warm_experiment.txt", "w")
fcold = open(f"{materialized_scripts_dir_path}/thesis/cold_experiment.txt", "w")

# Creating views
with open(f"test/initFileExample.txt",'r') as data_file:

    for line in data_file:
        args = line.split(";")
        f.write(f"CREATE VIEW AS {args[0]} {args[1]}")
        fcold.write(f"CREATE VIEW AS {args[0]} {args[1]}")


# Using the views
f.write("\n")
fcold.write("\n")

index_list = list(range(len(commands)))

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