package main;

import java.util.concurrent.TimeUnit;
import org.apache.age.jdbc.base.Agtype;
import org.postgresql.jdbc.PgConnection;

import java.sql.*;
import java.util.HashMap;

//the set of classes to use for comparing with sigmod paper
import static main.sigmodNodeSetBaseline.*;
import static main.sigmodPropertyBasedNodeSetNodeView.*;
//import static main.sigmodPropertyBasedSubgraphSetNodeView.*;

// the set of class to use for postgres standalone tests
//import static main.NodeSetBaseline.*;
//import static main.PathSetBaseline_copy.*;
//import static main.propertyBasedNodeSetNodeView.*;
//import static main.IDbasedNodeSetNodeView.*;
//import static main.propertyBasedPathSetPathView_copy.*;
//import static main.IDbasedPathSetPathView_copy.*;

public class PostgresqlConnector {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/amelia";
    static final String USER = "amelia";
    static final String PASS = "amelia";

    static HashMap<String,HashMap<String, String>> nodeTable = new HashMap<>();

    public static void main(String[] args) {

        // Open a connection
        try {

            PgConnection connection = DriverManager.getConnection(DB_URL,USER,PASS).unwrap(PgConnection.class);
            connection.addDataType("agtype", Agtype.class);

            // configure AGE
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE EXTENSION IF NOT EXISTS age;");
	    stmt.execute("GRANT USAGE ON SCHEMA ag_catalog TO amelia;");
            stmt.execute("LOAD 'age'");
            stmt.execute("SET search_path = ag_catalog, \"$user\", public;");


            long start = System.currentTimeMillis();
            //VCL_L_declare(stmt);
	    VCL_L_baseline(stmt);
            long end  = System.currentTimeMillis();
            System.out.println("Declaration time:" + (end-start));

            start = System.currentTimeMillis();
            //VCL_L(stmt);
            end  = System.currentTimeMillis();
            System.out.println("Usage time:" + (end-start));

 	    cleanup(stmt);
            //cleanup(stmt,"VSL");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
