# This experiment runs on
    # Medium database with 173,575 users
    # MAX nodeid in these users is 293,352
    # The experiment is only conducted in cold environment
    # 5,653,345 total nodes on Users

f = open("test/view_use/baseline/experiment_scripts/other/cold_nodeid_experiment.txt", "w")

# batches of 250k nodes
for i in range(20): 
    id_range = range((i+1)*250000)
    f.write(f"range test {(i+1)*250000}\n")

f.close()