import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


public class Card extends JLabel{
	
	private static final long serialVersionUID = -3842368598551207535L;
	
	private ImageIcon backFaceIcon;
	private ImageIcon frontFaceIcon;
	
	private int number = 52;
	private int value = 0;
	private String name = "Joker";
	private String suit = "Joker";
	
	// Constructor (Cards default to a joker)
	public Card() {
	
		try {
			backFaceIcon = new ImageIcon(ImageIO.read(new File("cards/Backface_Red.jpg")));
		} catch (IOException e) {
			try {
				backFaceIcon = new ImageIcon(ImageIO.read(new File("cards\\Backface_Blue.jpg")));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		try {
			frontFaceIcon = new ImageIcon(ImageIO.read(new File("cards/Joker_Black.jpg")));
		} catch (IOException e) {
			try {
				frontFaceIcon = new ImageIcon(ImageIO.read(new File("cards\\Joker_Red.jpg")));
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		
		updateIcon(true);
	}
	
	/**
	 * This method updates the icon to either face up or face down
	 * @param displayFront
	 */
	public void updateIcon(boolean displayFront) {
		if (displayFront) {
			setIcon(frontFaceIcon);
		} else {
			setIcon(backFaceIcon);
		}
	}
	
	// To string
	@Override
	public String toString() {
		return "number: " + number + " | value: " + value + " | suit: " + suit;
	}
	
	// Getters and setters
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ImageIcon getBackFaceIcon() {
		return backFaceIcon;
	}

	public void setBackFaceIcon(ImageIcon backFaceIcon) {
		this.backFaceIcon = backFaceIcon;
	}

	public ImageIcon getFrontFaceIcon() {
		return frontFaceIcon;
	}

	public void setFrontFaceIcon(ImageIcon frontFaceIcon) {
		this.frontFaceIcon = frontFaceIcon;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	

	
	
	
}
