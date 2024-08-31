# GDBViews
This repository contains the source code for the paper "G-View: View Management for Graph Databases". In this paper, we present new language extensions that integrate views for current graph databases, and suggest implementation strategies featuring view usage and maintenance.
For questions, please reach out to Yunjia Zheng(yunjia.zheng@mail.mcgill.ca).

## Instructions for running with Neo4j:
#### Requirements
* Java (tested with v.19.0.2)
* Neo4j (tested with v.5.2.0)
#### To run from command line
We used the Neo4j API to implement the view management system. Simply compile and run using:
```
javac -sourcepath src -cp ".:lib2/apoc-5.3.0-core.jar:./lib2/*" src/main/Main.java
java -cp "lib2/apoc-5.3.0-core.jar:lib2/*:src/" main.Main ./test/temporary.txt
```
where the 'temporary.txt' is the source file containing the test queries, either being view creation, view usage or a query directly feeding to the database(start with 'original').

In addition, we also provide connection using the driver, mainly facillitating the periodic commit function from the APOC library with:
```
javac -sourcepath src -cp ".:lib2/apoc-5.3.0-core.jar:./lib2/*" src/main/Neo4jDriverConnector.java
java -cp "lib2/apoc-5.3.0-core.jar:lib2/*:src/" main.Neo4jDriverConnector ./test/temporary.txt
```
To further enrich the grammar of our extended language, update View.g4, execute the following command and copy the newly generated files under src/gen:
```
java -jar lib2/antlr4-4.8-1-complete.jar src/View.g4
```
#### To run with an IDE
It is also supported to run from the terminal within an IDE. Queries are entered to the terminal directly, in the same form as from the test files.

## View declaration and usage tests
We developed tests for both LDBC and StackOverFlow graphs with various view types, which can be found under the test/ folder, categorized by the specific type of creation/usage.
The layout is as follows:
* experimentScripts: scripts that helps with setting up the database (importLDBCtoNeo4j.txt), randomized queries (random_use_query_gen.py) and processing the result.
* LDBC: queries for LDBC graph, universal queries(universal_queries/), seeded queries adapted from LDBC benchmark(BI_IC/) and example queries that anchors the size and complexity demonstrated in the paper(example_queries/).
* stackOverFlow: queries used for stackOverFlow graph.
