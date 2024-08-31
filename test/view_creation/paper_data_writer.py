from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
num_view = 13
num_iteration = 5
dir_path = 'test/view_creation'
size = sys.argv[1]

output_cold_with_init_laod_filename = f'{dir_path}/outputs/paper_{size}_new.txt'

view_size_table = [0] * num_view
view_create_time_table = [ [] for _ in range(num_view) ]
view_create_cold_init_load_time_table = [ [] for _ in range(num_view) ]
time_to_create_view =  0
view_index =  -1
view_size = 0
isPath = False

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

with open(output_cold_with_init_laod_filename,'r') as cold_revised_file:
    for line in cold_revised_file:
        if line.startswith('ViewSql is'):
            view_name = line.split()[5]
            if view_name == 'V_test':
                final_time = False
            else:
                view_index = views_arr.index(view_name)
                final_time = True
        elif 'to create views' in line and final_time: 
            time_to_create_view = int(line.split()[1][:-2])
        elif '************************************' in line:
            # Update the table with the latest parameter values
            view_create_cold_init_load_time_table[view_index].append(time_to_create_view)
            # Clear all the parameters for the next round
            isPath = False 
            time_to_create_view =  0
            view_index =  -1
            view_size = 0

avg_cold_create_init_load_time = []

# Storing the average creation times
for l in view_create_cold_init_load_time_table:
    avg_cold_create_init_load_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_cold_create_init_load_time)), columns =['View_Name', 'Average_View_Creation_Time'])
df.to_csv(f'test/view_creation/paper_creation_time_{size}_new.csv', encoding='utf-8', index=False)


# Writing ratio of time_to_create / size
# avg_cold_create_init_load_time[i] / view_size_table[i]
'''ratio = []
for i in range(num_view):
    ratio.append(avg_cold_create_init_load_time[i]/view_size_table[i])

print(avg_cold_create_init_load_time)
print(view_size_table)
print(ratio)
df = pd.DataFrame(list(zip(views_arr, ratio,view_size_table )), columns =['View_Name', 'Time_to_Size_Ratio', 'Size'])
df.to_csv(f'{dir_path}/view_ratio_{size}.csv', encoding='utf-8', index=False)'''