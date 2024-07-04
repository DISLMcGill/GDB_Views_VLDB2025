from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
dir_path = 'test/view_use/materialized/other_algorithms/view_as_property'
num_view = 24
num_iteration = 5
size = sys.argv[1]

view_use_time_table = [ [] for _ in range(num_view) ]
time_to_use_view =  0
view_index =  -1
view_size = 0

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

#########################################################################################
# Local Basic Data
#########################################################################################

with open(f"{dir_path}/local_basic_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            view_name = line.split()[2]
            view_index = views_arr.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            view_use_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1

avg_use_time = []

# Storing the average view use times
for l in view_use_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_use_time)), columns =['View_Name', 'Average_Materialized_View_Use_Time'])
df.to_csv(f'{dir_path}/local_basic_{size}.csv', encoding='utf-8', index=False)
