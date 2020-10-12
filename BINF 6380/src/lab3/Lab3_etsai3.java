package lab3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Lab3_etsai3 {
	
//	you can change the path and filename to what you want here
	private static final String path = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/"; 
	private static final String filename = "practice";
	
	
//	this method reads the file, parses the sequence name and sequence, then runs nucleotide counts method 4x for each nucleotide, 
//	then writes it all out to a new file. the new file will have the same path and name as the original file, but it will end in .txt
	
	private static void fasta_parse(String path, String filename) throws IOException
	{	
		BufferedReader reader = new BufferedReader(new FileReader(new File(path + filename+ ".fasta")));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path+filename+".txt")));
		
//		write out the headers to the new text file, tab separated
		writer.write("sequenceID" + "\t" + "numA" + "\t" + "numC" + "\t" + "numG" + "\t" + "numT" + "\t" + "sequence" + "\n");
		System.out.println("sequenceID" + "\t" + "numA" + "\t" + "numC" + "\t" + "numG" + "\t" + "numT" + "\t" + "sequence" + "\n");
		
		String seq = "";
		String name = "";
		int counter = 0;
		
//		iterating over every line to read in, if the line starts with a ">" then, it moves onto the next if statement
//		the next if statement says if the counter doesn't equal 0, then write out the name, nucleotide %'s, and sequence to the txt file
//		the counter adds 1 each time the for loop iterates.
		for(String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
		{
			if(nextLine.startsWith(">"))
			{
				if(counter!=0)
				{
					writer.write(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + "\t" + seq + "\n");
					System.out.println(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + "\t" + seq + "\n");
				}
				
				name = (nextLine.substring(1).split(" ")[0]);
				seq = "";
			}
			else
			{
				seq = seq + nextLine.strip();
			}
			
			counter ++;
		}
		
//		this line is added because the loop doesn't include the last seqID and sequence. then the reader and writer are closed.
		writer.write(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + "\t" + seq + "\n");
		System.out.println(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + "\t" + seq + "\n");
		
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
