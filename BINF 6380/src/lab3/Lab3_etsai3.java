package lab3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Lab3_etsai3 {
	
//	you can change the path and filename to what you want here
	private static final String path = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/"; 
	private static final String filename = "Mdomestica_short";
	
	
//	this method reads the file, parses the sequence name and sequence, then runs nucleotide counts method 4x for each nucleotide, 
//	then writes it all out to a new file. the new file will have the same path and name as the original file, but it will end in .txt
	
	private static void fasta_parse(String path, String filename) throws IOException
	{
		//List<String> name_list = new ArrayList<String>();
		//List<String> seq_list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(path + filename+ ".fasta")));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path+filename+".txt")));
		
		writer.write("sequenceID" + "\t" + "numA" + "\t" + "numC" + "\t" + "numG" + "\t" + "numT" + "\t" + "sequence" + "\n");
		System.out.println("sequenceID" + "\t" + "numA" + "\t" + "numC" + "\t" + "numG" + "\t" + "numT" + "\t" + "sequence" + "\n");
		
		String seq = "";
		String name = "";
		String first_name = ""; 
		
//		read the first line, blank line.
		reader.readLine();
		
		for(String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
		{
			if(nextLine.startsWith(">"))
			{
				name = (nextLine.substring(1).split(" ")[0]);
				//System.out.println(name + "\t" + seq);
				//name_list.add(name);
				//first_name = name_list.get(0);

				writer.write(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + "\t" + seq + "\n");
				System.out.println(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + "\t" + seq + "\n");
				//seq_list.add(seq);
				seq = "";
			}
			else
			{
				seq = seq + nextLine.strip();
				//seq_list.add(seq);
				//writer.write(name + "\t" + seq + "\n");
				//break;
			}
			
				
		}
		
		//System.out.println(name_list);
		//System.out.println(seq_list.size());
		writer.flush(); 
		writer.close();
		reader.close();
	}
	
	
//	this method calculates the amount of a specific nucleotide, needs a string to look through and a character to look for	
	private static int nuc_number(String sequence, char nuc) throws IOException
	{
		int num_nuc = 0;
		
		for(int i = 0; i < sequence.length(); i++)
		{
			char nucleotide = sequence.charAt(i);
			if(nucleotide == nuc)
			{
				num_nuc++;
			}

		}
		return num_nuc;
	}

//	main method that runs the parser method
	public static void main(String[] args) throws IOException
	{
		
		fasta_parse(path,filename);
		
	}
}
