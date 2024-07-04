package main;
import java.io.*;
import java.util.*;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;

public class Neo4jDriverConnector {

    public Driver driver;

    // Constructor
    public Neo4jDriverConnector(String uri) {
        // Create a new driver instance
        this.driver = GraphDatabase.driver(uri);
    }

    // Method to close the driver connection
    public void close() {
        driver.close();
    }

    // Method to run a query that fetches all nodes with the label 'Person'
    public void executeDirectly(String query,Session session) {
	long start = System.currentTimeMillis();
        Result result = session.run(query);
        int i = 0;
        while (result.hasNext()) {
            Record record = result.next();
	    //System.out.println(record);
            i++;
        }
        System.out.println("number of elements: " + i);
	long duration = System.currentTimeMillis() - start;
        System.out.println("Took " + duration + " ms to execute query");
        
    }

    // Main method to run the example
    public static void main(String[] args) {
        Neo4jDriverConnector driver= new Neo4jDriverConnector("bolt://localhost:7687");
	String fileName = args[0];
        ArrayList<String> commands_in_order = getExperimentCommands(fileName);
	try (Session session = driver.driver.session()) {
	    for (String cmd: commands_in_order) {
                System.out.println(cmd);
                driver.executeDirectly(cmd,session);
            }
	}
        driver.close();
    }

    public static ArrayList<String> getExperimentCommands(String filePath){
        ArrayList<String> commands = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(new File(filePath));
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                commands.add(line);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return commands;
    }
}
