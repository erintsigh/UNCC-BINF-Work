package final_project;

import java.awt.BorderLayout;
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
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
	//private static final String filepath = "/Users/erintsigh/Desktop/NC_000913.fa";
	
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
	private JButton exit = new JButton("Exit");
	private JButton export_button = new JButton("Export");
	private JButton export_btn = new JButton("Write VCF");
	private JButton exit_btn = new JButton("Exit");
	private JButton newrun_btn = new JButton("New Run");
	
	private JTextField pam_user = new JTextField(20);
	//private JTextField fasta_file = new JTextField(20);
	private JTextArea found_display = new JTextArea(200,50);
	private JTextArea found_disp = new JTextArea(200,50);
	private final JScrollPane scroll_window = new JScrollPane(found_display);
	private final JScrollPane scrolly = new JScrollPane(found_disp);
	
	
	////TO KNOW:
	////run may fail if the file is too large or if there are too many pam sites
	////if there are a lot of pam sites found, it will take a few minutes to load on the start page
	////start button will be highlighted/clicked until the run is finished.
	////I have provided an NC fasta file in git for running through this GUI, if needed.
	
	
	//holds all the gui pages 
	public PAM_GUI() throws Exception
	{
		super("CRISPR PAM Finder");
		setLocationRelativeTo(null);
		setSize(880,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cards = new JPanel(new CardLayout());
		cards.add(startPanel(), "start");
		//cards.add(progressPanel(), "progress");
		cards.add(resultsPanel(), "results");
		cards.add(exportPanel(), "export");
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(cards, BorderLayout.CENTER);
		setVisible(true);
		
	}
	
	//start card panel
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
		
		//select file method by action listener
		open_button.addActionListener(e->{
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
		
		//exits window
		exit_button.addActionListener(new CloseWindow());
		
		
		//starts the pam site finding action
		start_button.addActionListener(new startWork());
		
		return panel;
	}
	
	
	//export panel, where you can export the found pam sites to a vcf file of your choosing
	private JPanel exportPanel()
	{
		JPanel panel = new JPanel();
		JPanel bottom_panel = new JPanel();
		JPanel export_panel = new JPanel(); 
		
		JLabel export_lb = new JLabel("Write out results to a VCF file: ");
		
		//JButton export_btn = new JButton("Write VCF");
		//JButton exit_btn = new JButton("Exit");
		//JButton newrun_btn = new JButton("New Run");
		
		bottom_panel.setLayout(new GridLayout(0,2));
		bottom_panel.add(newrun_btn);
		bottom_panel.add(exit_btn);
		
		export_panel.setLayout(new GridLayout(0,2));
		export_panel.add(export_lb);
		export_panel.add(export_btn);
		
		panel.setLayout(new BorderLayout());
		panel.add(export_panel, BorderLayout.NORTH);
		panel.add(scrolly, BorderLayout.CENTER);
		panel.add(bottom_panel, BorderLayout.SOUTH);
		
		
		//action listener for new run button and goes back to start page
		newrun_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				found_display.setText("");
				found_disp.setText("");
				start_button.setEnabled(true);
				exit_btn.setEnabled(true);
				export_btn.setEnabled(true);
				newrun_btn.setEnabled(true);
				pam_user.setText("");
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "start");
			}
		});
		
		//action listener to export button and runs the exporting to file mechanism
		export_btn.addActionListener(new exportResults());
		
		//action listener for closing the entire window
		exit_btn.addActionListener(new CloseWindow());
		
		return panel;
	}
	
	
	//results panel has the scroll window of the results (found pam sites)
	private JPanel resultsPanel()
	{
		JPanel panel = new JPanel();
		JPanel bottom_panel = new JPanel();
		//JButton exit = new JButton("Exit");
		//JButton export_button = new JButton("Export");
		
		bottom_panel.setLayout(new GridLayout(0,3));
		bottom_panel.add(export_button);
		bottom_panel.add(new_button);

		bottom_panel.add(exit);
		
		panel.setLayout(new BorderLayout());
		panel.add(scroll_window);
		panel.add(bottom_panel, BorderLayout.SOUTH);
		
		//action listener for the export button that goes to the exporting vcf page/card
		export_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				start_button.setEnabled(true);
				export_button.setEnabled(true);
				exit.setEnabled(true);
				CardLayout c1 = (CardLayout)(cards.getLayout());
				c1.show(cards, "export");
			}
		});
		
		//new button's action listener to go back to the start page and resets.
		new_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				found_display.setText("");
				found_disp.setText("");
				start_button.setEnabled(true);
				export_button.setEnabled(true);
				exit.setEnabled(true);
				pam_user.setText("");
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "start");
			}
		});
		
		exit.addActionListener(new CloseWindow());
		
		return panel;
	}
	
	
	//method for using filechooser to open up a fasta file for getting pam sites
	private void pickFile()
	{

			JFileChooser file_chooser = new JFileChooser();
			file_chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			int result = file_chooser.showOpenDialog(this);
			
			if(file_chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				file_path = file_chooser.getSelectedFile();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "File Access Cancelled. ");
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "start");
			}
			
	}
	
	
	//action listener that closes the window and stops the run. 
	private class CloseWindow implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}
	
	
	//action listener for start button on start page, initiates pam site finding 
	private class startWork implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			try 
			{
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "results");
				found_display.setText("");
				found_disp.setText("");

				String pam_enter = (pam_user.getText()).toUpperCase();
				String pam_regex = FastaParser.getPAMregex(pam_enter);
				//System.out.println(pam_enter);
				//System.out.println(pam_regex);
				int len_pam = pam_enter.length();
				//String filepath = "/Users/erintsigh/Desktop/NC_000913.fa";
				List<String> seqs = new ArrayList<String>(); 
				List<String> heads = new ArrayList<String>(); 

				
				//if length of the pam site user entered is within 2-7 bp long then it'll run the pam site find
				if(len_pam > 1 && len_pam <8)
				{
					found_display.append("Finding PAM sites with: " + pam_enter + "\n\n");
					found_disp.append("Finding PAM sites with: " + pam_enter + "\n\n");

					heads = FastaParser.getHeads(file_path);
					seqs = FastaParser.getSeqs(file_path);				
				
					
					//iterates through the sequence/header list and appends to a display panel that goes into a jscroller
					//iterates through the pam site/position lists and appends to that display panel
					//so will give you pams found per sequence/header in your fasta file.
					//GC ratio will also be printed onto scrollpane per header/sequence
					for(int i = 0; i < 1; i++)
					{
						found_display.append((heads.get(i)) + "\n");
						found_disp.append((heads.get(i)) + "\n");
						found_display.append(seqs.get(i) + "\n");
						found_disp.append(seqs.get(i) + "\n");
						float gc_ratio = FastaParser.getGCRatio(seqs.get(i));
						String GC_rat = String.valueOf(gc_ratio);
						found_display.append("GC Ratio of Seq: " + GC_rat + "\n\n");
						found_disp.append("GC Ratio of Seq: " + GC_rat + "\n\n");
						int pam_list_size = ((FastaParser.getPAMs(pam_regex,seqs.get(i)).size()));
						
						for(int ii=0; ii < (pam_list_size); ii++)
						{
								found_display.append("PAM found " + (FastaParser.getPAMs(pam_regex, seqs.get(i)).get(ii)) + "\n");
								found_disp.append("PAM found " + (FastaParser.getPAMs(pam_regex, seqs.get(i)).get(ii)) + "\n");
								found_display.append("pos: " + (FastaParser.getPAMpos(pam_regex, seqs.get(i)).get(ii)) + "\n\n");
								found_disp.append("pos: " + (FastaParser.getPAMpos(pam_regex, seqs.get(i)).get(ii)) + "\n\n");
	
						}
						found_display.append("End of search for " + (heads.get(i).substring(1).split(" ")[0]) + "\n");
						found_disp.append("End of search for " + (heads.get(i).substring(1).split(" ")[0]) + "\n");
						found_display.append("PAM sites found in " + (heads.get(i).substring(1).split(" ")[0]) + " : " + pam_list_size);
						found_disp.append("PAM sites found in " + (heads.get(i).substring(1).split(" ")[0]) + " : " + pam_list_size);
					}

					
				}
				
				//if the user's pam length is not of correct sizing, it'll restart the panel/card 
				//and display a joptionpane error message
				else
				{
					JOptionPane.showMessageDialog(null, ("ERROR. You must enter a PAM site between 2-7bps."));
					pam_user.setText("");
					CardLayout c1 = (CardLayout)(cards.getLayout());
					c1.show(cards, "start");
				}
			
			}
			
			//if any errors are caught it restarts the card/panel
			catch(Exception ex)
			{
				pam_user.setText("");
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "start");
				JOptionPane.showMessageDialog(null, ("ERROR."));
			}
			
		}
	}
	
	//panel not implemented
	/*
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
	}*/
	
	
	//action listener for exported the results to an output file using jfilechooser 
	//to a location/name of your choosing
	//does not include .vcf extension, need to include that yourself.
	//then writes out the file using buffered writer
	private class exportResults implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{	
			JFileChooser jfc = new JFileChooser();
			
			if(jfc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION)
			{
				return;
			}
			
			if(jfc.getSelectedFile() == null)
			{
				return;
			}
			
			File chosenFile = jfc.getSelectedFile();
			
			if(jfc.getSelectedFile().exists())
			{
				String outMessage = "File " + jfc.getSelectedFile().getName() + " exists. Overwrite?";
				if(JOptionPane.showConfirmDialog(null, outMessage) != JOptionPane.YES_OPTION)
				{
					return;
				}
			}
			
			
			//writes out to a file of the PAM sites and their positions found to a VCF file
			try
			{
				BufferedWriter writer = new BufferedWriter(new FileWriter(chosenFile));
				
				String pam_enter = (pam_user.getText()).toUpperCase();
				String pam_regex = FastaParser.getPAMregex(pam_enter);
				//int len_pam = pam_enter.length();
				//String filepath = "/Users/erintsigh/Desktop/NC_000913.fa";
				List<String> seqs = new ArrayList<String>(); 
				List<String> heads = new ArrayList<String>(); 
				String seq = new String();
				String head = new String();
				String pam_found = new String();
				String pos_found = new String();
				
				heads = FastaParser.getHeads(file_path);
				seqs = FastaParser.getSeqs(file_path);
				
				writer.write("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO" + "\n");
				for(int i = 0; i < 1; i++)
				{
					head = ((heads.get(i)).substring(1).split(" ")[0]).replace(">", "");
					seq = (seqs.get(i));
					int pam_list_size = ((FastaParser.getPAMs(pam_regex,seqs.get(i)).size()));
					
					for(int ii=0; ii < (pam_list_size); ii++)
					{
							pam_found = (FastaParser.getPAMs(pam_regex, seqs.get(i)).get(ii));
							pos_found = (FastaParser.getPAMpos(pam_regex, seqs.get(i)).get(ii));
							
							writer.write(head + "\t" + pos_found + "\t" + ".\t" + pam_found + "\t" + pam_enter + "\t.\t.\t.\n");

					}
					
				}
				
				writer.flush();
				writer.close();
				
				//goes back to the same page/card it was on 
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "export");
				start_button.setEnabled(true);
				exit_btn.setEnabled(true);
				export_button.setEnabled(true);
				export_btn.setEnabled(true);
				newrun_btn.setEnabled(true);
				//pam_user.setText("");
				//found_display.setText("");
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				String errorMes = ex.getMessage();
				JOptionPane.showMessageDialog(null, errorMes, "Could not write file", JOptionPane.ERROR_MESSAGE);
				pam_user.setText("");
				found_display.setText("");
				found_disp.setText("");
				CardLayout c = (CardLayout)(cards.getLayout());
				c.show(cards, "start");
				JOptionPane.showMessageDialog(null, ("ERROR."));
			}

		}
	}
	
	
	//main method where PAM_GUI is called to.
	public static void main(String[] args) throws Exception
	{
		new PAM_GUI();

		
		//calls method for writeUnique, write the path for output file and what to call the output file.
		//FastaParser.writeUnique(filepath,"/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/unique_fastas.txt");
	}

}