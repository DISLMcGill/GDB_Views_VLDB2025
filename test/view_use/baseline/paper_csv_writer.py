from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
dir_path = 'test/view_use/baseline'
num_view = 13
num_iteration = 5
size = sys.argv[1]

view_baseline_time_table = [ [] for _ in range(num_view) ]

time_to_execute_baseline =  0
view_index =  -1
view_size = 0
isPath = False

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7']
local_complex_queries = ['U1', 'U2', 'U3', 'U4', 'U5']
local_complex_new_queries = ['U1', 'U2', 'U3', 'U4', 'U5']
global_queries = ['U6', 'U7', 'U8', 'U9', 'U10']
new_global_queries = ['U6', 'U7', 'U8', 'U9', 'U10']
thesis_queries = ['V4', 'V5', 'V6', 'V7', 'V8', 'V15', 'V16', 'V17', 'V18', 'V19', 'V20', 'L1_8', 'L18_19', 'G5', 'G15', 'G20']

# Writing Local Basic Data
with open(f"{dir_path}/outputs/local_basic/paper_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('baseline V'):
            view_name = line.split()[1]
            view_index = views_arr.index(view_name)
        elif 'Took' in line:
            time_to_execute_baseline = int(line.split()[1])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            view_baseline_time_table[view_index].append(time_to_execute_baseline)
            # Clear all the parameters for the next round
            isPath = False 
            time_to_execute_baseline =  0
            view_index =  -1
            view_size = 0

time_to_execute_baseline =  0
view_index =  -1

avg_baseline_time = []

# Storing the average creation times
for l in view_baseline_time_table:
    avg_baseline_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_baseline_time)), columns =['View_Name', 'Average_View_Baseline_Time'])
df.to_csv(f'{dir_path}/data/paper/local_basic_{size}.csv', encoding='utf-8', index=False)

########################################## LOCAL COMPLEX ####################################

baseline_local_complex_table = [ [] for _ in range(len(local_complex_queries)) ]

# Writing Local Complex Stuff 
with open(f"{dir_path}/outputs/local_complex/paper_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('baseline U'):
            view_name = line.split()[1]
            view_index = local_complex_queries.index(view_name)
        elif 'Took' in line:
            time_to_execute_baseline = int(line.split()[1])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            baseline_local_complex_table[view_index].append(time_to_execute_baseline)
            # Clear all the parameters for the next round
            isPath = False 
            time_to_execute_baseline =  0
            view_index =  -1
            view_size = 0

time_to_execute_baseline =  0
view_index =  -1

avg_baseline_local_complex_time = []

# Storing the average creation times
for l in baseline_local_complex_table:
    avg_baseline_local_complex_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(local_complex_queries, avg_baseline_local_complex_time)), columns =['Use_Query', 'Average_Baseline_Time'])
df.to_csv(f'{dir_path}/data/paper/local_complex_{size}.csv', encoding='utf-8', index=False)

############################ GLOBAL QUERIES ########################################

global_table = [ [] for _ in range(len(global_queries)) ]

time_to_execute_baseline =  0
view_index =  -1

with open(f"{dir_path}/outputs/global/paper_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('baseline U'):
            view_name = line.split()[1]
            view_index = global_queries.index(view_name)
        elif 'Took' in line:
            time_to_execute_baseline = int(line.split()[1])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            global_table[view_index].append(time_to_execute_baseline)
            # Clear all the parameters for the next round
            time_to_execute_baseline =  0
            view_index =  -1

time_to_execute_baseline =  0
view_index =  -1

avg_baseline_global_time = []

# Storing the average creation times
for l in global_table:
    avg_baseline_global_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(global_queries, avg_baseline_global_time)), columns =['Use_Query', 'Average_Baseline_Time'])
df.to_csv(f'{dir_path}/data/paper/global_{size}.csv', encoding='utf-8', index=False)