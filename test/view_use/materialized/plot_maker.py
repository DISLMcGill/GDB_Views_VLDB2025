from matplotlib import pyplot as plt
import sys
import numpy as np
import pandas as pd 

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

# This should be consistent with the number of views used in the experiment
materialized_dir_path = 'test/view_use/materialized'
num_view = 24
view_use_time_table = [0] * num_view
view_cold_use_time_table = [0] * num_view
use_time =  0
view_index =  -1

# Parameters that should be passed as inputs to the program
size = sys.argv[1]

# Defining the x-axis data 
variant1_views = []
variant2_views = []

for v in views_arr:
    if "_2" in v:
        variant2_views.append(views_arr.index(v))
    elif "_1" in v:
        variant1_views.append(views_arr.index(v))
    else:
        variant1_views.append(views_arr.index(v))
        variant2_views.append(-1) # indicator that the second variant does not exist


# Loading the dataframes

local_basic_df = pd.read_csv(f'{materialized_dir_path}/materialized_use_local_basic_{size}.csv')
local_complex_df = pd.read_csv(f'{materialized_dir_path}/materialized_use_local_complex_{size}.csv')


#################### Local Basic Plot ###############################

local_basic_warm_time =  local_basic_df['Average_Materialized_View_Use_Time'].tolist()
local_basic_cold_time =  local_basic_df['Materialized_Cold_Use_Time'].tolist()

var1_local_basic_warm_bars = []
var2_local_basic_warm_bars = []
var1_local_basic_cold_bars = []
var2_local_basic_cold_bars = []

# Populating the mean and std of the measurements into lists for plotting
for v_idx in variant1_views:
    var1_local_basic_warm_bars.append(local_basic_warm_time[v_idx])
    var1_local_basic_cold_bars.append(local_basic_cold_time[v_idx])
for v_idx in variant2_views:
    if v_idx == -1:
        var2_local_basic_warm_bars.append(0)
        var2_local_basic_cold_bars.append(0)
    else:
        var2_local_basic_warm_bars.append(local_basic_warm_time[v_idx])
        var2_local_basic_cold_bars.append(local_basic_cold_time[v_idx])

# The x position of bars
barWidth = 0.3
r1 = np.arange(len(var1_local_basic_warm_bars))
r2 = [x + barWidth for x in r1]
 
plt.bar(r1, var1_local_basic_warm_bars, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Variant 1')
plt.bar(r1, list(np.array(var1_local_basic_cold_bars) - np.array(var1_local_basic_warm_bars)), bottom = var1_local_basic_warm_bars, width = barWidth, color = '#F88379', edgecolor = 'black', capsize=7)
 
# Create cyan bars for variant 2
plt.bar(r2, var2_local_basic_warm_bars, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Variant 2')
plt.bar(r2, list(np.array(var2_local_basic_cold_bars) - np.array(var2_local_basic_warm_bars)), bottom = var2_local_basic_warm_bars, width = barWidth, color = '#F88379', edgecolor = 'black', capsize=7, label='Cold Use Overhead')

plt.xlabel("View Name")
plt.ylabel("Average Materialized USE Time (ms)")
plt.xticks([r + barWidth for r in range(len(var1_local_basic_warm_bars))], views_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_materialized/materialized_time_local_basic_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()



#################### Local Complex Plot ###############################

local_complex_warm_time =  local_complex_df['Average_Materialized_Use_Time'].tolist()
local_complex_cold_time =  local_complex_df['Average_Materialized_Cold_Use_Time'].tolist()
use_query_names = local_complex_df['Use_Query'].tolist()

# The x position of bars
barWidth = 0.3
r1 = np.arange(len(local_complex_warm_time))
r2 = [x + barWidth for x in r1]
 
plt.bar(r1, local_complex_warm_time, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Warm Processing')
#plt.bar(r1, list(np.array(variant1_cold_bars) - np.array(var1_local_basic_warm_bars)), bottom = var1_local_basic_warm_bars, width = barWidth, color = '#F88379', edgecolor = 'black', capsize=7)
 
# Create cyan bars for variant 2
plt.bar(r2, local_complex_cold_time, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Cold Processing')
#plt.bar(r2, list(np.array(variant2_cold_bars) - np.array(var2_local_basic_warm_bars)), bottom = var2_local_basic_warm_bars, width = barWidth, color = '#F88379', edgecolor = 'black', capsize=7, label='Cold Use Overhead')

plt.xlabel("USE Query")
plt.ylabel("Average Materialized USE Time (ms)")
plt.xticks([r + barWidth for r in range(len(local_complex_warm_time))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
#ax = plt.gca()
#ax.set_yscale('log')
#plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_materialized/materialized_time_local_comlpex_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()
