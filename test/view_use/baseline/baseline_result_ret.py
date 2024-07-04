from matplotlib import pyplot as plt

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']

# This should be consistent with the number of views used in the experiment
num_view = 24
baseline_time_table = [0] * num_view
time =  0
view_index =  -1

with open("test/baseline_output.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('baseline'):
            view_name = line.split()[1]
            view_index = views_arr.index(view_name)
        elif 'to execute baseline query' in line:
            time = int(line.split()[1])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            baseline_time_table[view_index] += time
            # Clear all the parameters for the next round
            time =  0
            view_index =  -1

average_baseline_time = [x / 5 for x in baseline_time_table]

print("Average baseline time")
print(average_baseline_time)

# Make plots of the results and save

plt.plot(views_arr, average_baseline_time, marker='o', color='b')
plt.xlabel("View Name")
plt.ylabel("Baseline Execution Time(ms)")
plt.xticks(rotation=315)
plt.savefig("test/evaluation_plots/baseline_time.pdf", format="pdf", bbox_inches="tight")



#################### Second Part: Comparing Baseline time with creation time ####################
time_to_create_view = 0
view_create_time_table = [0] * num_view

with open("test/experiment_output.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('CREATE VIEW AS'):
            view_name = line.split()[3]
            view_index = views_arr.index(view_name)
        elif 'to create views' in line: 
            time_to_create_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            view_create_time_table[view_index] += time_to_create_view
            time_to_create_view =  0
            view_index =  -1

average_creation_time_list = [x / 5 for x in view_create_time_table]

print("View Creation Time Table")
print(average_creation_time_list)

# Make comparison plot
plt.close()

plt.plot(views_arr, average_baseline_time, linestyle="-", color='b', label = "Average Baseline Query Execution (ms)")
plt.plot(views_arr, average_creation_time_list, linestyle="-.", color='g', label = "Average View Creation Time (ms)")
plt.xlabel("View Name")
plt.ylabel("Proessing Time (ms)")
plt.xticks(rotation=315)
plt.legend()
plt.savefig("test/evaluation_plots/baseline_vs_creation_time.pdf", format="pdf", bbox_inches="tight")