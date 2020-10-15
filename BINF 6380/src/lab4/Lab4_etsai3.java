package lab4;

import java.util.List;

public class Lab4_etsai3 
{
	private static final String filepath = "/Users/erintsigh/git/UNCC-BINF-Work/BINF 6380/src/practice.fasta";
	
	public static void main(String[] args) throws Exception
	{
		List<FastaSequence> fastaList = FastaSequence.readFastaFile(filepath);
		
		for(FastaSequence fs_list: fastaList)
		{
			System.out.println(fs_list.getHeader());
			System.out.println(fs_list.getSequence());
			System.out.println(fs_list.getGCRatio());
		}
		//FastaSequence.writeUnique(filepath);
	}
	
}
