package lab2;

import java.util.Random;

public class Lab2_etsai3 
{
	private static final Random RANDOM = new Random(); 
	
	
	public static String[] SHORT_NAMES = 
	{"A","R", "N", "D", "C", "Q", "E", 
		"G",  "H", "I", "L", "K", "M", "F", 
		"P", "S", "T", "W", "Y", "V" };

	public static String[] FULL_NAMES = 
	{"alanine","arginine", "asparagine", 
		"aspartic acid", "cysteine",
		"glutamine",  "glutamic acid",
		"glycine" ,"histidine","isoleucine",
		"leucine",  "lysine", "methionine", 
		"phenylalanine", "proline", 
		"serine","threonine","tryptophan", 
		"tyrosine", "valine"};
	
	public static void quiz_check(String[] letter_in, String [] amino_acid) throws Exception
	{
		if(letter_in.length != amino_acid.length)
		{
			throw new Exception("These arrays must be the same length! ");
		}
		
		System.out.println("\nStarting Amino Acid Quiz... Answer correctly as fast as you can! \nOne wrong answer ends the game. \nYou have 30 seconds... ");
		int total_score = 0;
		long start = System.currentTimeMillis();
		long time_allotted = (long) 30.0;
		
		while (true)
		{
			int random_number = RANDOM.nextInt(20);
			System.out.println("\nWhat is the one letter code for this amino acid: " + amino_acid[random_number]);
			String user_guess = System.console().readLine().toUpperCase();
			String answer = letter_in[random_number];
			
			if(user_guess.equals(answer))
			{
				total_score ++;
				float time_left = (System.currentTimeMillis() - start)/ 1000f;
				System.out.println("\nCorrect! ");
				System.out.println("> " + time_left + " seconds has elasped...");
				
				if(time_left < time_allotted)
				{
					continue;
				}
				else
				{
					System.out.println("\nTimes up! \nYour Final Score: " + total_score + "\n");
					break;
				}

			}
			else
			{
				System.out.println("\nIncorrect! The correct answer is... " + answer + "\n\nFinal Score = " + total_score + "\n");
				break;
			}

		}
	
	}
	
	public static void main(String[] args) throws Exception
	{
		quiz_check(SHORT_NAMES, FULL_NAMES);
	}
}
