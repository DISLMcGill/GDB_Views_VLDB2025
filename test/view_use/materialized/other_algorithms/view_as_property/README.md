The create experiment for this method as below: 

1. Assuming that all the views already exist in the middleware, for a random order of local basic materialized use queries, each time a single use query will run in the middleware. Before running the query a warm-up baseline command will be run and then the query will be given as input and finally we  shutdown the middleware. So each time, we only measure the execution time of only one use query given that the system has been warmed up.
2. This process will be repeated five times and the average execution time will be reported.