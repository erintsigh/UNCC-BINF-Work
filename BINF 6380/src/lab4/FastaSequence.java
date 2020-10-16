package lab4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

public class FastaSequence {
	
//	you can change the path and file name to what you want here, make sure you add the extension to the filename
	private static final String filepath = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/practice.fasta"; 
	
	private String header;
	private String sequence;
	
//	returns the header of the sequence without the ">"
	public String getHeader()
	{
		header = (header.substring(1).split(" ")[0]);
//	the line above or below do the same thing. Either works for future reference.
//		header = header.replace('>', ' ').strip();
		return header;
	}
	
//	returns the DNA sequence of this FastaSequence
	public String getSequence()
	{
		return sequence;
	}

//	returns the number of G's and C's divided by the length of this sequence
	public float getGCRatio()
	{
		float num_GCs = 0;
		float seq_length = sequence.length();
		
		for(int i = 0; i < seq_length; i++)
		{
			char nucleotide = sequence.charAt(i);
			if(nucleotide == 'G' || nucleotide == 'C')
			{
				num_GCs++;
			}
		}
		return (num_GCs/seq_length);
	}
	
	public FastaSequence(String header, String sequence)
	{
		this.header = header;
		this.sequence = sequence;
	}

	public static List<FastaSequence> readFastaFile(String filepath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(FastaSequence.filepath)));
		
		List<FastaSequence> fs = new ArrayList<FastaSequence>();
		
		String header = "";
		StringBuffer sequence = new StringBuffer();
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
					fs.add(new FastaSequence(header, sequence.toString()));
				}
				header = nextLine;
				sequence = new StringBuffer();
			}
			else
			{
				sequence.append(nextLine);
			}
			
			counter ++;
		}
		fs.add(new FastaSequence(header, sequence.toString()));
		reader.close();

		return fs;
	}

//that writes each unique sequence to the output file with the # of times each sequence was 
//	seen in the input file as the header (sorted with the sequence seen the fewest times the first)


	public static void writeUnique(String inFile, String outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(inFile)));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
		List<FastaSequence> fastaList = FastaSequence.readFastaFile(filepath);
		HashMap<String, String> fastaMap = new HashMap<String, String>();
		Map<String, Integer> numberMap = new HashMap<String, Integer>();
		
		for(FastaSequence fs: fastaList)
		{
			fastaMap.put(fs.getHeader(), fs.getSequence());
		}
		
		Collection<String> fastaValues = fastaMap.values();
		
		for(String value : fastaValues)
		{
			numberMap.put(value,(Collections.frequency(fastaValues, value)));
		}
		
		for(String key: numberMap.keySet())
		{
			
			writer.write(">" + key + "\n" + numberMap.get(key) + "\n");
			System.out.println(">" + key + "\n" + numberMap.get(key) + "\n");
		}

		writer.flush(); 
		writer.close();
		reader.close();
	}
	
}
