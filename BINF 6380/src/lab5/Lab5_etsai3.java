package lab5;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Lab5_etsai3 implements ActionListener 
{
	JFrame aFrame = new JFrame();
	JFrame bFrame = new JFrame();
	
	JRadioButton button1, button2, button3, button4;
	JButton b1, b2;
	JLabel label1, label2;
	ButtonGroup buttons;
	
	List<String> question = Arrays.asList("1) Which boop bop made meep mops? ", "2) Why did the meep mop do bloops? ");
	List<String> choice1 = Arrays.asList("This one", "A1");
	List<String> choice2 = Arrays.asList("B", "B1");
	List<String> choice3 = Arrays.asList("C", "This one.");
	List<String> choice4 = Arrays.asList("D", "D1");
	List<String> answer = Arrays.asList("This one", "This one.");
	
	int num;
	int lsize = question.size();
	
	public Lab5_etsai3()
	{
		aFrame = new JFrame();
		aFrame.setLayout(null);
		aFrame.setSize(700,650);
		aFrame.setLocationRelativeTo(null);
		Container c = aFrame.getContentPane();
		c.setBackground(Color.gray);
		
		label1 = new JLabel(question.get(0));
		label1.setBounds(50,50,600,30);
		aFrame.add(label1);
		Font qf = new Font("Serif", Font.BOLD, 18);
		label1.setFont(qf);
		
		button1 = new JRadioButton(choice1.get(0));
		button1.setBounds(100,120,100,30);
		aFrame.add(button1);
		
		button2 = new JRadioButton(choice2.get(0));
		button2.setBounds(350,120,100,30);
		aFrame.add(button2);
		
		button3 = new JRadioButton(choice3.get(0));
		button3.setBounds(100,200,100,30);
		aFrame.add(button3);
		
		button4 = new JRadioButton(choice4.get(0));
		button4.setBounds(350,200,100,30);
		aFrame.add(button4);
		
		buttons = new ButtonGroup();
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		buttons.add(button4);
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		
		b1 = new JButton("Enter");
		b1.setBounds(100,400,100,30);
		aFrame.add(b1);
		
		b1.addActionListener(this);
		
		aFrame.setJMenuBar(getMyMenuBar());
		aFrame.setVisible(true);
		aFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	public static void main(String[] args)
	{
		new Lab5_etsai3();
	}

	public void actionPerformed(ActionEvent event)
	{
		
		if(event.getSource()==b1)
		{
			String s = "";
			if(button1.isSelected())
			{
				s = button1.getText();
			}
			if(button2.isSelected())
			{
				s = button2.getText();
			}
			if(button3.isSelected())
			{
				s = button3.getText();
			}
			if(button4.isSelected())
			{
				s = button4.getText();
			}
			
			if(s.contentEquals(answer.get(num)))
			{
				num++;
				if(num<lsize)
				{
					JOptionPane.showMessageDialog(null, "Correct! Next Question");;
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Correct! End of Quiz!");
					
					//System.exit(0);
					aFrame.getContentPane().removeAll();
					aFrame.repaint();
					
					Container c = aFrame.getContentPane();
					c.setBackground(Color.gray);
					JLabel endlabel = new JLabel("You have completed the quiz! Save quiz questions in menu or exit.");
					endlabel.setBounds(110,150,600,30);
					aFrame.add(endlabel);
					Font qf = new Font("Serif", Font.BOLD, 18);
					endlabel.setFont(qf);
					aFrame.setJMenuBar(getMyMenuBar());
					aFrame.setVisible(true);
				}
				label1.setText(question.get(num));
				button1.setText(choice1.get(num));
				button2.setText(choice2.get(num));
				button3.setText(choice3.get(num));
				button4.setText(choice4.get(num));
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Incorrect!");
			}
		}
	}
	
	private JMenuBar getMyMenuBar()
	{
		JMenuBar jmenuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		jmenuBar.add(fileMenu);
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setMnemonic('S');
		fileMenu.add(saveItem);
		
		saveItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveToFile();
			}
		});
		
		JMenuItem quitWindow = new JMenuItem("Quit Quiz");
		quitWindow.setMnemonic('Q');
		fileMenu.add(quitWindow);
		
		return jmenuBar;
	}
	
	private void saveToFile()
	{
		JFileChooser jfc = new JFileChooser();
		
		if(jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
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
			String message = ("File ") + jfc.getSelectedFile().getName() + (" exists. Overwrite?");
			if(JOptionPane.showConfirmDialog(this, message) != JOptionPane.YES_OPTION)
			{
				return;
			}
		}
	}
}


