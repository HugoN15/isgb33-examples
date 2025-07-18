package se.kau.isgb33;

import java.io.FileInputStream;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Stub {

	public static void main(String[] args) {
		
		Logger logger = LoggerFactory.getLogger(Stub.class);
		
		JFrame f = new JFrame("FAIR BNB");
		f.setSize(400, 500);
		f.setLayout(null);
		
		JTextArea area = new JTextArea();
		area.setBounds(10, 10, 365, 400);
		area.setLineWrap(true);
		
		JTextField t = new JTextField("");
		t.setBounds(10, 415, 260, 40);
		
		JButton b = new JButton("SÖK!");
		b.setBounds(275, 415, 100, 40);
		
		b.addActionListener(new ActionListener() {
				
			public void actionPerformed(ActionEvent q) {
				
				logger.info("TJOOOOOHOPP!");
				
				try (InputStream input = new FileInputStream("connection.properties")) {

					Properties prop = new Properties();
					prop.load(input);
					String connString;
					connString = prop.getProperty("db.connection_string");
					logger.info(connString);

					ConnectionString connectionString = new ConnectionString(connString);
					MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
							.build();
					MongoClient mongoClient = MongoClients.create(settings);
					MongoDatabase database = mongoClient.getDatabase(prop.getProperty("db.name"));
					MongoCollection<Document> collection = database.getCollection("Movies");
					
					String tillb = t.getText();
					
					Bson filter = Filters.in("genres", tillb);
					
					AggregateIterable<Document> myDocs = collection.aggregate(Arrays.asList(
							Aggregates.project(
										Projections.include("title", "year", "genres")
									),
							Aggregates.match(filter),
							Aggregates.limit(10),
							Aggregates.sort(Sorts.descending("title"))
							
								
							));
					
					MongoCursor<Document> iterator = myDocs.iterator();
					
					area.setText("");
					
					if (!iterator.hasNext()) {
						area.setText("Ingen film matchade kategorin");
						
					} else {
					
					while(iterator.hasNext()) {
						

						Document myDoc = iterator.next();
						area.append("Filmens namn: " + myDoc.getString("title") + "\n");
						area.append("Filmens gjordes år: " +  myDoc.get("year") +"\n");
						
						List <String> list = (List <String>) myDoc.get("genres");
						List <String> tilbhlista = list;
						
						
						for(String til:tilbhlista) {
							area.append(til + ", ");
						}
						
						area.append("\n\n -----------------------------\n "); 
						
						
					}
					}
				
				
					
					
					
					
					
					
					//Document myDoc = collection.find(Filters.eq("address.country", country)).first();
					//logger.info(myDoc.toJson());
					//area.setText(myDoc.toJson());

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}}
		}
			
			
			);
		
		
		f.add(area);
		f.add(t);
		f.add(b);
		f.setVisible(true); } }
		
		
		
		
		
	
		
		
