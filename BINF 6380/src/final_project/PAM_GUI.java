package final_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import final_project.FastaParser;


public class PAM_GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String filepath = "/Users/erintsigh/Desktop/NC_000913.fa";
	
	private List<FastaParser> fastaList;
	//private List<FastaParser> pam_group;
	private static File selected_file;
	private static File file_path;

	private volatile boolean close_operation = false;
	private Thread counter;
	
	private JPanel cards;
	private JButton open_button = new JButton("Open");
	private JButton start_button = new JButton("Start Run");
	private JButton cancel_button = new JButton("Cancel");
	private JButton exit_button = new JButton("Exit");
	private JButton new_button = new JButton("New Run");
	private JTextField pam_user = new JTextField(20);
	//private JTextField fasta_file = new JTextField(20);
	private JTextArea found_display = new JTextArea(200,50);
	private final JScrollPane scroll_window = new JScrollPane(found_display);
	
	private String pam_enter;
	private volatile long count = 0;
	private volatile long endNum = 0;
	private volatile int digit = 0;
	private volatile boolean cancel = false;
	
	public PAM_GUI() throws Exception
	{
		super("CRISPR PAM Finder");
		setLocationRelativeTo(null);
		setSize(880,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cards = new JPanel(new CardLayout());
		cards.add(startPanel(), "start");
		//cards.add(progressPanel(), "progress");
		cards.add(endPanel(), "end");
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(cards, BorderLayout.CENTER);
		setVisible(true);
		
	}
	
	private JPanel startPanel() throws Exception
	{
		JPanel panel = new JPanel();
		JPanel mid_panel = new JPanel();
		JPanel submit_panel = new JPanel();
		JPanel fasta_panel = new JPanel();
		JPanel bottom_panel = new JPanel();
		
		submit_panel.setLayout(new FlowLayout());
		JLabel user_pam_label = new JLabel("Enter PAM sequence of interest (2-7bp): ");
		submit_panel.add(user_pam_label);
		submit_panel.add(pam_user);
		
		//submit_panel.setLayout(new FlowLayout());
		
		JLabel fasta_label = new JLabel("Select a FASTA file");
		fasta_panel.add(fasta_label);
		fasta_panel.add(open_button);
		open_button.addActionListener(e -> {
			pickFile();
		});

		//mid_panel.add(submit_panel, BorderLayout.NORTH);
		//mid_panel.add(fasta_panel, BorderLayout.SOUTH);
		
		JLabel start_description = new JLabel("<html>"+"CRISPR PAM Site Finder"+ "<br/>" + "<br/>" +  "Looks for PAM sites and their positions (start at pos 1) in your FASTA file (one file per run). <br/>" +"</html>");
		
		start_description.setFont(new Font("Arial", Font.BOLD, 20));
		
		bottom_panel.add(start_button, BorderLayout.EAST);
		bottom_panel.add(exit_button, BorderLayout.WEST);
		
		//panel.setLayout(new BorderLayout());		
		panel.add(start_description, BorderLayout.NORTH);
		panel.add(submit_panel, BorderLayout.EAST);
		panel.add(fasta_panel, BorderLayout.WEST);
		panel.add(mid_panel, BorderLayout.CENTER);
		panel.add(bottom_panel, BorderLayout.SOUTH);
		
		exit_button.addActionListener(new CloseWindow());
		
		List<FastaParser> fastaList = FastaParser.readFastaFile(filepath);
		String pamsite_enter = new String();
		
		start_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try 
				{
					CardLayout c = (CardLayout)(cards.getLayout());
					c.show(cards, "end");
					found_display.setText("");
					String pam_enter = (pam_user.getText()).toUpperCase();
					String pam_regex = FastaParser.getPAMregex(pamsite_enter);
					int len_pam = pam_enter.length();
					
					if(len_pam > 1 && len_pam <7)
					{
						found_display.append("Finding PAM sites with: " + pam_enter + "\n\n");
						
						for(FastaParser fs: fastaList)
						{
							String seq = fs.getSequence();
							String head = fs.getHeader();
							
							List<String> pam_matches = new ArrayList<String>(); 
							List<String> pos_matches = new ArrayList<String>();
							
							Matcher matcher = Pattern.compile(pam_regex).matcher(seq);
							
							if(matcher.find())
							{
								while(matcher.find())
								{
									pam_matches.add(matcher.group());
									pos_matches.add(String.valueOf(matcher.start()+1));
								}
							}
							else
							{
								pam_matches.add("NOTHING FOUND");
								pos_matches.add("NOTHING FOUND");
							}
							
							for(int i=0; i<pam_matches.size(); i++)
							{
								System.out.println("PAM found " + pam_matches.get(i) + "\n");
								System.out.println("pos: " + pos_matches.get(i) + "\n\n");
							}
							
							
							//found_display.append(head + "\n");
							//found_display.append(seq + "\n");

							//for(int i=0; i < (fs.getPAMpos(pam_regex)).size(); i++)
							//{
								//found_display.append("PAMS: " + getPAMs(pam_regex, seq));
								//found_display.append("PAM's found: " + fs.getPAMs(pam_regex) + "\n");
							//	found_display.append("PAM positions found: " +((fs.getPAMpos(pam_regex))) + "\n\n");
								
							//}
							
						}
						
					}
					else
					{
						JOptionPane.showMessageDialog(null, ("ERROR. You must enter a PAM site between 2-7bps."));
						pam_user.setText("");
						CardLayout c1 = (CardLayout)(cards.getLayout());
						c1.show(cards, "start");
					}
				
				}
				catch(Exception ex)
				{
					pam_user.setText("");
					CardLayout c = (CardLayout)(cards.getLayout());
					c.show(cards, "start");
				}
				
			}
		});
		
		return panel;
	}
	
	private JPanel progressPanel()
	{
		JPanel panel = new JPanel();
		JPanel bottom_panel = new JPanel();
		JButton start = new JButton("Start");
		JButton exit_btn = new JButton("Exit");
		
		bottom_panel.setLayout(new GridLayout(0,3));
		bottom_panel.add(start);
		bottom_panel.add(cancel_button);
		bottom_panel.add(exit_btn);
		
		panel.setLayout(new BorderLayout());
		found_display.setEditable(false);
		panel.add(found_display, BorderLayout.CENTER);
		panel.add(bottom_panel, BorderLayout.SOUTH);
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				cancel = false;
				start.setEnabled(false);
				cancel_button.setEnabled(true);
				//new Thread(new runnable()).start();
			}
		});
		
		cancel_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				cancel = true;
			}
		});
		
		exit_btn.addActionListener(new CloseWindow());
		
		return panel;
	}
	
	private JPanel endPanel()
	{
		JPanel panel = new JPanel();
		JPanel bottom_panel = new JPanel();
		JButton exit = new JButton("Exit");
		
		bottom_panel.setLayout(new GridLayout(0,2));
		bottom_panel.add(new_button);
		bottom_panel.add(exit);
		
		panel.setLayout(new BorderLayout());
		panel.add(scroll_window);
		panel.add(bottom_panel, BorderLayout.SOUTH);
		
		new_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				found_display.setText("");
				start_button.setEnabled(true);
				pam_user.setText("");
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "start");
			}
		});
		
		exit.addActionListener(new CloseWindow());
		
		return panel;
	}
	
	public void pickFile()
	{
		
		JFileChooser file_chooser = new JFileChooser();
		file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = file_chooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			selected_file = file_chooser.getSelectedFile();
		}
		else
		{
			CardLayout c = (CardLayout)(cards.getLayout());
			c.show(cards, "start");
		}
		
		file_path = selected_file.getAbsoluteFile();
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
	
	public static void main(String[] args) throws Exception
	{
		new PAM_GUI();
		String pam;
		String pos;
		String pamsite_new = "NGG";
		String pam_regex = FastaParser.getPAMregex(pamsite_new);
		
		String filepath = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/Mdomestica_short.fasta";
		System.out.println(file_path);
		List<FastaParser> fastaList = FastaParser.readFastaFile(filepath);
		
		
		for(FastaParser fs: fastaList)
		{
			//List<FastaParser> pamList = FastaParser.getPAMs(pam_regex, seq);
			System.out.println(fs);
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println("GC content: " + fs.getGCRatio());
			System.out.println((fs.getPAMpos(pam_regex)).size());
			System.out.println((fs.getPAMpos(pam_regex)));
			//System.out.println((fs.getPAMs(pam_regex)).size());
			//System.out.println((fs.getPAMs(pam_regex)));
			String head = fs.getHeader();
			String seq = fs.getSequence();
			
			System.out.println(head + ": " + seq);
			
			List<String> pam_matches = new ArrayList<String>();
			List<String> pos_matches = new ArrayList<String>();
			
			Matcher matcher = Pattern.compile(pam_regex).matcher(seq);
			
			if(matcher.find())
			{
				while(matcher.find())
				{
					pam_matches.add(matcher.group());
					pos_matches.add(String.valueOf(matcher.start()+1));
				}
			}
			else
			{
				pam_matches.add("NOTHING FOUND");
				pos_matches.add("NOTHING FOUND");
			}
			
			for(int i=0; i<pam_matches.size(); i++)
			{
				System.out.println("PAM found " + pam_matches.get(i) + "\n");
				System.out.println("pos: " + pos_matches.get(i) + "\n\n");
			}

				//for(int i=0; i < (fs.getPAMpos(pam_regex)).size(); i++)
				//{
	
					//System.out.println("PAM found: " + (fs.getPAMs(pam_regex).get(i)) + "\n");
				//	System.out.println("PAM position found: " + (fs.getPAMpos(pam_regex).get(i)) + "\n");
						
	
					
				//}
			//}
			
		}
		

		
		//calls method for writeUnique, write the path for output file and what to call the output file.
		//FastaParser.writeUnique(filepath,"/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/unique_fastas.txt");
	}

}
