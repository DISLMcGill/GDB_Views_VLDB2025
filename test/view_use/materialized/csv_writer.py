from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
dir_path = 'test/view_use/materialized'
num_view = 24
num_iteration = 5
size = sys.argv[1]

view_use_time_table = [ [] for _ in range(num_view) ]
view_cold_use_time_table = [ [] for _ in range(num_view) ]
time_to_use_view =  0
view_index =  -1
view_size = 0
isPath = False

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

# TODO: This should be monitored manually for now
local_complex_query_names = ['U1', 'U2', 'U3', 'U4', 'U5']
new_local_complex_query_names = ['U1', 'U2', 'U3', 'U4', 'U5']
global_query_names = ['U6', 'U7', 'U8', 'U9', 'U10']
new_global_query_names = ['U6', 'U7', 'U8', 'U9', 'U10']
thesis_query_names = ['V4', 'V5', 'V6', 'V7', 'V8', 'V15', 'V16', 'V17', 'V18', 'V19', 'V20', 'L1_8', 'L18_19', 'G5', 'G15', 'G20']

local_complex_queries = [
    'U1 WITH VIEWS V3_1 V4_2 LOCAL MATCH (n) WHERE n IN V3_1 AND n IN V4_2 RETURN n',
    'U2 WITH VIEWS V5 V8_1 LOCAL MATCH (n) WHERE n IN V5 AND n IN V8_1 RETURN n',
    'U3 WITH VIEWS V11_1 V12_2 LOCAL MATCH (n) WHERE n IN V11_1 AND n IN V12_2 RETURN n',
    'U4 WITH VIEWS V14_2 V1_2 LOCAL MATCH (n) WHERE n IN V14_2 AND n IN V1_2 RETURN n',
    'U5 WITH VIEWS V2_2 V11_1 LOCAL MATCH (n) WHERE n IN V2_2 AND n IN V11_1 RETURN n'
]

new_complex_queries = [
    'U1 WITH WITH VIEWS V11_2 V7_2 LOCAL MATCH (n) WHERE n IN V11_2 AND n IN V7_2 RETURN n',
    'U2 WITH VIEWS V2_1 V4_1 LOCAL MATCH (n) WHERE n IN V2_1 AND n IN V4_1 RETURN n',
    'U3 WITH VIEWS V3_2 V14_1 LOCAL MATCH (n) WHERE n IN V3_2 AND n IN V14_1 RETURN n',
    'U4 WITH VIEWS V8_2 V9_2 LOCAL MATCH (n) WHERE n IN V8_2 AND n IN V9_2 RETURN n',
    'U5 WITH VIEWS V6_1 V1_2 LOCAL MATCH (n) WHERE n IN V6_1 AND n IN V1_2 RETURN n'
]

global_queries = [
    'U6 WITH VIEWS V7_1 GLOBAL',
    'U7 WITH VIEWS V7_2 GLOBAL',
    'U8 WITH VIEWS V5 GLOBAL',
    'U9 WITH VIEWS V5 V6_2 GLOBAL',
    'U10 WITH VIEWS V5 V7_1 GLOBAL'
]

new_global_queries = [
    'U6 WITH VIEWS V9_1 GLOBAL',
    'U7 WITH VIEWS V7_2 GLOBAL',
    'U8 WITH VIEWS V2_1 GLOBAL',
    'U9 WITH VIEWS V3_2 V6_2 GLOBAL',
    'U10 WITH VIEWS V7_1 V6_1 GLOBAL'
]

thesis_queries = [
    'V4 WITH VIEWS V4 LOCAL',
    'V5 WITH VIEWS V5 LOCAL',
    'V6 WITH VIEWS V6 LOCAL',
    'V7 WITH VIEWS V7 LOCAL',
    'V8 WITH VIEWS V8 LOCAL',
    'V15 WITH VIEWS V15 LOCAL',
    'V16 WITH VIEWS V16 LOCAL',
    'V17 WITH VIEWS V17 LOCAL',
    'V18 WITH VIEWS V18 LOCAL',
    'V19 WITH VIEWS V19 LOCAL',
    'V20 WITH VIEWS V20 LOCAL',
    'L1_8 WITH VIEWS V1 V8 LOCAL',
    'L18_19 WITH VIEWS V18 V19 LOCAL',
    'G5 WITH VIEWS V5 GLOBAL',
    'G15 WITH VIEWS V15 GLOBAL',
    'G20 WITH VIEWS V20 GLOBAL',
]

#########################################################################################
# Local Basic Data
#########################################################################################

with open(f"{dir_path}/outputs/local_basic/warm_{size}.txt",'r') as data_file:
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
            isPath = False 
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

time_to_use_view =  0
view_index =  -1

with open(f"{dir_path}/outputs/local_basic/cold_{size}.txt",'r') as cold_file:
    for line in cold_file:
        if line.startswith('WITH VIEWS'):
            view_name = line.split()[2]
            view_index = views_arr.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            view_cold_use_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False 
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

# Special Output files for huge views that could not be run with others [This part needs to be monitored manually]
if size == 'medium':
    with open(f"{dir_path}/outputs/local_basic/warm_medium_special_V5.txt",'r') as cold_file:
        for line in cold_file:
            if line.startswith('WITH VIEWS'):
                view_name = line.split()[2]
                view_index = views_arr.index(view_name)
            elif 'to use view' in line:
                time_to_use_view = int(line.split()[1][:-2])
            elif '*********************************' in line:
                # Update the table with the latest parameter values
                if view_name == 'V5':
                    view_use_time_table[view_index].append(time_to_use_view)
                
                # Clear all the parameters for the next round
                isPath = False 
                time_to_use_view =  0
                view_index =  -1
                view_size = 0

    with open(f"{dir_path}/outputs/local_basic/warm_medium_special_V10.txt",'r') as cold_file:
        for line in cold_file:
            if line.startswith('WITH VIEWS'):
                view_name = line.split()[2]
                view_index = views_arr.index(view_name)
            elif 'to use view' in line:
                time_to_use_view = int(line.split()[1][:-2])
            elif '*********************************' in line:
                # Update the table with the latest parameter values
                if view_name == 'V10':
                    view_use_time_table[view_index].append(time_to_use_view)
                # Clear all the parameters for the next round
                isPath = False 
                time_to_use_view =  0
                view_index =  -1
                view_size = 0

avg_use_time = []
avg_use_cold_time = []

# Storing the average view use times
for l in view_use_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

for l in view_cold_use_time_table:
    if not l:
        avg_use_cold_time.append(0)
    else:
        avg_use_cold_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_use_time, avg_use_cold_time)), columns =['View_Name', 'Average_Materialized_View_Use_Time', 'Materialized_Cold_Use_Time'])
df.to_csv(f'{dir_path}/materialized_use_local_basic_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Local Complex Data
#########################################################################################

use_local_complex_time_table = [ [] for _ in range(num_view) ]
use_cold_local_complex_time_table = [ [] for _ in range(num_view) ]

with open(f"{dir_path}/outputs/local_complex/warm_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            first = line.split()[2]
            second = line.split()[3]
            view_name = ''
            for q in local_complex_queries:
                if first in q and second in q:
                    view_name = q.split()[0]
                    break
            view_index = local_complex_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            use_local_complex_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False 
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

time_to_use_view =  0
view_index =  -1

with open(f"{dir_path}/outputs/local_complex/cold_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            first = line.split()[2]
            second = line.split()[3]
            view_name = ''
            for q in local_complex_queries:
                if first in q and second in q:
                    view_name = q.split()[0]
                    break
            view_index = local_complex_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            use_cold_local_complex_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False 
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

avg_use_time = []
avg_use_cold_time = []

# Storing the average view use times
for l in use_local_complex_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

for l in use_cold_local_complex_time_table:
    if not l:
        avg_use_cold_time.append(0)
    else:
        avg_use_cold_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(local_complex_query_names, avg_use_time, avg_use_cold_time)), columns =['Use_Query', 'Average_Materialized_Use_Time', 'Average_Materialized_Cold_Use_Time'])
df.to_csv(f'{dir_path}/materialized_use_local_complex_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Global Data
#########################################################################################

use_global_time_table = [ [] for _ in range(len(global_query_names)) ]
# use_cold_local_complex_time_table = [ [] for _ in range(num_view) ]

time_to_use_view =  0
view_index =  -1

is_use = False

with open(f"{dir_path}/outputs/global/warm_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            global_index = line.find('GLOBAL')
            use_first_substring = line[:global_index + len('GLOBAL')].strip()
            view_name = ''
            for q in global_queries:
                if use_first_substring in q:
                    view_name = q.split()[0]
                    break
            view_index = global_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
            is_use = True 
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            use_global_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            time_to_use_view =  0
            view_index =  -1
            is_use = False

time_to_use_view =  0
view_index =  -1
is_use = False

avg_global_time = []
#avg_use_cold_time = []

# Storing the average view use times
for l in use_global_time_table:
    if not l:
        avg_global_time.append(0)
    else:
        avg_global_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(global_query_names, avg_global_time)), columns =['Use_Query', 'Average_Materialized_Use_Time'])
df.to_csv(f'{dir_path}/materialized_use_global_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# New Global Data
#########################################################################################

use_global_time_table = [ [] for _ in range(len(global_query_names)) ]
# use_cold_local_complex_time_table = [ [] for _ in range(num_view) ]

time_to_use_view =  0
view_index =  -1

is_use = False

with open(f"{dir_path}/outputs/global/warm_{size}_new.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            global_index = line.find('GLOBAL')
            use_first_substring = line[:global_index + len('GLOBAL')].strip()
            view_name = ''
            for q in new_global_queries:
                if use_first_substring in q:
                    view_name = q.split()[0]
                    break
            view_index = new_global_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
            is_use = True 
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            use_global_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            time_to_use_view =  0
            view_index =  -1
            is_use = False

time_to_use_view =  0
view_index =  -1
is_use = False

avg_global_time = []
#avg_use_cold_time = []

# Storing the average view use times
for l in use_global_time_table:
    if not l:
        avg_global_time.append(0)
    else:
        avg_global_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(global_query_names, avg_global_time)), columns =['Use_Query', 'Average_Materialized_Use_Time'])
df.to_csv(f'{dir_path}/materialized_use_global_new_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Thesis Queries Data
#########################################################################################

thesis_time_table = [ [] for _ in range(len(thesis_queries)) ]
thesis_cold_time_table = [ [] for _ in range(len(thesis_queries)) ]

time_to_use_view =  0
view_index =  -1

with open(f"{dir_path}/outputs/thesis/warm_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            use_first_substring = line[:line.find('MATCH')].strip()
            view_name = ''
            for q in thesis_queries:
                if use_first_substring in q:
                    view_name = q.split()[0]
                    break
            view_index = thesis_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            thesis_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1

with open(f"{dir_path}/outputs/thesis/cold_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            use_first_substring = line[:line.find('MATCH')].strip()
            view_name = ''
            for q in thesis_queries:
                if use_first_substring in q:
                    view_name = q.split()[0]
                    break
            view_index = thesis_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            thesis_cold_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            time_to_use_view =  0
            view_index =  -1

avg_thesis_time = []
avg_thesis_cold_time = []

# Storing the average view use times
for l in thesis_time_table:
    if not l:
        avg_thesis_time.append(0)
    else:
        avg_thesis_time.append(mean(l))
for l in thesis_cold_time_table:
    if not l:
        avg_thesis_cold_time.append(0)
    else:
        avg_thesis_cold_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(thesis_query_names, avg_thesis_time, avg_thesis_cold_time )), columns =['Use_Query', 'Average_Materialized_Use_Time', 'Average_Materialized_Cold_Use_Time'])
df.to_csv(f'{dir_path}/materialized_use_thesis_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# New Local Complex Data
#########################################################################################

warm_time = [ [] for _ in range(len(thesis_queries)) ]

time_to_use_view =  0
view_index =  -1

with open(f"{dir_path}/outputs/local_complex/warm_{size}_new.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH VIEWS'):
            use_first_substring = line[:line.find('MATCH')].strip()
            view_name = ''
            for q in new_complex_queries:
                if use_first_substring in q:
                    view_name = q.split()[0]
                    break
            view_index = new_local_complex_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1][:-2])
        elif '*********************************' in line:
            # Update the table with the latest parameter values
            warm_time[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1

avg_warm_time = []

# Storing the average view use times
for l in warm_time:
    if not l:
        avg_warm_time.append(0)
    else:
        avg_warm_time.append(mean(l))


# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(new_local_complex_query_names, avg_warm_time )), columns =['Use_Query', 'Average_Materialized_Use_Time'])
df.to_csv(f'{dir_path}/materialized_use_local_complex_new_{size}.csv', encoding='utf-8', index=False)