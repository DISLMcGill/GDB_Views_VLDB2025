There are different types of VIEW USE exerimetns that we perform. 
Under the [baseline directory](./baseline/) you find the data from running the baseline queries of the USE commands (which is how a developer would write those queries).

Under the [materialized directory](./materialized/), there is the data from running the USE queries in the middleware with the materialized approach explained in the paper. And finally, under the   [non-materialized directory](./non_materialized/), there is the data from running the USE queries with a non-materilized approach (basically just reqriting the query with the definition of the view).

| USE Query | Views USED | Context | Query Body                               | Num_parameters in the query |
|-----------|------------|---------|------------------------------------------|-----------------------------|
| $V_{1}^1$      | $V_{1\_1}$          | Local   | WITH VIEWS V1_1 LOCAL MATCH (n) RETURN n | 1                           |
| $V_{1}^2$      | $V_{1\_2}$          | Local   | WITH VIEWS V1_2 LOCAL MATCH (n) RETURN n | 1 