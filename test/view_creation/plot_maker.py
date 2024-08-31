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
dir_path = 'test/view_creation'

view_size_table = [0] * num_view
view_create_time_table = [ [] for _ in range(num_view) ]
view_create_cold_time_table = [ [] for _ in range(num_view) ]
time_to_create_view =  0
view_index =  -1
view_size = 0
isPath = False

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

create_time_df = pd.read_csv(f'{dir_path}/view_creation_time_{size}.csv')
ratio_df = pd.read_csv(f'{dir_path}/view_ratio_{size}.csv')

view_create_time_table = create_time_df['Average_View_Creation_Time'].tolist()
view_create_cold_time_table = create_time_df['Average_Cold_Creation_With_Init_Load_Time'].tolist()
view_size_table = ratio_df['Size'].tolist()

print(view_create_time_table)
print("************")
print(view_create_cold_time_table)
print("************")
print("Size array is")
print(view_size_table)
print("************")

variant1_views = []
variant2_views = []
variant1_avg_bars = []
variant2_avg_bars = []
variant1_bars = []
variant2_bars = []
variant1_size = []
variant2_size = []
variant1_cold_avg_bars = []
variant2_cold_avg_bars = []
variant1_cold_bars = []
variant2_cold_bars = []
yer1 = []
yer2 = []
yer1_cold = []
yer2_cold = []

for v in views_arr:
    if "_2" in v:
        variant2_views.append(views_arr.index(v))
    elif "_1" in v:
        variant1_views.append(views_arr.index(v))
    else:
        variant1_views.append(views_arr.index(v))
        variant2_views.append(-1) # indicator that the second variant does not exist

# Populating the mean and std of the measurements into lists for plotting
for v_idx in variant1_views:
    variant1_bars.append(view_create_time_table[v_idx])
    variant1_cold_bars.append(view_create_cold_time_table[v_idx])
    variant1_size.append(view_size_table[v_idx])
for v_idx in variant2_views:
    if v_idx == -1:
        variant2_bars.append(0)
        variant2_cold_bars.append(0)
        variant2_size.append(0)
    else:
        variant2_bars.append(view_create_time_table[v_idx])
        variant2_cold_bars.append(view_create_cold_time_table[v_idx])
        variant2_size.append(view_size_table[v_idx])

# The x position of bars
barWidth = 0.3
r1 = np.arange(len(variant1_bars))
r2 = [x + barWidth for x in r1]
 
# Create blue bars for variant 1 
plt.bar(r1, variant1_bars, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Variant 1')
#plt.bar(r1, list(np.array(variant1_cold_bars) - np.array(variant1_bars)), bottom = variant1_bars, width = barWidth, color = '#F88379', edgecolor = 'black', capsize=7)
 
# Create cyan bars for variant 2
plt.bar(r2, variant2_bars, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Variant 2')
#plt.bar(r2, list(np.array(variant2_cold_bars) - np.array(variant2_bars)), bottom = variant2_bars, width = barWidth, color = '#F88379', edgecolor = 'black', capsize=7, label='Cold Start Overhead')

plt.xlabel("View Name")
plt.ylabel("View Creation Time (ms)")
plt.xticks([r + barWidth for r in range(len(variant1_bars))], views_names)
plt.xticks(rotation=315)
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
plt.legend(bbox_to_anchor = (1.3, 0.9), loc='center right')
plt.savefig(f"test/evaluation_plots/create/warm_create_time_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

# Create blue bars for variant 1 
plt.bar(r1, variant1_cold_bars, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Variant 1')
 
# Create cyan bars for variant 2
plt.bar(r2, variant2_cold_bars, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Variant 2')

plt.xlabel("View Name")
plt.ylabel("View Creation Time (ms)")
plt.xticks([r + barWidth for r in range(len(variant1_bars))], views_names)
plt.xticks(rotation=315)
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
plt.legend(bbox_to_anchor = (1.3, 0.9), loc='center right')
plt.savefig(f"test/evaluation_plots/create/cold_init_load_create_time_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

# Make View Size plot
barWidth = 0.3
r1 = np.arange(len(variant1_size))
r2 = [x + barWidth for x in r1]
 
# Create blue bars for variant 1 
plt.bar(r1, variant1_size, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Variant 1')
 
# Create cyan bars for variant 2
plt.bar(r2, variant2_size, width = barWidth, color = 'cyan', edgecolor = 'black', capsize=7, label='Variant 2')

plt.xlabel("View Name")
plt.ylabel("View Size")
plt.xticks([r + barWidth for r in range(len(variant1_bars))], views_names)
plt.xticks(rotation=315)
plt.legend()
plt.yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
plt.savefig(f"test/evaluation_plots/size/view_size_{size}.pdf", format="pdf", bbox_inches="tight")
plt.close()