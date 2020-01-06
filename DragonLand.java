/*
 * File: DragonLand.java
 * Name: Shujaullah Ahsan
 * Project 2: DragonBar
 * Course: CSI237 (Spring 2019)
 * Date: May 3, 2019
 * Description: Live the life of an adventurer in Dragon Land.
 *              Encapsulate Adventurer and Monster in classes.
 */
 import java.util.*;  // for Scanner
import dragoncorp.*;
import java.io.*;    // for FileReader and FileNotFoundException
import java.util.Optional;

//import com.sun.prism.paint.Color;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.scene.paint.*;
import java.lang.reflect.*;


public class DragonLand extends Application 
{
	public final static int COINS_TO_WIN = 30;  // # coins to win game
	// store existing constants
	public static final int FOOD_COST = 5;      // food cost in coins
	public static final int MIN_WAGE = 7;       // minimum wage in coins
	public static final double WORK_ENERGY = 0.3;  // energy needed to work a job
    
	// Create default challengers.
	public static Villain[] challengers = {
		new Dragon(1.0),
		new Orc(0.3),
	 	new Orc(0.3),
		new Thief("Jessibelle", 0.7),
	 	new Thief("Rodney", 0.3),
		null
	};
		
	// Challenger chosen by rolling a die.
	public static Die die = new Die(challengers.length);
	
	// Create default adventurer.
	public static Adventurer adventurer = new Adventurer();

	public static Scanner console = new Scanner(System.in);
	int counter =1;
	// opening the output binary file
	DataOutputStream outFile;
	

	// label for the vitality and coins
	private Label vitalityLabel, coinsLabel;
	// labels for the action and the confrontation.
	private Label actionLabel;
	private static Label confrontLabel;
	//private TextField action;
	public static Bar2D bar;

	// button for the actions of the games.
	private Button workButton, eatButton, napButton, quitButton;


	// Run an adventure, role-playing type game.
	public static void main(String[] args) 
	{
		launch(args);
		
		
		
	}
	
	@Override
	public void start(Stage mainStage) throws FileNotFoundException
	{
		// adding the dialog box to get the input for the name of the adventurer.
		// setting the title of the box to dragonland
		// setting the header to adventurer and the content of the box to enter name.
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Dragonland");
		dialog.setHeaderText("Adventurer");
		dialog.setContentText("Please enter your name:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
		{
			// giving the adventurer name by getting the name from the dialog box.
		    adventurer = new Adventurer(result.get());
		}
		else
			System.exit(0);

		// The Java 8 way to get the response value (with lambda expression).
		//result.ifPresent(name -> System.out.println("Your name: " + name));
		
		// Create layout using a column, row grid.
		GridPane pane = new GridPane();

		// Add components to layout.
		addComponents(pane);
		
		// Set initial values for fields.
		//setFieldData();

		// Create scene that uses layout and set for window.
		//Scene scene = new Scene(pane);
		Scene scene = new Scene(pane, 280,280);

		mainStage.setScene(scene);

		// Set text in titlebar.
		mainStage.setTitle("DragonLand " + result.get());
		
		//opening the output file for the binary
		outFile = new DataOutputStream(new FileOutputStream("actions.log"));
		// Make window visible.
		mainStage.show();
	}
	
	public void addComponents(GridPane pane)
	{
		// initializing the lables
		vitalityLabel = new Label("Vitality");
		coinsLabel = new Label("Coins");
		actionLabel = new Label();
		confrontLabel = new Label();
		//initializing the bar for the coins and the 
		bar = new Bar2D(200,200);
		
		
		// adding teh coins and vitality label with proper alignment.
		pane.add(vitalityLabel, 0, 2);
		GridPane.setValignment(vitalityLabel, VPos.CENTER);
		vitalityLabel.setRotate(-90.0);
		pane.add(coinsLabel, 0, 0,4,1);
		GridPane.setHalignment(coinsLabel, HPos.CENTER);
		
		// initializing the buttons for the action of adventurer
		// set there actions.
		
		workButton = new Button("Work");
		workButton.setOnAction(new WorkHandler());
		
		eatButton = new Button("Eat");
		eatButton.setOnAction(new EatHandler());
		
		napButton = new Button("Nap");
		napButton.setOnAction(new NapHandler());
		
		quitButton = new Button("Quit");
		quitButton.setOnAction(new QuitHandler());
		
		// adding the bar 
		pane.add(bar, 1, 2, 3,1);
		// calling the method for the drawing of bar.
		drawBar();
		// adding them at the same time with proper position
		pane.add(workButton, 0, 15,1,1);
		pane.add(eatButton, 1, 15,1,1);
		pane.add(napButton, 2, 15,1,1);
		pane.add(quitButton, 3, 15,1,1);
		
		
		// setting the width of the action and confront label
		//with max and the min width.
		actionLabel.setMinWidth(300);
		confrontLabel.setMinWidth(300);
		actionLabel.setMaxWidth(300);
		confrontLabel.setMaxWidth(300);
		
		// changing the label background color of 
		// action and confront
		actionLabel.setBackground(new Background(new BackgroundFill(Color.WHITE , CornerRadii.EMPTY, Insets.EMPTY)));
		confrontLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		// adding the action and the confront label
		pane.add(actionLabel,0,20,10,1);
		pane.add(confrontLabel,0,21,10,1);
		

		
	}
	// method for the darwing the bar aordingly.

	public void drawBar()
	{
		bar.draw(adventurer.getCoins()/30.0,adventurer.getVitality()/adventurer.MAX_VITALITY);
	}
	
	// Allow adventurer to choose an action, then display the results
	// of that action, which may change the adventurer's status.
	public static void chooseAction()
	{
		final int FOOD_COST = 5;      // food cost in coins
		final int MIN_WAGE = 7;       // minimum wage in coins
		final double WORK_ENERGY = 0.3;  // energy needed to work a job

		System.out.println();
		System.out.println("W)ork at stables");
		System.out.println("E)at at tavern");
		System.out.println("T)ake a nap");
		System.out.println("G)ive up");
		System.out.print("Action? ");
		
		// Read string from user, convert to uppercase,
		// and extract first character.
		char action = console.next().toUpperCase().charAt(0);
		console.nextLine();  // eat newline
		
		switch (action)
		{
		case 'W':
			if (adventurer.work(WORK_ENERGY, MIN_WAGE))
				System.out.println("Stables have been mucked out.");
			else
				System.out.println("You are too weak to muck out stables!");
			break;
			
		case 'E':
			if (adventurer.buyFood(FOOD_COST))
				System.out.println("You had a palatable meal.");		
			else
				System.out.printf("Need %d coins for food!%n", FOOD_COST);		
			break;
				
		case 'T':
			adventurer.sleep();
			System.out.println("You had a nice nap.");
			break;
						
		case 'G':
			System.exit(0);
			break;
			
		default:
			System.out.printf("I do not understandeth \"%c\"%n", action);
			break;
		}
	}
   
	// Determine whether a confrontation occurs with a monster.
	// If so, change adventurer's status to reflect any damage
	// caused.
	public static void confrontation()
	{
 		Villain confronter = challengers[die.roll()-1];

		if (confronter == null)
		{
			System.out.println("No one around.");
			return;
		}

		System.out.printf("You come across %s%n", confronter);
		
		Adventurer backupAdv = new Adventurer(adventurer);
         
		// Determine whether monster or thief attacked.
		// If so, compute damage caused.
		// setting the text for the label of the confronter.
		if (confronter.confront(adventurer))
		{
			double damage =
				backupAdv.getVitality() -
 				adventurer.getVitality();
            
		 	if (damage > 0.0)
		 		confrontLabel.setText(String.format("%s caused you %.2f damage!%n",
   					confronter.getIdent(), damage));
   		
		 	int coinDiff = backupAdv.getCoins()
   		               - adventurer.getCoins();
	   		
		 	if (coinDiff > 0)
		 		confrontLabel.setText(String.format("%s stole %d coin(s)!%n",
	   				confronter.getIdent(), coinDiff));
			}
			else
			{
				confrontLabel.setText(String.format("%s left you alone%n",
					confronter.getIdent()));
			}
	}
	
	// the method check for the vitality of the adventurer and run the game 
	// also  the  wining coins if the coins is greater or equal for required coins to win
	// will pop up the message of winning.
	private void survival() throws IOException
	{
		if (adventurer.getVitality() > 0.0)
		{
			// Determine if won by number of coins acquired.
			if (adventurer.getCoins() >= COINS_TO_WIN)
			{	
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("You Won");
				alert.setContentText("congractlations");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK )
				{
					
					System.exit(0);
				}
			}
			// doing confrontation
			
		  confrontation();
		}
		// giving the dialog box of losing the game.
		else 
		{
		
		//actionLabel.setText("You are dead (too weak to fend off mouse)!");
		
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("DragonnLand "+ adventurer.getName());
			alert.setHeaderText("Game Over");
			alert.setContentText("You are dead (too weak to fend off mouse)!");
			//alert.showAndWait();
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK )
			{
				
				System.exit(0);
			}
		}
	}
	
	// the method for the writing in the binary file according to the button number
	// it will take an int and write it in to the binary file.

	public void addToLog(int val) throws IOException
	{
		outFile.writeInt(counter);
		outFile.writeInt(val);
		if(val ==4 )
		{
			
			outFile.close();
		}
		counter++;
	}
	// event handler for the work button 
	private class WorkHandler
	implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e) //throws FileNotFoundException
		{
			// checking the condition for the adventurer work
			// setting text for the action label
			// calling the draw method for the bar.
			if (adventurer.work(WORK_ENERGY, MIN_WAGE))
			{
				actionLabel.setText("Stables have been mucked out.");
				drawBar();
				
	
			}
			else
				actionLabel.setText("You are too weak to muck out stables!");
			
			// go confront after working
			// try catch block for the binary file 
			// calling the method of survival in there.
			try 
			{
				addToLog(1);
				survival();
			} 
			catch (IOException ex)
			{
				System.out.println("unable to write in the file " +
										"There is an error " + ex.toString());
				
				
			}
			
			//survival();
	
		}
	}
	
	// event handler for the eat button
	private class EatHandler
	implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent e)
		{
			// checking the condition for the adventurer buying food
			// setting text for the action label
			// calling the draw method for the bar.
			if (adventurer.buyFood(FOOD_COST))
			{
				actionLabel.setText("You had a palatable meal.");
				drawBar();
				
			}
			else
				actionLabel.setText(String.format("Need %d coins for food!", FOOD_COST));
			// go confront after working
		    // try catch block for the binary file 
			// calling the method of survival in there.
			try 
			{
				addToLog(2);
				survival();
			} 
			catch (IOException ex)
			{
				System.out.println("unable to write in the file " +
						"There is an error " + ex.toString());
				
				
			}
			
			// go confront after eating
			//survival();
	
	
		}
	}
	// event handler for the sleep button
	private class NapHandler
	implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event)
		{
			//calling sleep method for an adventurer
			// setting text for the action label
			// calling the draw method for the bar.
			adventurer.sleep();
			//bar.draw(adventurer.getCoins()/30.0,adventurer.getVitality()/adventurer.MAX_VITALITY);
			
			actionLabel.setText("You had a nice nap.");
			drawBar();
			
			// go confront after working
			// try catch block for the binary file 
			// calling the method of survival in there.
			try 
			{
				addToLog(3);
				survival();
			} 
			catch (IOException ex)
			{
				System.out.println("unable to write in the file " +
						"There is an error " + ex.toString());
				
				
			}
			//survival();
		}		
	}	
	
	// event handler for the quit button.
	private class QuitHandler
	implements EventHandler<ActionEvent>
	{
		@Override
		public void handle(ActionEvent event)
		{
			// go confront after working
			// try catch block for the binary file 
			// calling the method of survival in there.
			try 
			{
				addToLog(1);
			} 
			catch (IOException ex)
			{
				System.out.println("unable to write in the file " +
						"There is an error " + ex.toString());
				
				
			}
			System.exit(0);
		}		
	}	
	
	

	
}




