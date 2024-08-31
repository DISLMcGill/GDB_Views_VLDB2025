from matplotlib import pyplot as plt
import sys
import numpy as np
import pandas as pd 
import matplotlib.patches as mpatches

# This should change to include the right name for the views and their variants
views_arr = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2']
views_names = ['V1', 'V2', 'V3', 'V4', 'V5', 'V6', 'V7']

# This should be consistent with the number of views used in the experiment
baseline_dir_path = 'test/view_use/baseline'
mater_dir_path = 'test/view_use/materialized'
non_mater_dir_path = 'test/view_use/non_materialized'

num_view = 13
view_use_time_table = [0] * num_view
view_cold_use_time_table = [0] * num_view
use_time =  0
view_index =  -1

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

baseline_local_basic_df_small = pd.read_csv(f'{baseline_dir_path}/data/paper/local_basic_small.csv')
mater_local_basic_df_small = pd.read_csv(f'{mater_dir_path}/data/paper/local_basic_small.csv')
non_mater_local_basic_df_small = pd.read_csv(f'{non_mater_dir_path}/data/paper/local_basic_small.csv')
baseline_local_basic_df_medium = pd.read_csv(f'{baseline_dir_path}/data/paper/local_basic_medium.csv')
mater_local_basic_df_medium = pd.read_csv(f'{mater_dir_path}/data/paper/local_basic_medium.csv')
non_mater_local_basic_df_medium = pd.read_csv(f'{non_mater_dir_path}/data/paper/local_basic_medium.csv')

baseline_local_complex_df_small = pd.read_csv(f'{baseline_dir_path}/data/paper/local_complex_small.csv')
mater_local_complex_df_small = pd.read_csv(f'{mater_dir_path}/data/paper/local_complex_small.csv')
non_mater_local_complex_df_small = pd.read_csv(f'{non_mater_dir_path}/data/paper/local_complex_small.csv')
baseline_local_complex_df_medium = pd.read_csv(f'{baseline_dir_path}/data/paper/local_complex_medium.csv')
mater_local_complex_df_medium = pd.read_csv(f'{mater_dir_path}/data/paper/local_complex_medium.csv')
non_mater_local_complex_df_medium = pd.read_csv(f'{non_mater_dir_path}/data/paper/local_complex_medium.csv')

baseline_global_df_small = pd.read_csv(f'{baseline_dir_path}/data/paper/global_small.csv')
mater_global_df_small = pd.read_csv(f'{mater_dir_path}/data/paper/global_small.csv')
non_mater_global_df_small = pd.read_csv(f'{non_mater_dir_path}/data/paper/global_small.csv')
baseline_global_df_medium = pd.read_csv(f'{baseline_dir_path}/data/paper/global_medium.csv')
mater_global_df_medium = pd.read_csv(f'{mater_dir_path}/data/paper/global_medium.csv')
non_mater_global_df_medium = pd.read_csv(f'{non_mater_dir_path}/data/paper/global_medium.csv')

########## Local Basic ###########

baseline_local_basic_small =  baseline_local_basic_df_small['Average_View_Baseline_Time'].tolist()
mater_new_local_basic_small =  mater_local_basic_df_small['Average_New_Materialized_View_Use_Time'].tolist()
non_mater_local_basic_small =  non_mater_local_basic_df_small['Average_Non_Materialized_View_USE_Time'].tolist()

baseline_local_basic_medium =  baseline_local_basic_df_medium['Average_View_Baseline_Time'].tolist()
mater_new_local_basic_medium =  mater_local_basic_df_medium['Average_New_Materialized_View_Use_Time'].tolist()
non_mater_local_basic_medium =  non_mater_local_basic_df_medium['Average_Non_Materialized_View_USE_Time'].tolist()

barWidth = 0.25
r1 = np.arange(len(baseline_local_basic_medium))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

fig, (ax1, ax2) = plt.subplots(2, 1, sharex=True)

# Small Database
ax1.bar(r1, baseline_local_basic_small, width = barWidth, color = '#CF9FFF', hatch='///', edgecolor = 'black', capsize=7, label='Baseline')
ax1.bar(r2, mater_new_local_basic_small, width = barWidth, color = '#93C572', hatch='xx', edgecolor = 'black', capsize=7, label='Materialized')
ax1.bar(r3, non_mater_local_basic_small, width = barWidth, color = '#FFC000', hatch='++', edgecolor = 'black', capsize=7, label='Non Materialized')

# Medium Database
ax2.bar(r1, baseline_local_basic_medium, width = barWidth, color = '#CF9FFF',hatch='///', edgecolor = 'black', capsize=7, label='Baseline')
ax2.bar(r2, mater_new_local_basic_medium, width = barWidth, color = '#93C572', hatch='xx', edgecolor = 'black', capsize=7, label='Materialized')
ax2.bar(r3, non_mater_local_basic_medium, width = barWidth, color = '#FFC000', hatch='++', edgecolor = 'black', capsize=7, label='Non Materialized')

use_query_names = baseline_local_basic_df_small['View_Name'].tolist()

ax1.set_xlabel('Small Database')
ax2.set_xlabel('Medium Database')

ax1.set_yscale('log')
ax1.grid(True, which='both', ls='dotted', axis='y')

ax2.set_xticks(range(len(use_query_names)))
ax2.set_xticklabels(use_query_names, rotation=315)  # Rotate x-axis labels
ax2.set_yscale('log')  # Set y-axis to log scale
ax2.grid(True, which='both', ls='dotted', axis='y')

# Add legend and y-axis label
legend_labels = ['Baseline', 'Materialized', 'Non Materialized']
legend = ax1.legend(legend_labels, loc='upper center', ncol=3, bbox_to_anchor=(0.5, 1.25), frameon=False)

plt.tight_layout()  # Adjust layout to prevent overlapping labels

fig.text(0.001, 0.5, 'View Usage Time (ms)', va='center', rotation='vertical')

plt.savefig(f"test/evaluation_plots/paper/use_basic_formal.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### Updated Local Complex Plot ###############################

baseline_local_complex_small =  baseline_local_complex_df_small['Average_Baseline_Time'].tolist()
mater_local_complex_small =  mater_local_complex_df_small['Average_Materialized_Use_Time'].tolist()
non_mater_local_complex_small =  non_mater_local_complex_df_small['Average_Non_Materialized_USE_Time'].tolist()

baseline_local_complex_medium =  baseline_local_complex_df_medium['Average_Baseline_Time'].tolist()
mater_local_complex_medium =  mater_local_complex_df_medium['Average_Materialized_Use_Time'].tolist()
non_mater_local_complex_medium =  non_mater_local_complex_df_medium['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = mater_local_complex_df_medium['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(mater_local_complex_df_medium))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

fig, (ax1, ax2) = plt.subplots(2, 1, sharex=True)

# Small Database
ax1.bar(r1, baseline_local_complex_small, width = barWidth, color = '#CF9FFF', hatch='///', edgecolor = 'black', capsize=7, label='Baseline')
ax1.bar(r2, mater_local_complex_small, width = barWidth, color = '#93C572', hatch='xx', edgecolor = 'black', capsize=7, label='Materialized')
ax1.bar(r3, non_mater_local_complex_small, width = barWidth, color = '#FFC000', hatch='++', edgecolor = 'black', capsize=7, label='Non Materialized')

# Medium Database
ax2.bar(r1, baseline_local_complex_medium, width = barWidth, color = '#CF9FFF',hatch='///', edgecolor = 'black', capsize=7, label='Baseline')
ax2.bar(r2, mater_local_complex_medium, width = barWidth, color = '#93C572', hatch='xx', edgecolor = 'black', capsize=7, label='Materialized')
ax2.bar(r3, non_mater_local_complex_medium, width = barWidth, color = '#FFC000', hatch='++', edgecolor = 'black', capsize=7, label='Non Materialized')

ax1.set_xlabel('Small Database')
ax2.set_xlabel('Medium Database')

ax1.set_yscale('log')
ax1.grid(True, which='both', ls='dotted', axis='y')

ax2.set_xticks(range(len(use_query_names)))
ax2.set_xticklabels(use_query_names, rotation=315)  # Rotate x-axis labels
ax2.set_yscale('log')  # Set y-axis to log scale
ax2.grid(True, which='both', ls='dotted', axis='y')

# Add legend and y-axis label
legend_labels = ['Baseline', 'Materialized', 'Non Materialized']
legend = ax1.legend(legend_labels, loc='upper center', ncol=3, bbox_to_anchor=(0.5, 1.25), frameon=False)

plt.tight_layout()  # Adjust layout to prevent overlapping labels

fig.text(0.001, 0.5, 'View Usage Time (ms)', va='center', rotation='vertical')

plt.savefig(f"test/evaluation_plots/paper/use_complex.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### Updated Global Plot ###############################

baseline_global_small =  baseline_global_df_small['Average_Baseline_Time'].tolist()
mater_global_small =  mater_global_df_small['Average_Materialized_Use_Time'].tolist()
non_mater_global_small =  non_mater_global_df_small['Average_Non_Materialized_USE_Time'].tolist()

baseline_global_medium =  baseline_global_df_medium['Average_Baseline_Time'].tolist()
mater_global_medium =  mater_global_df_medium['Average_Materialized_Use_Time'].tolist()
non_mater_global_medium =  non_mater_global_df_medium['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = mater_global_df_medium['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(mater_global_df_medium))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

fig, (ax1, ax2) = plt.subplots(2, 1, sharex=True)

# Small Database
ax1.bar(r1, baseline_global_small, width = barWidth, color = '#CF9FFF', hatch='///', edgecolor = 'black', capsize=7, label='Baseline')
ax1.bar(r2, mater_global_small, width = barWidth, color = '#93C572', hatch='xx', edgecolor = 'black', capsize=7, label='Materialized')
ax1.bar(r3, non_mater_global_small, width = barWidth, color = '#FFC000', hatch='++', edgecolor = 'black', capsize=7, label='Non Materialized')

# Medium Database
ax2.bar(r1, baseline_global_medium, width = barWidth, color = '#CF9FFF',hatch='///', edgecolor = 'black', capsize=7, label='Baseline')
ax2.bar(r2, mater_global_medium, width = barWidth, color = '#93C572', hatch='xx', edgecolor = 'black', capsize=7, label='Materialized')
ax2.bar(r3, non_mater_global_medium, width = barWidth, color = '#FFC000', hatch='++', edgecolor = 'black', capsize=7, label='Non Materialized')

ax1.set_xlabel('Small Database')
ax2.set_xlabel('Medium Database')

ax1.set_yscale('log')
ax1.grid(True, which='both', ls='dotted', axis='y')

ax2.set_xticks(range(len(use_query_names)))
ax2.set_xticklabels(use_query_names, rotation=315)  # Rotate x-axis labels
ax2.set_yscale('log')  # Set y-axis to log scale
ax2.grid(True, which='both', ls='dotted', axis='y')

# Add legend and y-axis label
legend_labels = ['Baseline', 'Materialized', 'Non Materialized']
legend = ax1.legend(legend_labels, loc='upper center', ncol=3, bbox_to_anchor=(0.5, 1.25), frameon=False)

plt.tight_layout()  # Adjust layout to prevent overlapping labels

fig.text(0.001, 0.5, 'View Usage Time (ms)', va='center', rotation='vertical')

plt.savefig(f"test/evaluation_plots/paper/use_global.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()