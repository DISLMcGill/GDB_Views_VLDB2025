The create experiment for this method as below: 

1. Given that the middleware initially doesn't have any views, we start the system and warm it up by running a couple of baseline queries loading node indexes into the main memory. 
2. After that, we create all the views in a randomized order and append the outputs to the final log file. 
3. This process is repeated for five times and we report the average creatoin times.