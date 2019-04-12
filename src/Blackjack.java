/*	blackjack
 * 	
 * 	Written by Jared Caruso
 * 	11/16/2018
 * 	IST 271
 * 
 * 	A simple blackjack game
 * 
 */

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import java.awt.FlowLayout;
import javax.swing.JCheckBoxMenuItem;

public class Blackjack extends JFrame {


	private static final long serialVersionUID = 19L;

	private JPanel CardDealerPane;
	
	private Card[] cards = new Card[52]; // cards
	
	private Random random = new Random();
	private int dealtCard = 0; // a number corresponding to what card was dealt
	
	
	private JPanel pnlPlayer = new JPanel(); //player panel
	private JPanel pnlDealer = new JPanel(); // dealer panel
	
	private JButton btnHit = new JButton("Hit"); // hit button
	private JButton btnStand = new JButton("Stand"); // stand button
	private JMenuItem mntmHit = new JMenuItem("Hit"); // hit menu item
	private JMenuItem mntmStand = new JMenuItem("Stand"); // stand menu item
	
	// this list contains numbers corresponding to the cards dealt
	private ArrayList<Integer> dealtCardNumbers = new ArrayList<Integer>();
	// This list is the dealer's hand. A hand for the player is not needed
	private ArrayList<Card> dealerHand = new ArrayList<Card>(); 
	
	// Scores
	private int dealerScore = 0;
	private int playerScore = 0;
	
	// Set this to true to always hit 21
	private boolean always21 = false;
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {


		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Blackjack frame = new Blackjack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		


	}

	/**
	 * Create the frame.
	 */
	public Blackjack() {
		
		// Copied from the card dealer program:
		//spades hearts clubs diamonds
		// Set the cards
		for (int index = 0, value = 2, number = 0; index < cards.length; index++, number++) {
			// New card
			cards[index] = new Card();
			
			// Set the image
			try {
				cards[index].setFrontFaceIcon(new ImageIcon(ImageIO.read(new File("cards/" + index + ".jpg"))));
			} catch (IOException e) {
				try {
					cards[index].setFrontFaceIcon(new ImageIcon(ImageIO.read(new File("cards\\" + index + ".jpg"))));
				} catch (IOException e2) {
					System.out.println("Could not set image of card " + index);
				}
			}
			// Update the icon
			cards[index].updateIcon(true);
			// Set the number
			cards[index].setNumber(index);
			// Give them a name
			cards[index].setName(Integer.toString(index));
			// Then assign a value
			cards[index].setValue(value);
			
			// Face cards must be 10
			if (index >= 36 && index <= 47) {
				cards[index].setValue(10);
			}
			// Aces must always be 11
			if (index >= 48 && index <= 51) {
				cards[index].setValue(11);
			}
			
			// Change the suit. Then increase the value every 4 cards
			if (number % 4 == 0) { cards[index].setSuit("spades"); }
			else if (number % 4 == 1) { cards[index].setSuit("hearts"); }
			else if (number % 4 == 2) { cards[index].setSuit("clubs"); }
			else if (number % 4 == 3) {value++; cards[index].setSuit("diamonds"); }

			// Debug
			System.out.println("cards/" + index + ".jpg | " + cards[index].toString());
		}
		
		
		setResizable(false);
		setTitle("Caruso : Blackjack");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close :)
		setBounds(100, 100, 695, 705);
		
		// Menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// Options menu
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		// Hit menu item
		mntmHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hit();
			}
		});
		mnOptions.add(mntmHit);
		
		// Stand menu item
		mntmStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stand();
			}
		});
		mnOptions.add(mntmStand);
		
		// New game menu item
		JMenuItem mntmNewGame = new JMenuItem("New Game");
		mntmNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		mnOptions.add(mntmNewGame);
		
		// Debug menu (for debugging. This actually isn't need anymore)
		JMenu mnDebug = new JMenu("Debug");
		menuBar.add(mnDebug);
		
		// Score menu item (display the score of each player)
		JMenuItem mntmScores = new JMenuItem("Scores");
		mntmScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Dealer: " + dealerScore);
				System.out.println("Player: " + playerScore);
			}
		});
		mnDebug.add(mntmScores);
		
		JCheckBoxMenuItem chckbxmntmAlways = new JCheckBoxMenuItem("Always 21");
		chckbxmntmAlways.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				always21 = !always21;
				System.out.println("always21: " + always21);
			}
		});
		mnDebug.add(chckbxmntmAlways);
		
		// Main panel
		CardDealerPane = new JPanel();
		CardDealerPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(CardDealerPane);
		CardDealerPane.setLayout(null);
		
		// Put a scroll pane around the dealer's panel, for if the dealer has more than 4 cards
		JScrollPane srlpnDealer = new JScrollPane();
		srlpnDealer.setBounds(12, 12, 674, 282);
		CardDealerPane.add(srlpnDealer);
		
		// Now make the dealer panel
		pnlDealer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		pnlDealer.setBorder(new TitledBorder(null, "Dealer", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		srlpnDealer.setViewportView(pnlDealer);
		
		
		
		// Repeat for the player
		JScrollPane srlpnPlayer = new JScrollPane();
		srlpnPlayer.setBounds(12, 300, 674, 282);
		CardDealerPane.add(srlpnPlayer);
		
		pnlPlayer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		srlpnPlayer.setViewportView(pnlPlayer);
		pnlPlayer.setBorder(new TitledBorder(null, "Player", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		
		
		
		
		// Hit button
		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hit();
			}
		});
		btnHit.setBounds(12, 594, 114, 25);
		CardDealerPane.add(btnHit);
		
		// New game button
		JButton btnNewGame = new JButton("New Game");
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
			}
		});
		btnNewGame.setBounds(12, 625, 114, 25);
		CardDealerPane.add(btnNewGame);
		
		// Stand button
		btnStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				stand();
			}
		});
		btnStand.setEnabled(true);
		btnStand.setBounds(134, 594, 114, 25);
		CardDealerPane.add(btnStand);
		
		// Start a new game
		newGame();
		

	}
	
	/**
	 * hit method
	 */
	private void hit() {
		// Deal a card for the player
		dealPlayerCard();
		
		// Check if the game has to end
		if (playerScore > 21) {
			endGame();
			JOptionPane.showMessageDialog(null, "You went over 21. You lose");
		} else if (playerScore == 21) {
			endGame();
			JOptionPane.showMessageDialog(null, "You hit 21. You win!");
			
		}
		
	}
	
	/**
	 * stand method
	 */
	private void stand() {
		
		// Flip over all of the cards
		for (int h = 0; h < dealerHand.size(); h++) { dealerHand.get(h).updateIcon(true); }
		
		// Deal cards for the dealer until the dealer's score is 17 or greater
		while (dealerScore <= 17) {
			dealDealerCard();
		} 
		
		// Check who wins
		if (playerScore == dealerScore) {
			JOptionPane.showMessageDialog(null, "It's a tie!");
		} else if (dealerScore > 21) {
			JOptionPane.showMessageDialog(null, "The dealer went over 21. You win!");
		} else if (playerScore < dealerScore && dealerScore == 21) {
			JOptionPane.showMessageDialog(null, "The dealer's score is 21. The dealer wins");
		} else if (playerScore < dealerScore) {
			JOptionPane.showMessageDialog(null, "The dealer beat your score. The dealer wins");
		} else if (playerScore > dealerScore) {
			JOptionPane.showMessageDialog(null, "You beat the dealer's score. You win!");
		} else if (dealerScore == 21) {
			JOptionPane.showMessageDialog(null, "The dealer hit 21. The dealer wins");
		} else {
			JOptionPane.showMessageDialog(null, "Something's wrong...");
		}
		
		// End the game
		endGame();
	}
	
	/**
	 * This method ends the game
	 */
	private void endGame() {
		// debug...
		System.out.println("Ending game");
		
		// Flip the cards
		for (int h = 0; h < dealerHand.size(); h++) { dealerHand.get(h).updateIcon(true); }
		
		// Disable the buttons and menu items
		btnHit.setEnabled(false);
		btnStand.setEnabled(false);
		mntmHit.setEnabled(false);
		mntmStand.setEnabled(false);
	}
	
	
	/**
	 * This method shuffles and deals a card
	 */
	private void shuffleCards() {
		// debug
		System.out.println("Shuffling cards");
		
		// Clear list of dealt cards
		dealtCardNumbers.clear();
		
		// more debug...
		System.out.println("\n---------------\nCards shuffled.");
	}
	
	/**
	 * Start a new game
	 */
	private void newGame() {
		
		// Debug
		System.out.println("Starting new game");
		
		// RESET THE ICONS YOU DUMB IDIOT
		for (int i = 0; i < cards.length; i++) {
			cards[i].updateIcon(true);
		}
		
		//Reset scores
		dealerScore = 0;
		playerScore = 0;
		
		// Restart the boards (remove all items from the panels)
		pnlDealer.removeAll();
		pnlDealer.revalidate();
		pnlPlayer.removeAll();
		pnlPlayer.revalidate();
		

		// shuffle 
		shuffleCards();
		

		
		// Deal cards for the computer
		dealDealerCard();
		dealDealerCard();
		dealerHand.get(dealerHand.size() - 1).updateIcon(false); // flip over the computer's last icon

		// Deal the cards for the player
		dealPlayerCard();
		dealPlayerCard();
		
		
		// (re)enable buttons
		btnHit.setEnabled(true);
		btnStand.setEnabled(true);
		mntmHit.setEnabled(true);
		mntmStand.setEnabled(true);
		
		// the game is over before it even started
		
		if (playerScore == 21 && dealerScore == 21) {
			JOptionPane.showMessageDialog(null, "Both you and the dealer are already on 21. It's a tie");
			endGame();
		} else if (dealerScore == 21) {
			JOptionPane.showMessageDialog(null, "The dealer is already on 21. You lose");
			endGame();
		} else if (playerScore > 21) {
			JOptionPane.showMessageDialog(null, "You are already over 21. You lose");
			endGame();
		} else if (playerScore == 21) {
			JOptionPane.showMessageDialog(null, "You are already on 21. You win!");
			endGame();
		}
		
	}
	
	/**
	 * deals a card
	 */
	private void dealCard() {
		// While a card has not yet been dealt...
		do {
			// Generate a random number, corresponding to a card
			dealtCard = random.nextInt(cards.length);
		} while (dealtCardNumbers.contains(dealtCard));
		
		// Add that number to the list of dealt cards
		dealtCardNumbers.add(dealtCard);

		// Debug
		System.out.println("Card dealt - " + cards[dealtCard].toString());
	}
	
	/**
	 * method to deal card for the player
	 */
	private void dealPlayerCard() {
		// Set the value of an ace accordingly
		/*
		for (int a = 48; a < 52; a++) {
			if (playerScore <= 10) { cards[a].setValue(11); }
			else if (playerScore > 10) { cards[a].setValue(1); }
		}
		*/
		
		// deal card
		dealCard();

		// add it to the player's panel
		pnlPlayer.add(cards[dealtCard]);
		// add that value to the score
		playerScore += cards[dealtCard].getValue();
		
		// Update the player panel
		pnlPlayer.repaint();
		pnlPlayer.revalidate();
	}
	/**
	 * method to deal card for the computer {
	 */
	private void dealDealerCard() {
		// Set the value of an ace accordingly
		/*
		for (int a = 48; a < 52; a++) {
			if (dealerScore <= 10) { cards[a].setValue(11); }
			else if (dealerScore > 10) { cards[a].setValue(1); }
		}
		*/
		
		// deal a card
		dealCard();
		// add the card to the dealer panel
		pnlDealer.add(cards[dealtCard]);
		// calculate the dealer's score
		dealerScore += cards[dealtCard].getValue();
		// add the card to the dealer's hand
		dealerHand.add(cards[dealtCard]);
		
		// Update the dealer panel
		pnlDealer.repaint();
		pnlDealer.revalidate();
	}
}
