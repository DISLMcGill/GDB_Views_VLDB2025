from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

# Parameteres to set before running the code
# This should be consistent with the number of views used in the experiment
num_view = 24
num_iteration = 5
size = sys.argv[1]
dir_path = 'test/view_creation/other_algorithms/view_as_property'

view_create_time_table = [ [] for _ in range(num_view) ]
time_to_create_view =  0
view_index =  -1

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

default_create_time_df = pd.read_csv(f'test/view_creation/view_creation_time_{size}.csv')
modified_create_time_df = pd.read_csv(f'{dir_path}/view_creation_time_{size}.csv')


plt.plot(default_create_time_df['View_Name'], default_create_time_df['Average_View_Creation_Time'], color='b', label='Default Method')
plt.plot(modified_create_time_df['View_Name'], modified_create_time_df['Average_View_Creation_Time'], color='r', label='Modified Method')
plt.xlabel("View Name")
plt.ylabel("Creation Time(ms)")
plt.xticks(rotation=315)
plt.legend()
plt.savefig(f"test/evaluation_plots/create_view_as_property/create_{size}.pdf", format="pdf", bbox_inches="tight")

plt.close()
