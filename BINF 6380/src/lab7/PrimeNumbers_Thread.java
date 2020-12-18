package lab7;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.ArrayList;


public class PrimeNumbers_Thread extends JFrame {
	
	private static final long serialVersionUID = 1L;
	volatile private boolean cancel_operation = false;
	
	private JButton start_button = new JButton("Start");
	private JButton cancel_button = new JButton("Cancel");
	private JButton exit_button = new JButton("Exit");
	private JTextArea status = new JTextArea();
	private JScrollPane scroll_pane = new JScrollPane();
	private long user_enter;
	private JTextField out = new JTextField();
	
	public PrimeNumbers_Thread()
	{
		super("Prime Number Finder GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		
		JPanel top_panel = new JPanel();
		JLabel intro_label = new JLabel("Prime Number Finder. " +  "Press Start to Begin. ");
		top_panel.setLayout(new GridLayout(0,1));
		top_panel.add(intro_label);
		
		JPanel mid_panel = new JPanel();
		mid_panel.setLayout(new GridLayout(0,2));
		mid_panel.add(out);
		scroll_pane = new JScrollPane(status, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mid_panel.add(scroll_pane);
		
		JPanel bottom_panel = new JPanel();
		exit_button.addActionListener(new CloseWindow());
		bottom_panel.add(start_button);
		bottom_panel.add(cancel_button);
		bottom_panel.add(exit_button);
		
		getContentPane().add(top_panel, BorderLayout.NORTH);
		getContentPane().add(mid_panel, BorderLayout.CENTER);
		getContentPane().add(bottom_panel, BorderLayout.SOUTH);
		
		start_button.addActionListener(new start_finding());
		cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(cancel_operation == true) 
				{
					cancel_operation = false;
				}
				else
				{
					cancel_operation = true;
				}
			}
		});
		
		setLocationRelativeTo(null);
		setSize(600,400);
		setVisible(true);
		
	}
	
	private class CloseWindow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	private class start_finding implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String user_num = JOptionPane.showInputDialog(null, "Enter a large number (>6 digits and an integer: ) ", null, JOptionPane.PLAIN_MESSAGE);
			user_enter = Long.parseLong(user_num);
			cancel_button.setEnabled(true);
			start_button.setEnabled(false);
			exit_button.setEnabled(true);
			status.setText("");
			out.setText("");
			new Thread(new find_primes()).start();
		}
	}
	
	private void update_status()
	{
		validate();
	}
	
	private class find_primes implements Runnable
	{
		ArrayList<Long> prime_nums = new ArrayList<Long>();
		volatile long start_time = System.currentTimeMillis();
		volatile long time1 = System.currentTimeMillis();
		volatile long time2;
		volatile long save_time = 0;
		
		private void set_output(long i, long num)
		{
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if ((time2-time1)/1000.0 >5.0)
					{
						save_time += (time2-time1);
						out.setText("Found " + prime_nums.size() + " numbers: " + i + ": " + (save_time)/1000f + " seconds");
						update_status();
						time1 = System.currentTimeMillis();
					}
				}
			});
		}
		
		public void run()
		{
			long user_in = user_enter;
			
			for(long i = 1; i <= user_in && !cancel_operation; i++)
			{
				time2 = System.currentTimeMillis();
				boolean non_prime = false;
				
				for(long ii = 2; ii <= i/2 && !non_prime; ii++)
				{
					if(i % ii == 0)
					{
						non_prime = true;
					}
				}
				time2 = System.currentTimeMillis();
				
				if(!non_prime)
				{
					status.append("" + i + "\n");
					prime_nums.add(i);
					set_output(i, user_in);
				}
			}
			long stop_time = System.currentTimeMillis();
			long total_time = (stop_time - start_time)/1000l;
			out.setText("Found " + prime_nums.size() + " numbers in: "+ total_time + " seconds");
			status.append("Total time: " + total_time);
			update_status();
			cancel_button.setEnabled(false);
			start_button.setEnabled(true);
			//user_enter.setEnabled(true);
			cancel_operation = false;
		}
	}
	
	public static void main(String[] args) 
	{
		new PrimeNumbers_Thread();
	}

}








