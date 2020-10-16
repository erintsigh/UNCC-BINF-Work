package lab4;

import java.util.List;

public class Lab4_etsai3 
{
	private static final String filepath = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/practice.fasta";
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> fastaList = FastaSequence.readFastaFile(filepath);
		
		for(FastaSequence fs: fastaList)
		{
			System.out.println(fs.getHeader());
			System.out.println(fs.getSequence());
			System.out.println("GC content: " + fs.getGCRatio());
			System.out.println("\n");
		}
		FastaSequence.writeUnique(filepath,"/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/unique_fastas.txt");
	}
	
}
