package lab6;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.Timer;

//Amino acid quiz gui, 30 second timer
//must hit ****Start**** button to start the timer countdown

public class Amino_acid_GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private Random RANDOM = new Random();
	private int correct = 0;
	private int incorrect = 0;
	private int total_time = 30;
	
	//using the swing timer
	Timer timer_countdown;
	private int timer_count = 0;
	
	public static String[] SHORT_NAMES = {
			"A","R","N","D","C","Q","E","G","H",
			"I","L","K","M","F","P","S","T","W","Y","V"
			};
	
	public static String[] FULL_NAMES = {
			"alanine","arginine","asparagine","aspartic acid","cysteine",
			"glutamine","glutamic acid","glycine","histidine","isoleucine","leucine","lysine","methionine",
			"phenylalanine","proline","serine","threonine","tryptophan","tyrosine","valine"
			} ;
	
	private JFrame aFrame = new JFrame();
	private JButton start_button = new JButton("Start");
	private JButton exit_button = new JButton("Exit");
	private JButton cancel_button = new JButton("Cancel");
	private JButton submit_button = new JButton("Submit");
	private JTextField user_answer = new JTextField(25);
	private JLabel correct_label = new JLabel();
	private JLabel incorrect_label = new JLabel();
	private JLabel time_label = new JLabel();
	private JLabel aa_label = new JLabel();
	
	//contains most of the GUI functionality:
	//main panel contains the actual quiz portion
	//score panel contains the incorrect and correct count, it also contains the timer countdown
	//bottom panel has the start button, cancel button, and exit button. 
	//the user must hit the start button to start the countdown timer
	//cancel button cancels the quiz, stops timer, resets the counts and timer
	//exit button quits out of the entire GUI
	public Amino_acid_GUI() {
		super("Amino Acid Quiz");
		aFrame.setSize(900,550);
		aFrame.setLocationRelativeTo(null);
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		aFrame.setLayout(new BorderLayout());
		
		JPanel main_panel = Quiz_panel();
		JPanel score_panel = new JPanel();
		JPanel bottom_panel = new JPanel();
		
		score_panel.add(correct_label);
		score_panel.add(incorrect_label);
		score_panel.add(time_label);
		
		bottom_panel.add(start_button);
		bottom_panel.add(cancel_button);
		bottom_panel.add(exit_button);

		exit_button.addActionListener(new CloseWindow());
		
		//timer action listener, if the timer counts up to 30 seconds, then a joptionpane pops up to tell you time is up
		//and your score, then another joptionpane tells you can you start over
		//clears the time label, correct label, and incorrect label
		//must hit start button to start the quiz over
		timer_countdown = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time_label.setText("Time Left: " + (total_time - timer_count)+ " seconds");
				timer_count++;
				//System.out.println(timer_count);
				if(timer_count == (total_time+1))
				{
					timer_countdown.stop();
					total_time = 30;
					timer_count = 0;
					JOptionPane.showMessageDialog(null, ("Time's up! \nYou got " + correct + " correct out of " + (correct+incorrect) + "!"));
					incorrect = 0;
					correct = 0;
					JOptionPane.showMessageDialog(null, "To try again... \nExit out of this window and hit Start.");
					time_label.setText("Time Left: " + "--");
					correct_label.setText("Correct: --  ");
					incorrect_label.setText("Incorrect: --  ");
				}
			}
		});
		
		//start button action listener, starts the timer countdown
		start_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				incorrect = 0;
				correct = 0;
				total_time = 30;
				time_label.setText("Time Left: " + "--");
				correct_label.setText("Correct: --  ");
				incorrect_label.setText("Incorrect: --  ");
				timer_countdown.start();
			}
			
		});
		
		//cancel button stops timer countdown, and restarts the total time, correct and incorrect counters
		//displays joptionpane to tell you quiz was cancelled
		//need to select start button to start quiz over
		cancel_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer_countdown.stop();
				total_time = 30;
				correct = 0;
				incorrect = 0;
				JOptionPane.showMessageDialog(null, "Quiz was cancelled. \nSelect start to restart the quiz after exiting this window. ");
			}
			
		});
		
		aFrame.add(main_panel, BorderLayout.NORTH);
		aFrame.add(score_panel, BorderLayout.CENTER);
		aFrame.add(bottom_panel, BorderLayout.SOUTH);
		
		aFrame.setVisible(true);
		JOptionPane.showMessageDialog(null, "Enter the one letter code for each amino acid. \nYou have 30 seconds to get as many correct as possible. \n\nPress Start to Begin once you exit this window! ");
	}
	
	//this is the main panel that displays the randomly chosen amino acid, the textbox that the user can enter their answer, 
	//and the submit button the user needs to choose to enter in their guess.
	private JPanel Quiz_panel()
	{
		JPanel main_panel = new JPanel();
		
		main_panel.add(aa_label);
		main_panel.add(user_answer);
		main_panel.add(submit_button);
		
		//calls a random integer between 1-20 then chooses a random amino acid by calling a random int to then choose from the list of amino acids
		int rand1 = RANDOM.nextInt(20);
		aa_label.setText(FULL_NAMES[rand1]);
		
		//adds an action listener to the submit button to also check the user's answer. if the answer is correct it is displayed under the correct
		//label, if incorrect displayed under the incorrect label. 
		submit_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String answer = user_answer.getText();
				String check = SHORT_NAMES[Arrays.asList(FULL_NAMES).indexOf(aa_label.getText())];

				if (check.equals(answer.toUpperCase())) {
					correct++;
					//System.out.println("correct: " + correct);
					correct_label.setText("Correct: " + String.valueOf(correct) + "\t ");
					user_answer.setText("");
				} else {
					incorrect++;
					//System.out.println("incorrect: " + incorrect);
					incorrect_label.setText("Incorrect: " + String.valueOf(incorrect) + "\t ");
					user_answer.setText("");
					}
				int rand2 = RANDOM.nextInt(20);
				aa_label.setText(FULL_NAMES[rand2]);
				
			}	
			
		});
		
		return main_panel;
	}
	
	//Class that closes the window and stops the run. 
	private class CloseWindow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	//main method
	public static void main(String[] args)
	{
		new Amino_acid_GUI();
	}

}