# Go over the random over of the create queries in evaluation_experiment.txt and write their baseline commands into a new life

with open("test/view_creation/create_experiment.txt",'r') as data_file:
    f = open("test/view_use/baseline/experiment_scripts/baseline_local_basic.txt", "w")

    for line in data_file:
        view_name = line.split()[3]
        f.write(f"baseline {view_name} MATCH" + line.split("MATCH")[1])

    f.close()