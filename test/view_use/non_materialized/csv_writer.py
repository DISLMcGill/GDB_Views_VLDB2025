from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameters to be set before running the code
# This should be consistent with the number of views used in the experiment
dir_path = 'test/view_use/non_materialized'
num_view = 24
num_iteration = 5
size = sys.argv[1]

view_use_time_table = [ [] for _ in range(num_view) ]
view_cold_use_time_table = [ [] for _ in range(num_view) ]
time_to_use_view =  0
view_index =  -1
view_size = 0
isPath = False

# TODO: The following lists should be maintained manually for now
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']
local_complex_query_names = ['U1', 'U2', 'U3', 'U4', 'U5']
local_complex_new_query_names = ['U1', 'U2', 'U3', 'U4', 'U5']

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

new_local_complex_queries = [
    'U1 WITH WITH VIEWS V11_2 V7_2 LOCAL MATCH (n) WHERE n IN V11_2 AND n IN V7_2 RETURN n',
    'U2 WITH VIEWS V2_1 V4_1 LOCAL MATCH (n) WHERE n IN V2_1 AND n IN V4_1 RETURN n',
    'U3 WITH VIEWS V3_2 V14_1 LOCAL MATCH (n) WHERE n IN V3_2 AND n IN V14_1 RETURN n',
    'U4 WITH VIEWS V8_2 V9_2 LOCAL MATCH (n) WHERE n IN V8_2 AND n IN V9_2 RETURN n',
    'U5 WITH VIEWS V6_1 V1_2 LOCAL MATCH (n) WHERE n IN V6_1 AND n IN V1_2 RETURN n'
]

global_queries = [
    'V7_1',
    'V7_2',
    'V5',
    'V5 V6_2',
    'V5 V7_1'
]

new_global_queries = [
    'V9_1',
    'V7_2',
    'V2_1',
    'V3_2 V6_2',
    'V7_1 V6_1'
]

#########################################################################################
# Local Basic Data
#########################################################################################

is_use = False

with open(f"{dir_path}/outputs/local_basic/warm_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            view_name = line.split()[5]
            view_index = views_arr.index(view_name)
        elif 'to use view' in line:
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

with open(f"{dir_path}/outputs/local_basic/cold_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            view_name = line.split()[5]
            view_index = views_arr.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            view_cold_use_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False 
            is_use = False
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

time_to_use_view =  0
view_index =  -1

avg_use_time = []
avg_cold_use_time = []

# Storing the average creation times
for l in view_use_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))
for l in view_cold_use_time_table:
    if not l:
        avg_cold_use_time.append(0)
    else:
        avg_cold_use_time.append(mean(l))


# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(views_arr, avg_use_time, avg_cold_use_time)), columns =['View_Name', 'Average_Non_Materialized_View_USE_Time', 'Average_Non_Materialized_Cold_View_USE_Time'])
df.to_csv(f'{dir_path}/non_materialized_use_local_basic_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Local Complex Data
#########################################################################################

local_complex_time_table = [ [] for _ in range(len(local_complex_query_names)) ]
local_complex_cold_time_table = [ [] for _ in range(len(local_complex_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/local_complex/warm_{size}.txt",'r') as data_file:
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
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            # print(f"Appending {time_to_use_view} to {view_index}")
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

with open(f"{dir_path}/outputs/local_complex/cold_{size}.txt",'r') as data_file:
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
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            local_complex_cold_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            isPath = False
            is_use = False 
            time_to_use_view =  0
            view_index =  -1
            view_size = 0

avg_use_time = []
avg_cold_use_time = []

# Storing the average creation times
for l in local_complex_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))
for l in local_complex_cold_time_table:
    if not l:
        avg_cold_use_time.append(0)
    else:
        avg_cold_use_time.append(mean(l))


# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(local_complex_query_names, avg_use_time, avg_cold_use_time)), columns =['USE_Query', 'Average_Non_Materialized_USE_Time', 'Average_Non_Materialized_Cold_USE_Time'])
df.to_csv(f'{dir_path}/non_materialized_use_local_complex_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Global Data [Just warm data for now]
#########################################################################################

global_time_table = [ [] for _ in range(len(global_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/global/warm_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            views = line[line.find('GLOBAL') + len('GLOBAL'):line.find('MATCH', line.find('GLOBAL'))].strip()
            view_name = ''
            view_index = -1
            for q in global_queries:
                if views == q:
                    view_index = global_queries.index(q)
                    break
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            # print(f"Appending {time_to_use_view} to {view_index}")
            global_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            is_use = False
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1
is_use = False

avg_use_time = []
#avg_cold_use_time = []

# Storing the average creation times
for l in global_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

print(global_time_table)

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(global_query_names, avg_use_time)), columns =['USE_Query', 'Average_Non_Materialized_USE_Time'])
df.to_csv(f'{dir_path}/non_materialized_use_global_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# Thesis Data 
#########################################################################################

thesis_time_table = [ [] for _ in range(len(thesis_query_names)) ]
thesis_cold_time_table = [ [] for _ in range(len(thesis_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/thesis/warm_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            view_index = -1 

            if 'GLOBAL' in line: 
                view_name = f"G{line.split()[4][1:]}"
                view_index = thesis_query_names.index(view_name)
            elif 'BASIC' in line: 
                view_name = line.split()[5]
                view_index = thesis_query_names.index(view_name)
            elif 'COMPLEX' in line:
                first = line.split()[5]
                second = line.split()[6]
                view_name = f"L{first[1:]}_{second[1:]}"
                view_index = thesis_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            # print(f"Appending {time_to_use_view} to {view_index}")
            thesis_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            is_use = False
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1
is_use = False

with open(f"{dir_path}/outputs/thesis/cold_{size}.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            view_index = -1 

            if 'GLOBAL' in line: 
                view_name = f"G{line.split()[4][1:]}"
                view_index = thesis_query_names.index(view_name)
            elif 'BASIC' in line: 
                view_name = line.split()[5]
                view_index = thesis_query_names.index(view_name)
            elif 'COMPLEX' in line:
                first = line.split()[5]
                second = line.split()[6]
                view_name = f"L{first[1:]}_{second[1:]}"
                view_index = thesis_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            # print(f"Appending {time_to_use_view} to {view_index}")
            thesis_cold_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            is_use = False
            time_to_use_view =  0
            view_index =  -1

avg_use_time = []
avg_cold_use_time = []

# Storing the average creation times
for l in thesis_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

for l in thesis_cold_time_table:
    if not l:
        avg_cold_use_time.append(0)
    else:
        avg_cold_use_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(thesis_query_names, avg_use_time, avg_cold_use_time)), columns =['USE_Query', 'Average_Non_Materialized_USE_Time', 'Average_Non_Materialized_Cold_USE_Time'])
df.to_csv(f'{dir_path}/non_materialized_use_thesis_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# New Local Complex Data
#########################################################################################

local_complex_time_table = [ [] for _ in range(len(local_complex_new_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/local_complex/warm_{size}_new.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            first= line.split()[5]
            second = line.split()[6]
            view_name = ''
            for q in new_local_complex_queries:
                if first in q and second in q:
                    view_name = q.split()[0]
                    break
            view_index = local_complex_new_query_names.index(view_name)
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
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
df.to_csv(f'{dir_path}/non_materialized_use_local_complex_new_{size}.csv', encoding='utf-8', index=False)

#########################################################################################
# New Global Data [Just warm data for now]
#########################################################################################

global_time_table = [ [] for _ in range(len(global_query_names)) ]

is_use = False
with open(f"{dir_path}/outputs/global/warm_{size}_new.txt",'r') as data_file:
    for line in data_file:
        if line.startswith('WITH NON_MATERIALIZED VIEWS'):
            views = line[line.find('GLOBAL') + len('GLOBAL'):line.find('MATCH', line.find('GLOBAL'))].strip()
            view_name = ''
            view_index = -1
            for q in new_global_queries:
                if views == q:
                    view_index = new_global_queries.index(q)
                    break
        elif 'to use view' in line:
            time_to_use_view = int(line.split()[1])
            is_use = True
        elif '*********************************' in line and is_use:
            # Update the table with the latest parameter values
            # print(f"Appending {time_to_use_view} to {view_index}")
            global_time_table[view_index].append(time_to_use_view)
            # Clear all the parameters for the next round
            is_use = False
            time_to_use_view =  0
            view_index =  -1

time_to_use_view =  0
view_index =  -1
is_use = False

avg_use_time = []
#avg_cold_use_time = []

# Storing the average creation times
for l in global_time_table:
    if not l:
        avg_use_time.append(0)
    else:
        avg_use_time.append(mean(l))

# Writing mean of view_creation and view_cold_creation to a dataframe
df = pd.DataFrame(list(zip(new_global_query_names, avg_use_time)), columns =['USE_Query', 'Average_Non_Materialized_USE_Time'])
df.to_csv(f'{dir_path}/non_materialized_use_global_new_{size}.csv', encoding='utf-8', index=False)