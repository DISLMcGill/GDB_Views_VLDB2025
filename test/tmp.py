import pandas as pd 

df = pd.read_csv('test/view_use/non_materialized/non_materialized_use_local_basic_small.csv')
print ((df['Average_Non_Materialized_Cold_View_USE_Time']).to_string(index=False))