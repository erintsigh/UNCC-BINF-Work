package final_project;

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
import final_project.FastaParser;
import final_project.PamParser;

public class PamParser {
	
	public static ArrayList<String> symbol = new ArrayList<String>(Arrays.asList("A","T","G","C","W","S","M","K","R","Y","B","D","H","V","N"));
	public static ArrayList<String> nuc_reps = new ArrayList<String>(Arrays.asList("A", "T", "G", "C", "[AT]", "[CG]", "[AC]", "[GT]", "[AG]", "[CT]", "[CGT]", "[AGT]", "[ACT]", "[ACG]", "[ACGT]"));
	
	public String pam_match;
	public int pam_pos;
	public String pam_reg;
	
	//private List<PamParser> pam_group;
	
	public PamParser(String pam_match, int pam_pos)
	{
		this.pam_match = pam_match;
		this.pam_pos = pam_pos;
	}
	
	public String getPAM()
	{	
		return pam_match;
	}
	
	public int getPAMpos()
	{
		return pam_pos;
	}
	
	public static String getPAMregex(String pam_seq)
	{
		String pamsite_re = new String();
		
		for(char nuc : pam_seq.toCharArray())
		{
			int i = symbol.indexOf(String.valueOf(nuc));
			//System.out.println(nuc);
			String regex_rep = nuc_reps.get(i);
			pamsite_re = pamsite_re + regex_rep;
		}
		return pamsite_re;
	}
	
	public static List<PamParser> getPAMs(String pam_regex, String seq) throws Exception
	{
		List<PamParser> pam_group = new ArrayList<PamParser>();
		List<String> listMatches = new ArrayList<String>();
		List<Integer> posMatches = new ArrayList<Integer>();
		
		Matcher pam_match = Pattern.compile(pam_regex).matcher(seq);
	
		while(pam_match.find()) 
		{
			listMatches.add(pam_match.group());
			posMatches.add(pam_match.start());
		}
		
		for(String match : listMatches)
		{
			for(int pam_pos : posMatches)
			{
				pam_group.add(new PamParser(match, pam_pos));
			}
			
		}
		//pam_group.add(new PamParser(match, pam_pos);
		
		return pam_group;
	}

}
