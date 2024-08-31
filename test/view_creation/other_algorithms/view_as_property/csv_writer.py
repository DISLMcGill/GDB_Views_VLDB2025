from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
num_view = 13
num_iteration = 5
dir_path = 'test/view_creation/other_algorithms/view_as_property'
size = sys.argv[1]

output_filename = f'{dir_path}/paper_{size}_new.txt'

view_create_time_table = [ [] for _ in range(num_view) ]
time_to_create_view =  0
view_index =  -1
isPath = False

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7']

with open(output_filename,'r') as data_file:
    for line in data_file:
        if line.startswith('CREATE VIEW AS'):
            view_name = line.split()[3]
            view_index = views_arr.index(view_name)
        elif 'to create views' in line: 
            time_to_create_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            view_create_time_table[view_index].append(time_to_create_view)
            # Clear all the parameters for the next round
            time_to_create_view =  0
            view_index =  -1

time_to_create_view =  0
view_index =  -1

avg_create_time = []
# Storing the average creation times
for l in view_create_time_table:
    avg_create_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_create_time)), columns =['View_Name', 'Average_View_Creation_Time'])
df.to_csv(f'{dir_path}/paper_create_{size}_new.csv', encoding='utf-8', index=False)