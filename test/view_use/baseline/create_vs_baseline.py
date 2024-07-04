# Used for creating a plot comparing view creation time and baseline execution time
from matplotlib import pyplot as plt
from statistics import mean
import numpy as np
import pandas as pd
import sys

size = sys.argv[1]

baseline_dir_path = 'test/view_use/baseline'
create_dir_path = 'test/view_creation'

baseline_df = pd.read_csv(f'{baseline_dir_path}/baseline_basic_local_{size}.csv')
#print(baseline_df)

create_df = pd.read_csv(f'{create_dir_path}/view_creation_time_{size}.csv')
#print(create_df)

plt.plot(baseline_df['View_Name'], baseline_df['Average_View_Baseline_Time'], color='c', label='Avg Baseline Time')
#plt.plot(baseline_df['View_Name'], baseline_df['Average_Cold_Baseline_Time'], color='b', label='Avg Cold Baseline Time')
plt.plot(create_df['View_Name'], create_df['Average_View_Creation_Time'], color='m', label='Avg Creation Time')
#plt.plot(create_df['View_Name'], create_df['Average_Cold_Creation_Time'], color='g', label='Avg Cold Creation Time')
plt.xlabel("View Name")
plt.ylabel("Execution Time(ms)")
plt.xticks(rotation=315)
plt.legend()
plt.savefig(f"test/evaluation_plots/baseline_vs_create_{size}_warm.pdf", format="pdf", bbox_inches="tight")

plt.close()

plt.plot(baseline_df['View_Name'], baseline_df['Average_Cold_Baseline_Time'], color='c', label='Avg Cold Baseline Time')
plt.plot(create_df['View_Name'], create_df['Average_Cold_Creation_Time'], color='m', label='Avg Cold Creation Time')
plt.xlabel("View Name")
plt.ylabel("Execution Time(ms)")
plt.xticks(rotation=315)
plt.legend()
plt.savefig(f"test/evaluation_plots/baseline_vs_create_{size}_cold.pdf", format="pdf", bbox_inches="tight")