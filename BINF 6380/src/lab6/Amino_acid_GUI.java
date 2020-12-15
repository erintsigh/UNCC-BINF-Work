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

public class Amino_acid_GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private Random RANDOM = new Random();
	private int correct = 0;
	private int incorrect = 0;
	private int total_time = 30;
	
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
		
		start_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timer_countdown.start();
			}
			
		});
		
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
	
	private JPanel Quiz_panel()
	{
		JPanel main_panel = new JPanel();
		
		main_panel.add(aa_label);
		main_panel.add(user_answer);
		main_panel.add(submit_button);
		
		int randOuter = RANDOM.nextInt(20);
		aa_label.setText(FULL_NAMES[randOuter]);
		submit_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = user_answer.getText();
				String q = SHORT_NAMES[Arrays.asList(FULL_NAMES).indexOf(aa_label.getText())];

				if (q.equals(s.toUpperCase())) {
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
				int randInner = RANDOM.nextInt(20);
				aa_label.setText(FULL_NAMES[randInner]);
				
			}	
			
		});
		
		return main_panel;
	}
	
	private class CloseWindow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	
	public static void main(String[] args)
	{
		new Amino_acid_GUI();
	}

}