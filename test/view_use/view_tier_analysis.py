from matplotlib import pyplot as plt
import sys
import numpy as np
import pandas as pd 

# This script is for creating tier analysis of queries on the medium datatset

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2', 'V8_1', 'V8_2', 'V9_1', 'V9_2', 'V10', 'V11_1', 'V11_2', 'V12_1', 'V12_2', 'V14_1', 'V14_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10', 'V11', 'V12', 'V14']

# This should be consistent with the number of views used in the experiment
baseline_dir_path = 'test/view_use/baseline'
mater_dir_path = 'test/view_use/materialized'
non_mater_dir_path = 'test/view_use/non_materialized'
num_view = 24
view_use_time_table = [0] * num_view
view_cold_use_time_table = [0] * num_view
use_time =  0
view_index =  -1

# Parameters that should be passed as inputs to the program
size = 'medium'

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

baseline_local_basic_df = pd.read_csv(f'{baseline_dir_path}/baseline_local_basic_{size}.csv')
mater_local_basic_df = pd.read_csv(f'{mater_dir_path}/materialized_use_local_basic_{size}.csv')
non_mater_local_basic_df = pd.read_csv(f'{non_mater_dir_path}/non_materialized_use_local_basic_{size}.csv')

baseline_local_complex_df = pd.read_csv(f'{baseline_dir_path}/baseline_local_complex_{size}.csv')
mater_local_complex_df = pd.read_csv(f'{mater_dir_path}/materialized_use_local_complex_{size}.csv')
non_mater_local_complex_df = pd.read_csv(f'{non_mater_dir_path}/non_materialized_use_local_complex_{size}.csv')

baseline_global_df = pd.read_csv(f'{baseline_dir_path}/baseline_global_{size}.csv')
mater_global_df = pd.read_csv(f'{mater_dir_path}/materialized_use_global_{size}.csv')
non_mater_global_df = pd.read_csv(f'{non_mater_dir_path}/non_materialized_use_global_{size}.csv')

baseline_thesis_df = pd.read_csv(f'{baseline_dir_path}/baseline_thesis_{size}.csv')
mater_thesis_df = pd.read_csv(f'{mater_dir_path}/materialized_use_thesis_{size}.csv')
non_mater_thesis_df = pd.read_csv(f'{non_mater_dir_path}/non_materialized_use_thesis_{size}.csv')

#################### Tier 1 Plot (just for medium) ###############################
tier1_views = ['V4_1', 'V4_2', 'V6_1', 'V7_1', 'V7_2']

baseline_local_basic =  baseline_local_basic_df.loc[baseline_local_basic_df['View_Name'].isin(tier1_views), 'Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_local_basic =  mater_local_basic_df.loc[mater_local_basic_df['View_Name'].isin(tier1_views), 'Average_Materialized_View_Use_Time'].tolist()
non_mater_local_basic =  non_mater_local_basic_df.loc[non_mater_local_basic_df['View_Name'].isin(tier1_views), 'Average_Non_Materialized_View_USE_Time'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(tier1_views))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

plt.bar(r1, baseline_local_basic, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')

plt.bar(r2, mater_local_basic, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_basic, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_basic))], tier1_views)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
plt.savefig(f"test/evaluation_plots/use_comparison/tier1_local_basic_log.pdf", format="pdf", bbox_inches="tight", dpi=150)
plt.close()

#################### Tier 2 Plot (just for medium) ###############################
tier2_views = ['V1_1', 'V1_2', 'V2_1', 'V3_2', 'V6_2', 'V8_2', 'V9_1', 'V9_2', 'V11_2', 'V14_1', 'V14_2']

baseline_local_basic =  baseline_local_basic_df.loc[baseline_local_basic_df['View_Name'].isin(tier2_views), 'Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_local_basic =  mater_local_basic_df.loc[mater_local_basic_df['View_Name'].isin(tier2_views), 'Average_Materialized_View_Use_Time'].tolist()
non_mater_local_basic =  non_mater_local_basic_df.loc[non_mater_local_basic_df['View_Name'].isin(tier2_views), 'Average_Non_Materialized_View_USE_Time'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(tier2_views))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

plt.bar(r1, baseline_local_basic, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')

plt.bar(r2, mater_local_basic, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_basic, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_basic))], tier2_views)
plt.xticks(rotation = 315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')

ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
plt.savefig(f"test/evaluation_plots/use_comparison/tier2_local_basic_log.pdf", format="pdf", bbox_inches="tight", dpi=150)

#plt.savefig(f"test/evaluation_plots/use_comparison/tier2_local_basic.pdf", format="pdf", bbox_inches="tight", dpi=150)
plt.close()

#################### Tier 3 Plot (just for medium) ###############################
tier3_views = ['V2_2', 'V3_1', 'V5', 'V8_1', 'V10', 'V11_1', 'V12_1', 'V12_2']

baseline_local_basic =  baseline_local_basic_df.loc[baseline_local_basic_df['View_Name'].isin(tier3_views), 'Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_local_basic =  mater_local_basic_df.loc[mater_local_basic_df['View_Name'].isin(tier3_views), 'Average_Materialized_View_Use_Time'].tolist()
non_mater_local_basic =  non_mater_local_basic_df.loc[non_mater_local_basic_df['View_Name'].isin(tier3_views), 'Average_Non_Materialized_View_USE_Time'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(tier3_views))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

plt.bar(r1, baseline_local_basic, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')

plt.bar(r2, mater_local_basic, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_basic, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_basic))], tier3_views)
plt.xticks(rotation = 315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')

ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
plt.savefig(f"test/evaluation_plots/use_comparison/tier3_local_basic_log.pdf", format="pdf", bbox_inches="tight", dpi=150)

#plt.savefig(f"test/evaluation_plots/use_comparison/tier2_local_basic.pdf", format="pdf", bbox_inches="tight", dpi=150)
plt.close()