package final_project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.regex.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.File;


public class FastaParser {

//	you can change the path and file name to what you want here, make sure you add the extension to the filename
	private static final String filepath = "/Users/erintsigh/Desktop/NC_000913.fa"; 
	
	public String header;
	public String sequence;
	
	public String match;
	public int pam_pos;
	public String pam_reg;
	
	private List<FastaParser> pam_group;
	
	private static List<String> pam_matches = new ArrayList<String>();
	private static List<String> pos_matches = new ArrayList<String>();
	
	public static ArrayList<String> symbol = new ArrayList<String>(Arrays.asList("A","T","G","C","W","S","M","K","R","Y","B","D","H","V","N"));
	public static ArrayList<String> nuc_reps = new ArrayList<String>(Arrays.asList("A", "T", "G", "C", "[AT]", "[CG]", "[AC]", "[GT]", "[AG]", "[CT]", "[CGT]", "[AGT]", "[ACT]", "[ACG]", "[ACGT]"));

	private static List<String> heads;

	private static List<String> seqs;

	//private List<String> PAM_seqs = new ArrayList<String>();
	
//	returns the header of the sequence without the ">"
	public String getHeader()
	{
		header = (header.substring(1).split(" ")[0]);
//	the line above or below do the same thing. Either works for future reference.
//		header = header.replace('>', ' ').strip();
		return header;
	}
	
//	returns the DNA sequence of this FastaParser
	public String getSequence()
	{
		return sequence;
	}

	public String getPAM()
	{	
		return match;
	}
	
	public int getPAMpos()
	{

		return pam_pos;
	}
	
	public FastaParser(String header, String sequence)
	{
		this.header = header;
		this.sequence = sequence;
	}
	
	public void PAMList(String match, int pam_pos)
	{
		this.match = match;
		this.pam_pos = pam_pos;
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
	
	public static String getPAMregex(String pam_seq)
	{
		String pamsite_re = new String();
		pam_seq = pam_seq.toUpperCase();
		
		for(char nuc : pam_seq.toCharArray())
		{
			int i = symbol.indexOf(String.valueOf(nuc));
			//System.out.println(nuc);
			String regex_rep = nuc_reps.get(i);
			pamsite_re = pamsite_re + regex_rep;
		}
		return pamsite_re;
	}
	
	
	
	public static List<String> getPAMs(String pam_reg, String sequence)
	{
		Matcher matcher = Pattern.compile(pam_reg).matcher(sequence);
		
		//System.out.println(pam_reg);
		//System.out.println(sequence);
		
		//MatchResult result = matcher.toMatchResult(); 
		//System.out.println("Current Matcher: " + result);
		
		//if(matcher.find())
		//{
			while(matcher.find())
			{
				pam_matches.add(matcher.group());
				//pam_pos.add(String.valueOf(pam_match.start()+1));
			}

		//}
		//else
		//{
		//	pam_matches.add("NOTHING FOUND");
		//}
		
		return pam_matches;
	}
	

	public static List<String> getPAMpos(String pam_reg, String sequence)
	{
		Matcher matcher = Pattern.compile(pam_reg).matcher(sequence);
		while(matcher.find())
		{
			//matches.add(pam_match.group());
			pos_matches.add(String.valueOf(matcher.start()+1));
		}
		
		return pos_matches;
	}
	
	/*
	public String getPAM(String pam_reg, String seq)
	{
		String pam_match = String.valueOf(Pattern.compile(pam_reg).matcher(seq));
		return pam_match;
	}
	
	public int getPamPos(String pam_reg, String seq)
	{
		Matcher pam_match = Pattern.compile(pam_reg).matcher(seq);
		int pam_pos = 0;
		
		while(pam_match.find())
		{
			pam_pos = pam_match.start();
		}
		return pam_pos;
	}
	*/

	
//	this static list reads in the file and returns a list with the header and sequence  
	public static List<FastaParser> readFastaFile(String filepath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(FastaParser.filepath)));
		
		List<FastaParser> fs = new ArrayList<FastaParser>();
		heads = new ArrayList<String>();
		seqs = new ArrayList<String>();
		
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
					fs.add(new FastaParser(header, sequence.toString()));
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
		heads.add(header);
		seqs.add(sequence.toString());
		fs.add(new FastaParser(header, sequence.toString()));
		reader.close();
		return fs;
	
	}
	
	public static List<String> getHeads(String filepath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(FastaParser.filepath)));
		
		heads = new ArrayList<String>();
		seqs = new ArrayList<String>();
		
		String header = "";
		StringBuffer sequence = new StringBuffer();
		
//		iterating over every line to read in, if the line starts with a ">" then, it moves onto the next if statement
//		the next if statement says if the counter doesn't equal 0, then write out the name, nucleotide %'s, and sequence to the txt file
//		the counter adds 1 each time the for loop iterates.
		for(String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
		{
			if(nextLine.startsWith(">"))
			{
				header = nextLine;
				sequence = new StringBuffer();
			}
			else
			{
				sequence.append(nextLine);
			}
			
		}
		heads.add(header);
		seqs.add(sequence.toString());
		reader.close();
		return heads;
	}
	
	public static List<String> getSeqs(String filepath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(FastaParser.filepath)));
		
		heads = new ArrayList<String>();
		seqs = new ArrayList<String>();
		
		String header = "";
		StringBuffer sequence = new StringBuffer();
		
//		iterating over every line to read in, if the line starts with a ">" then, it moves onto the next if statement
//		the next if statement says if the counter doesn't equal 0, then write out the name, nucleotide %'s, and sequence to the txt file
//		the counter adds 1 each time the for loop iterates.
		for(String nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
		{
			if(nextLine.startsWith(">"))
			{
				header = nextLine;
				sequence = new StringBuffer();
			}
			else
			{
				sequence.append(nextLine);
			}
			
		}
		heads.add(header);
		seqs.add(sequence.toString());
		reader.close();
		return seqs;
	}

	
//	this method writes each unique sequence to the output file with the # of times each sequence was 
//	seen in the input file as the header (sorted with the sequence seen the fewest times the first)
	public static void writeUnique(String inFile, String outFile) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(inFile)));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
		List<FastaParser> fastaList = FastaParser.readFastaFile(filepath);
		HashMap<String, String> fastaMap = new HashMap<String, String>();
		Map<String, Integer> numberMap = new HashMap<String, Integer>();
		
		for(FastaParser fs: fastaList)
		{
			fastaMap.put(fs.getHeader(), fs.getSequence());
		}

		Collection<String> fastaValues = fastaMap.values();

//		for every value in the collection fasta values (the value is the sequence) add into the hashmap numberMap the 
//		sequence (old value) and make the new value the frequency of each sequence. So each sequence that exists in the file has 
		for(String value : fastaValues)
		{
			numberMap.put(value,(Collections.frequency(fastaValues, value)));
		}
	
//		this section of code sorts the numberMap with the frequency of unique sequences by ascending
//		i referenced this website to get this part of the code: https://www.javatpoint.com/how-to-sort-hashmap-by-value
		
		List<Entry<String,Integer>> origList = new LinkedList<Entry<String, Integer>>(numberMap.entrySet());
		
		Collections.sort(origList, new Comparator<Entry<String, Integer>>()
				{
//		it sorts by comparing the first entry's integer value with the second entry's integer value only. 
//		It returns whichever is smaller until no values to compare.
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
					
				});
		
//		creates a new linked hashmap, then iterates over each entry in the list to put them back into a hashmap.
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		
		for(Entry<String, Integer> entry:origList)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
//		print and write the hashmap per entry
		for(String key: sortedMap.keySet())
		{
			writer.write(">" + key + "\n" + sortedMap.get(key) + "\n");
			System.out.println(">" + key + "\n" + sortedMap.get(key) + "\n");
		}
		
		writer.flush(); 
		writer.close();
		reader.close();
	}

}


