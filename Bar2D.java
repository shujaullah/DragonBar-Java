// File: Bar2D.java
//Name: Shujaullah Ahsan.
// Topic 8: GUIs
// Course: CSI237 (Spring 2019)
// Date: April 25, 2019

import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javafx.geometry.*;

/**
 * Draws an egg with 0-3 cracks.
 *
 * @author YOUR NAME
 */
public class Bar2D extends Canvas
{
	/*
	 * 
	 */
		
	/*
	 * Set up egg with no initial cracks.
	 *
	 * @param width width of panel
	 * @param height height of panel
	 */
	public Bar2D(double width, double height)
	{
		super(width, height);
	
	}
	
	// draw function which will draw the 2D bar for the adventurer coins and vitality.
	public void draw(double widthF, double heightF)
	{
		
		GraphicsContext gc = getGraphicsContext2D();
		//Total width and height of the canvas
		double width = getWidth();
		double height = getHeight();
		
		//To create the background

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
		
		// creating the bar which will work on the fraction for teh coins
		//vitality of the adventurer
		gc.setFill(Color.CYAN);
		//fraction width and the fraction height based on the 
		// product of the actual canvas height to th efraction of the height
		// similarly for the fraction of the width.
		double fractionWidth =widthF * width;
		double fractionHeight = heightF * height;
				
		//making the rectangle acording to the fraction width and the fraction height.
		// ? : operator for the zero pixels of vitlality in the begining of the program so it will still
		// draw some color pixel bar.
		gc.fillRect(0, 0, fractionWidth==0 ? 3 :fractionWidth , fractionHeight==0 ? 3:fractionHeight);
		
		//double barPos = height-(fractionHeight==0 ? 4:fractionHeight );
		//gc.fillRect(0, barPos, fractionWidth==0 ? 4:fractionWidth , fractionHeight==0 ? 4:fractionHeight );
		
	}
	
}
