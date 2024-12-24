# GDBViews



Code for implementing views in PostgreSQL on top of the Apache AGE extension, along with some instructions and notes. Below are some useful class information:
  
PostgresqlConnector:
The main program, which runs for view declaration/usage/baseline.

The other classes are arranged based on the name: whether it is ID-based or property-based, for the node set views or path set views and also the baselines.



# Commands for running:

javac -sourcepath src -cp "lib2/*:src/"  src/main/PostgresqlConnector.java

java -cp "lib2/*:src/" main.PostgresqlConnector
