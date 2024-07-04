from matplotlib import pyplot as plt
import sys
import numpy as np
import pandas as pd 

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

#################### Local Basic Plot ###############################

baseline_local_basic =  baseline_local_basic_df['Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_local_basic =  mater_local_basic_df['Average_Materialized_View_Use_Time'].tolist()
non_mater_local_basic =  non_mater_local_basic_df['Average_Non_Materialized_View_USE_Time'].tolist()

use_query_names = baseline_local_basic_df['View_Name'].tolist()
view_size = len(use_query_names)

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_local_basic[:int(view_size/2)]))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

plt.bar(r1, baseline_local_basic[:int(view_size/2)], width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')

plt.bar(r2, mater_local_basic[:int(view_size/2)], width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_basic[:int(view_size/2)], width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_basic[:int(view_size/2)]))], use_query_names[:int(view_size/2)])
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/local_basic_{size}_1.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

r1 = np.arange(len(baseline_local_basic[int(view_size/2):]))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]

plt.bar(r1, baseline_local_basic[int(view_size/2):], width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')

plt.bar(r2, mater_local_basic[int(view_size/2):], width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_basic[int(view_size/2):], width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_basic[int(view_size/2):]))], use_query_names[int(view_size/2):])
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/local_basic_{size}_2.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### Local Complex Plot ###############################

baseline_local_complex =  baseline_local_complex_df['Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_local_complex =  mater_local_complex_df['Average_Materialized_Use_Time'].tolist()
non_mater_local_complex =  non_mater_local_complex_df['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = baseline_local_complex_df['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_local_complex))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]
 
plt.bar(r1, baseline_local_complex, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')
 
plt.bar(r2, mater_local_complex, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_complex, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_complex))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/local_complex_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### New Local Complex Plot ###############################

# loading new data
baseline_local_complex_df = pd.read_csv(f'{baseline_dir_path}/baseline_local_complex_new_{size}.csv')
mater_local_complex_df = pd.read_csv(f'{mater_dir_path}/materialized_use_local_complex_new_{size}.csv')
non_mater_local_complex_df = pd.read_csv(f'{non_mater_dir_path}/non_materialized_use_local_complex_new_{size}.csv')

baseline_local_complex =  baseline_local_complex_df['Average_Baseline_Time'].tolist()
mater_local_complex =  mater_local_complex_df['Average_Materialized_Use_Time'].tolist()
non_mater_local_complex =  non_mater_local_complex_df['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = baseline_local_complex_df['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_local_complex))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]
 
plt.bar(r1, baseline_local_complex, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')
 
plt.bar(r2, mater_local_complex, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_local_complex, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_local_complex))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/local_complex_new_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### Global Plot ###############################
baseline_global =  baseline_global_df['Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_global =  mater_global_df['Average_Materialized_Use_Time'].tolist()
non_mater_global =  non_mater_global_df['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = baseline_global_df['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_global))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]
 
plt.bar(r1, baseline_global, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')
 
plt.bar(r2, mater_global, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_global, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_global))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
#ax = plt.gca()
#ax.set_yscale('log')
#plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='both')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/global_no_log_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### New Global Plot ###############################

baseline_global_df_new = pd.read_csv(f'{baseline_dir_path}/baseline_global_new_{size}.csv')
mater_global_df_new = pd.read_csv(f'{mater_dir_path}/materialized_use_global_new_{size}.csv')
non_mater_global_df_new = pd.read_csv(f'{non_mater_dir_path}/non_materialized_use_global_new_{size}.csv')

baseline_global =  baseline_global_df_new['Average_Baseline_Time'].tolist()
mater_global =  mater_global_df_new['Average_Materialized_Use_Time'].tolist()
non_mater_global =  non_mater_global_df_new['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = baseline_global_df_new['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_global))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]
 
plt.bar(r1, baseline_global, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')
 
plt.bar(r2, mater_global, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_global, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_global_df_new))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
#ax = plt.gca()
#ax.set_yscale('log')
#plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='both')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/global_new_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

#################### Thesis Plot ###############################


baseline_thesis =  baseline_thesis_df['Average_Cold_Baseline_Time_With_Init_Load'].tolist()
mater_thesis =  mater_thesis_df['Average_Materialized_Use_Time'].tolist()
non_mater_thesis =  non_mater_thesis_df['Average_Non_Materialized_USE_Time'].tolist()

use_query_names = baseline_thesis_df['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_thesis))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]
 
plt.bar(r1, baseline_thesis, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')
 
plt.bar(r2, mater_thesis, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_thesis, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_thesis))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/thesis_{size}.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()

# Plotting the original thesis data [in the tables]

# Order should be: ['V4', 'V5', 'V6', 'V7', 'V8', 'V15', 'V16', 'V17', 'V18', 'V19', 'V20', 'L1_8', 'L18_19', 'G5', 'G15', 'G20']

baseline_thesis =  [2409, 163, 44786, 203660, 205370, 106144, 0, 6679643, 1684268, 95846, 9540, 1284654, 659781, 1113844, 59894, 215]
mater_thesis =  [16392, 4061, 52059, 112674, 8388, 84105, 40946621, 36569, 27281, 17, 13, 3270,1958, 1093283, 1213140, 49972]
non_mater_thesis =  [16392, 227, 47390, 219116, 205039, 103167, 0, 0,195381, 11811, 9121,186325, 34335, 1108458, 20323, 24006]

use_query_names = baseline_thesis_df['Use_Query'].tolist()

# The x position of bars
barWidth = 0.25
r1 = np.arange(len(baseline_thesis))
r2 = [x + barWidth for x in r1]
r3 = [x + barWidth for x in r2]
 
plt.bar(r1, baseline_thesis, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Baseline')
 
plt.bar(r2, mater_thesis, width = barWidth, color = 'green', edgecolor = 'black', capsize=7, label='Materialized')

plt.bar(r3, non_mater_thesis, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Non Materialized')

plt.xlabel("USE Query")
plt.ylabel("Average Processing Time (ms)")
plt.xticks([r + barWidth for r in range(len(baseline_thesis))], use_query_names)
plt.xticks(rotation=315)
plt.legend(bbox_to_anchor = (1.4, 0.9), loc='center right')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')
#plt.tick_params(axis='y', which='minor')
#ax.yaxis.set_minor_formatter(FormatStrFormatter("%0.1f"))
plt.savefig(f"test/evaluation_plots/use_comparison/thesis_original.pdf", format="pdf", bbox_inches="tight", dpi=150)

plt.close()