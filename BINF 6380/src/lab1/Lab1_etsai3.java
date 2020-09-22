package lab1;

import java.util.Random;

public class Lab1_etsai3 
{
	static Random random = new Random();
	
	// This method is part of Part1 in Lab1. 
	// It is creating a randomly generated string of 3 nucleotides (AKA 3mer/threemer). 
	// The frequency of getting each nucleotide is 25%, the 4 nucleotides have an equal 
	// chance of getting chosen. It then returns the threemer. 
	public static String getRandomThreemer()
	{
		String threemer = "";
		
		// Iterating three times to generate a random float number and if that float is between
		// 0 and 25, it will concatenate an "A" nucleotide to the string threemer, and so on to 
		// create a string of three nucleotides.
		for(int x=0; x<3; x++)
		{
			float f = random.nextFloat();
			//System.out.println(f);
			
			if(f>0 && f <= 0.25)
			{
				threemer += "A";
			}
			else if(f>0.25 && f<=0.50)
			{
				threemer += "C";
			}
			else if(f>0.5 && f<=0.75)
			{
				threemer += "G";
			}
			else
			{
				threemer += "T";
			}
			//System.out.println(threemer);
			//System.out.println(f);
		}
		return threemer; 
	}
	
	// This method is part of Part3 in Lab1.
	// It is doing the same thing as the method getRandomThreemer. But instead of the
	// chances being equal among each nucleotide the probability of A is 12%, C is 38%, G is 39%,
	// and T is 11%. It iterates three times over to concatenate to the string to return 
	// a string of three nucleotides.
	public static String randomThreeUneven()
	{
		String threemer = "";
		
		for(int x=0; x<3; x++)
		{
			float f = random.nextFloat();
			//System.out.println(f);
			
			if(f>0 && f <= 0.12)
			{
				threemer += "A";
			}
			else if(f>0.12 && f<=0.50)
			{
				threemer += "C";
			}
			else if(f>0.50 && f<=0.89)
			{
				threemer += "G";
			}
			else
			{
				threemer += "T";
			}
			//System.out.println(threemer);
			//System.out.println(f);
		}
		return threemer; 
	}
	
	
	public static void main(String[] args)
	{	
		// this is the number of iterations or threemers that will be generated
		int num_of_iter = 10000;
		
		// -------------------------------------------PART 2--------------------------------------------- //
		// this is the AAA threemer counter for parts 1 and 2 of lab1
		int aaa_counter1 = 0;
		
		// the for loop iterates a certain amount of times (AKA: num_of_iter) to create 
		// that many threemers, in this case we randomly generated 10K threemers (total of 30K 
		// individual nucleotides). We then count how many of those 10K threemers were AAA
		// and print that out. To get the frequency we divide by num_of_iter (in this case 10000).
		for(int x=0; x<num_of_iter; x++)
		{
			String s = getRandomThreemer();
			if(s.contentEquals("AAA"))
			{
				aaa_counter1 += 1;
			}
			//System.out.print(s);
		}
		
		// to get the frequency of AAA, i did the number of AAA's counted divided by the total number of
		// threemers randomly generated/how many times it iterated over the getRandomThreemer method.
		float freq_of_aaa = (float)aaa_counter1/num_of_iter;
		
		System.out.println("\nThe number of AAA's randomly generated (equal chance of each nucleotide 25%): \n" + aaa_counter1);
		System.out.println("\nThe number of total threemers that were randomly generated: " + num_of_iter);
		System.out.println("\nFrequency of only AAA's (should be close to (.25)^3 or 1.5625%): \n" + freq_of_aaa);
		System.out.println("-------------------------------------------------------------------------------------");
		// I expect that the frequency of AAA should be (0.25)*(0.25)*(0.25) or (0.25)^3. Which equals 0.015625 or 1.5625%. 
		// Java's number is close to this number, I'm sure if I made the number of iterations over the threemer method larger,
		// it would get closer to the actual frequency. 
		
		// ---------------------------------------------PART 3-------------------------------------------- //
		// this is the AAA threemer counter for part 3
		int aaa_counter2 = 0;
		
		// this for loop does the same thing as it does in part 1&2, but it uses
		// different nucleotide probabilities as seen in the method randomThreeUneven.
		// 
		for(int y=0; y<num_of_iter; y++)
		{
			String s2 = randomThreeUneven();
			if(s2.contentEquals("AAA"))
			{
				aaa_counter2 += 1;
			}
			//System.out.print(s2);
		}
		float freq_of_aaa2 = (float)aaa_counter2/num_of_iter;
		
		System.out.println("\nThe number of AAA's randomly generated (A=12%, C=38%, G=39%, T=11%): \n" + aaa_counter2);
		System.out.println("\nThe number of total threemers that were randomly generated: " + num_of_iter);
		System.out.println("\nFrequency of only AAA's (should be close to (.12)^3 or 0.1728%): \n" + freq_of_aaa2);
		// I expect that the frequency of AAA should be (0.12)*(0.12)*(0.12) or (0.12)^3. Which equals to 0.001728 or 0.1728%. 
		// Java's number is close to this number, again if I made the number of iterations over the threemer method larger,
		// it would get closer to the actual frequency. 
	}
}
