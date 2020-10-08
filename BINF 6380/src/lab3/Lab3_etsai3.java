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
	
	private static final String path = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/"; 
	private static final String filename = "Mdomestica_short";
	
	private static void fasta_parse(String path, String filename) throws IOException
	{
		List<String> name_list = new ArrayList<String>();
		List<String> seq_list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(path + filename+ ".fasta")));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path+filename+".txt")));
		
		writer.write("sequenceID" + "\t" + "numA" + "\t" + "numC" + "\t" + "numG" + "\t" + "numT" + "\t" + "sequence" + "\n");
		
		
		String seq = "";
		String name = "";
		for(String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
		{
			if(nextLine.startsWith(">"))
			{
				name = (nextLine.substring(1).split(" ")[0]);
				seq = "";
				name_list.add(name);
				
				//for(nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
				//{
					//seq_list.add(seq);
					if((nextLine.startsWith("A"))||(nextLine.startsWith("C"))||(nextLine.startsWith("G"))||(nextLine.startsWith("T")))
					{
						seq = seq + (nextLine.strip());
						
					}
					else
					{
						seq_list.add(seq);
						writer.write(name + "\t" + seq + "\n");
						//break;
					}
				//}
				//writer.write(name + "\t" + seq + "\n");
				//writer.write(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + seq + "\n");
			}
		//writer.write(name + "\t" + seq);
		}
		//writer.write(name + "\t" + nuc_number(seq,'A') + "\t" + nuc_number(seq, 'C') + "\t" + nuc_number(seq, 'G') + "\t" + nuc_number(seq, 'T') + seq + "\n");
		System.out.println(name_list);
		System.out.println(seq_list);
		writer.flush(); 
		writer.close();
		reader.close();
	}
	
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

	public static void main(String[] args) throws IOException
	{
		
		fasta_parse(path,filename);
		
	}
}
