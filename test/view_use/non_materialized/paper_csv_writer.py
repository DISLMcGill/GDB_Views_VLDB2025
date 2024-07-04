from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
dir_path = 'test/view_use/non_materialized'
num_view = 13
num_iteration = 5
size = sys.argv[1]

view_use_time_table = [ [] for _ in range(num_view) ]
view_cold_use_time_table = [ [] for _ in range(num_view) ]
time_to_use_view =  0
view_index =  -1
view_size = 0
isPath = False

# TODO: The following lists should be maintained manually for now
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7']
local_complex_query_names = ['U1', 'U2', 'U3', 'U4', 'U5']
global_query_names = ['U6', 'U7', 'U8', 'U9', 'U10']


local_complex_queries = [
    'U1 WITH VIEWS V2_2 V5 LOCAL MATCH (n) WHERE n IN V2_2 AND n IN V5 RETURN n',
    'U2 WITH VIEWS V2_1 V4_1 LOCAL MATCH (n) WHERE n IN V2_1 AND n IN V4_1 RETURN n',
    'U3 WITH VIEWS V6_2 V7_1 LOCAL MATCH (n) WHERE n IN V6_2 AND n IN V7_1 RETURN n',
    'U4 WITH VIEWS V3_1 V4_2 LOCAL MATCH (n) WHERE n IN V3_1 AND n IN V4_2 RETURN n',
    'U5 WITH VIEWS V6_1 V1_2 LOCAL MATCH (n) WHERE n IN V6_1 AND n IN V1_2 RETURN n'
]

global_queries = [
    'V5',
    'V7_2',
    'V2_1',
    'V3_2 V6_2',
    'V7_1 V6_1'
]

#########################################################################################
# Local Basic Data
#########################################################################################

is_use = False

with open(f"{dir_path}/outputs/local_basic/paper_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            view_name = line.split()[5]
            view_index = views_arr.index(view_name)
        elif 'to execute query' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            view_use_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False 
            is_use = False
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

time_to_use_view =  0
view_index =  -1
is_use = False 

avg_use_time = []

# Storing the average creation times
for l in view_use_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))


# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_use_time)), columns =['View_Name', 'Average_Non_Materialized_View_USE_Time'])
df.to_csv(f'{dir_path}/data/paper/local_basic_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Local Complex Data
#########################################################################################

local_complex_time_table = [ [] for _ in range(len(local_complex_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/local_complex/paper_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            first= line.split()[5]
            second = line.split()[6]
            view_name = ''
            for q in local_complex_queries:
                if first in q and second in q:
                    view_name = q.split()[0]
                    break
            view_index = local_complex_query_names.index(view_name)
        elif 'to execute baseline query' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter value
            local_complex_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False 
            is_use = False
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

time_to_use_view =  0
view_index =  -1
is_use = False

avg_use_time = []

# Storing the average creation times
for l in local_complex_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))


# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(local_complex_query_names, avg_use_time)), columns =['USE_Query', 'Average_Non_Materialized_USE_Time'])
df.to_csv(f'{dir_path}/data/paper/local_complex_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Global Data
#########################################################################################

global_time_table = [ [] for _ in range(len(global_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/global/paper_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            views = line[line.find('GLOBAL') + len('GLOBAL'):line.find('MATCH', line.find('GLOBAL'))].strip()
            view_name = ''
            view_index = -1
            for q in global_queries:
                if views == q:
                    view_index = global_queries.index(q)
                    break
        elif 'to execute baseline query' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            global_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            is_use = False
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1
is_use = False

avg_use_time = []

# Storing the average creation times
for l in global_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(global_query_names, avg_use_time)), columns =['USE_Query', 'Average_Non_Materialized_USE_Time'])
df.to_csv(f'{dir_path}/data/paper/global_{size}.csv', encoding='utf-8', index=False)