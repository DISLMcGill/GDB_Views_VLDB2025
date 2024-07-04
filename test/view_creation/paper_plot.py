import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import sys

# Parameteres to set before running the code
# This should be consistent with the number of views used in the experiment
dir_path = 'test/view_creation'

# This should change to include the right name for the views and their variants
labels = ['V1_1', 'V1_2', 'V2_1', 'V2_2', 'V3_1', 'V3_2', 'V4_1', 'V4_2', 'V5', 'V6_1', 'V6_2', 'V7_1', 'V7_2']

# These are all the new data experimented
create_time_df_small = pd.read_csv(f'{dir_path}/paper_creation_time_small.csv')
create_time_df_medium = pd.read_csv(f'{dir_path}/paper_creation_time_medium.csv')
# create_time_method2_df_small = pd.read_csv(f'{dir_path}/other_algorithms/view_as_property/paper_create_small.csv')
# create_time_method2_df_medium = pd.read_csv(f'{dir_path}/other_algorithms/view_as_property/paper_create_medium.csv')

view_create_cold_time_table_small = create_time_df_small['Average_View_Creation_Time'].tolist()[:13]
view_create_cold_time_table_medium = create_time_df_medium['Average_View_Creation_Time'].tolist()[:13]
# view_create_cold_time_method2_table_small = create_time_method2_df_small['Average_View_Creation_Time'].tolist()[:13]
# view_create_cold_time_method2_table_medium = create_time_method2_df_medium['Average_View_Creation_Time'].tolist()[:13]

barWidth = 0.3
r1 = np.arange(len(create_time_df_small))
r2 = [x + barWidth for x in r1]

# Plot data on ax1
plt.bar(r1, view_create_cold_time_table_small, width = barWidth, color = 'blue', edgecolor = 'black', capsize=7, label='Small Database')
plt.bar(r2, view_create_cold_time_table_medium, width = barWidth, color = 'orange', edgecolor = 'black', capsize=7, label='Medium Database')
ax = plt.gca()
ax.set_yscale('log')
plt.grid(True, which='both', ls='dotted', axis='y')

plt.xlabel("View Name")
plt.ylabel("View Creation Time (ms)")
plt.xticks([r + barWidth for r in range(len(view_create_cold_time_table_small))], labels)
plt.xticks(rotation=315)

# Add legend and y-axis label
legend_labels = ['Small Database', 'Medium Database']
legend = plt.legend(legend_labels, loc='upper center', ncol=2, bbox_to_anchor=(0.5, 1.1), frameon=False)

plt.tight_layout()  # Adjust layout to prevent overlapping labels

# Display the plot
plt.savefig(f"test/evaluation_plots/paper/create.pdf", format="pdf", bbox_inches="tight", dpi=150)
