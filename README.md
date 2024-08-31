# COMP400

## Compilation & Run
First `cd COMP400_code/src`, then:
To compile
- Linux: `javac -cp "../lib2/*:.." ./**/*.java`
- Windows: `javac -cp "../lib2/*;.." ./**/*.java`

To run:
- Linux: `java -cp "../lib2/*:."  -Djava.io.tmpdir=<local tmp dir> <file>` 
  - e.g. `java -cp "../lib2/*:."  -Djava.io.tmpdir=/home/2020/csacre/tmp main.MainKuzu` for Middleware
  - e.g. `java -cp "../lib2/*:."  -Djava.io.tmpdir=/home/2020/csacre/tmp kuzu.Main cli` for simple Kuzu cli
- Windows: `java -cp "../lib2/*;."  -Djava.io.tmpdir=<local tmp dir> <file>`
Note: specifying tmpdir is only necessary on mimi/lab machines 

## Key changes
- ID(n) in list => list_contains(list, string(ID(n)))
- Cannot use DISTINCT <path> as Kuzu does not support comparison of "STRUCT" with the type of a path
- list\[index] => list_element(list, index)

## Simple Kuzu interface (not middleware)
If need to create the database, add "create" argument
```
java -cp "../lib2/*:."  -Djava.io.tmpdir=/home/2020/csacre/tmp kuzu.Main create
```

Otherwise 
```
java -cp "../lib2/*:."  -Djava.io.tmpdir=/home/2020/csacre/tmp kuzu.Main cli
```

Then, execute Cypher queries in the CLI. Write `quit` to exit the CLI.


## Notes:
- When using a new database, need to 
  - Make a `KuzuDatabase db = new KuzuDatabase(db_path)` object where `db_path` is a string path to the db. If no
    db exists with this path, one is created automatically. If it does exist, loads the existing database (doesn't erase the old one).
  - First create the schema using CREATE TABLE NODE and CREATE TABLE REL statements
    - The definition should include all properties/columns with name and field
    - Node table need a primary key
  - Import the data using COPY FROM statements - can only use COPY FROM once for a table I believe
    - If there's a problem with some data in the file, nothing gets imported
- In the middleware code (MainKuzu), the most relevant functions (starting points) are:
  - View creation: processMainView() (and pathQuery() for path view creation)
  - View usage: processUseView() 

## Baseline query translation
- Any `n in <list>` should be translated to `list_contains(<list>, n)`
- To concatenate 2 lists, use `list_concat` instead of `+`
- Use `NODES(p)` to extract nodes of a path `p`, and `RELS(p)` for edges
- For any query that matches on an exact ID e.g. `ID(n) = 123`, the ID format should follow the Kuzu format of `tableId:offset` e.g. `0:0`
- Sometimes `COLLECT(...)` creates a list that is too large, which causes memory issues and the program to crash (either 
  fail to claim a buffer frame or string size too big) 
- Cannot use `DISTINCT` for paths 
- Subqueries and carrying results with `WITH` can be quite limited, so even queries that look fine syntactically might have to be rewritten

