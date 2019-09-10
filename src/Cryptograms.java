/**
 * 
 * @author Yongqi Jia
 * 
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Cryptograms {
	public static void main(String[] args) {
		try {
            Scanner file = new Scanner(System.in);
            String filename = args[0];
            Scanner input = new Scanner(new File(filename));
            ArrayList<String> quotes_array = new ArrayList<>();
            while (input.hasNextLine()) {
                String quotes = input.nextLine();
                quotes_array.add(quotes);
            }
            
            Random rand = new Random();
            int n = rand.nextInt(quotes_array.size());
            String target = new String();
            target = quotes_array.get(n).toUpperCase();
            // select a random quote to be the puzzle
            
            HashMap<String, String> cryptogram = new HashMap<String, String>();
            String encrypted_quotes = new String();
            cryptogram = random_key();
            encrypted_quotes = encryption(cryptogram, target);
            decryption(encrypted_quotes, cryptogram, target);

            input.close();
            file.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
	}
	
	// decryption is using additional hashmap to store the user guesses and mapping to their hashmap
	// and show them their progress by using the hashmap to replace the encrypted characters with their
	// guess ones.
	private static void decryption(String encrypted_quotes, HashMap<String, String> cryptogram, String target) {
		HashMap<String, String> user_guess = new HashMap<String, String>();
		for(String letter : cryptogram.keySet() ) {
			user_guess.put(letter, " ");
		}
		// Create an additional hashmap that maps characters to characters which is used to store the 
		// user guesses.
		
		ArrayList<String> guess_str = new ArrayList<String>();
		for(int i = 0; i < encrypted_quotes.length(); i++) {
			if(Pattern.matches("\\p{Punct}", Character.toString(encrypted_quotes.charAt(i)))) {
				guess_str.add(i, Character.toString(encrypted_quotes.charAt(i)));
			}else {
				guess_str.add(i, " ");
			}
		}
		// Create a string to store every guess and show the progress.
		
		Boolean check = true;
		while(check) {
			System.out.println(encrypted_quotes);
			Scanner ask1 = new Scanner(System.in);
		    System.out.print("Enter the letter to replace: ");
		    String replace = ask1.nextLine().toUpperCase();
		    Scanner ask2 = new Scanner(System.in);
		    System.out.print("Enter its replacement: ");
		    String replacement = ask2.nextLine().toUpperCase();
		    // Ask user to input the replace letter and replacement.
		    
		    for(String letter : user_guess.keySet()) {
		    	if(letter == replace) {
		    		user_guess.put(letter, replacement);
		    	}
		    }
		    for(int i = 0; i < encrypted_quotes.length(); i++) {
		    	if(Character.toString(encrypted_quotes.charAt(i)).equals(replace)) {
		    		guess_str.set(i, replacement);
		    	}
		    }
		    // Update the hashmap which is used to stroe the user guesses.
		    
		    for(String letter : user_guess.keySet()) {
		    	if(letter.equals(replacement)) {
		    		user_guess.put(letter, replace);
		    		break;
		    	}
		    }
		    System.out.println();
		    
		    String temp = "";
		    for(int i = 0; i < guess_str.size(); i++) {
		    	temp += guess_str.get(i);
		    	System.out.print(guess_str.get(i));
		    }
		    System.out.println();  

		    if(temp.equals(target)) {
		    	check = false;
		    } 
		    // check whether user complete the decryption.
		    
		}
		System.out.println(encrypted_quotes);
		System.out.println();
		System.out.println("You got it!");
		
	}

	// Encrypt the selected quotes
	private static String encryption(HashMap<String, String> cryptogram, String target) {
		String encrypted = new String();
		for(int i = 0; i < target.length(); i++) {
			if(target.charAt(i) == ' ') {
				encrypted += ' ';
			}else if(Pattern.matches("\\p{Punct}", Character.toString(target.charAt(i)))){
				encrypted += Character.toString(target.charAt(i));
			}else {
				encrypted += cryptogram.get(Character.toString(target.charAt(i)));
			}
		}
		return encrypted;
	}
	
	// Create the random encryption key and store in a hashmap.
	private static HashMap<String, String> random_key() {
		List<String> alpha = new ArrayList<String>(); 
		List<String> temp = new ArrayList<String>();
		for(int i = 0; i < 26; i++){
			temp.add(Character.toString((char)(65 + i)));
			alpha.add(Character.toString((char)(65 + i)));
		}
		HashMap<String, String> substitution_cypher = new HashMap<String, String>();
		Collections.shuffle(alpha);
		for(int i = 0; i < temp.size(); i++) {
			substitution_cypher.put(temp.get(i), alpha.get(i));
		}	
		return substitution_cypher;	
	}
}