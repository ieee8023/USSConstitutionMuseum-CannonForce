package com.ussc.serialreader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class FullScreenJFrame extends JFrame
{
	public FullScreenJFrame( String title )
	{
		super(title);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0,0,screenSize.width, screenSize.height);
      
		/*
		 * Set the background of the JFrame      
		 */
			try {
				File inputFile = new File("images/background.jpg");
				BufferedImage myImage = ImageIO.read(inputFile);
				this.setContentPane(new ImagePanel(myImage));
			} catch (IOException e) {
				System.out.println(e);
			}
		
		/*
		 * Hide the mouse cursor.      
		 */
			// Transparent 16 x 16 pixel cursor image.
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	
			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			cursorImg, new Point(0, 0), "blank cursor");
	
	      	// Set the blank cursor to the JFrame.
	      	this.getContentPane().setCursor(blankCursor);
	
	}
}