package globallogic;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

public class LogicV2 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean validAnswer = false;
		
		String response1 = "";
		String response2 = "";
		
		String pattern = "";
		String sentence = "";
		
		while(!validAnswer) {
			System.out.print("Do you want to read from file? (yes/no) ");
			response1 = sc.nextLine().toLowerCase();
			if(response1.equals("yes")) {
				validAnswer = true;
				System.out.print("Enter input file name: ");
				String inputFileName = sc.nextLine();
				try {
					File inputFile = new File(inputFileName);
					Scanner fileReader = new Scanner(inputFile);
					
					pattern = fileReader.nextLine();
					sentence = fileReader.nextLine();
					fileReader.close();
				} catch(FileNotFoundException e) {
					System.out.println(e.getMessage());		
				}
				
			} else if(response1.equals("no")) {
				validAnswer = true;
				System.out.print("Enter pattern: ");
				pattern = sc.nextLine();
				
				System.out.print("Enter sentence: ");
				sentence = sc.nextLine();
			} else {
				System.out.println("Please put yes or no");
			}
		}
		
		validAnswer = false;
		
		String outputFileName = "";
		while(!validAnswer) {
			System.out.print("Do you want to write in the file? (yes/no) ");
			response2 = sc.nextLine().toLowerCase();
			if(response2.equals("yes")) {
				validAnswer = true;
				System.out.print("Enter output file name: ");
				outputFileName = sc.nextLine();
				
			} else if(response2.equals("no")) {
				validAnswer = true;
			} else {
				System.out.println("Please put yes or no");
			}
		}
				
		String[] wordsInSentence = sentence.split(" ");
			
		Map<Map<String, Integer>, Integer> resultMap = new HashMap<Map<String,Integer>, Integer>();
			
		int patternCharacters = countPatternCharacters(wordsInSentence, pattern);
			
		int totalCharacters =  countTotalCharacters(wordsInSentence);
			
		if(totalCharacters == 0) {
			System.out.println("No valid characters in the sentence");
		} else {
			
			for(int i = 0; i < wordsInSentence.length; i++) {
				if(wordsInSentence[i].trim().equals("")) continue;
				updateMap(wordsInSentence[i], resultMap, pattern);
			}
				
				
			List<Map.Entry<Map<String, Integer>, Integer>> list = new ArrayList<Map.Entry<Map<String, Integer>, Integer>>(resultMap.entrySet());
			Collections.sort(list, new ValueThenKeyComparator<Map<String, Integer>, Integer>());
			
			
			printResult(list, patternCharacters, totalCharacters, outputFileName);
		}
		
		
		sc.close();
		
	}

	

	private static void printResult(List<Entry<Map<String, Integer>, Integer>> list, int patternCharacters,int totalCharacters, String outputFileName) {
		
		for(int i = 0; i < list.size(); i++) {
			String partOfWord = "";
			int lengthOfWord = 0;
			for(Map.Entry<String, Integer> entry : list.get(i).getKey().entrySet()) {
				partOfWord = entry.getKey();
				lengthOfWord = entry.getValue(); 
			}
			String line = "{ (" + partOfWord + "), " + lengthOfWord + "} = " + (double) Math.round(list.get(i).getValue()*1.0/patternCharacters * 100) / 100 +" (" + list.get(i).getValue() + "/" + patternCharacters + ")";
			if(outputFileName.equals("")) {
				System.out.println(line);
			} else {
				FileWriter output;
				try {
					output = new FileWriter(outputFileName, true);
					output.write(line + "\n");
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}
		}
		
		String finalLine = "TOTAL Frequency: " + (double) Math.round(patternCharacters*1.0/totalCharacters * 100) / 100 + " (" + patternCharacters + "/" + totalCharacters + ")";
		
		if(outputFileName.equals("")) {
			System.out.println(finalLine);
		} else {
			FileWriter output;
			try {
				output = new FileWriter(outputFileName, true);
				output.write(finalLine + "\n");
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}



	private static void updateMap(String word, Map<Map<String, Integer>, Integer> resultMap, String pattern) {
		Set<String> set = new LinkedHashSet<String>();
		word = word.toLowerCase();
		int patternCount = 0;
		
		for(int i = 0; i < word.length(); i++) {
			if(pattern.contains(String.valueOf(word.charAt(i))) && !set.contains(String.valueOf(word.charAt(i)))) {
				set.add(String.valueOf(word.charAt(i)));
				patternCount++;
			} else if(pattern.contains(String.valueOf(word.charAt(i)))) {
				patternCount++;
			}
		}
		
		final int patternCountFinal = patternCount;
		
		if(set.isEmpty()) {
			return;
		}
		
		reorderSet(set, pattern);
		
		Map<String, Integer> helpMap = new HashMap<String, Integer>();
		helpMap.put(set.toString(), countTotalCharactersInWord(word));
		
		if(!resultMap.containsKey(helpMap)) {
			resultMap.put(helpMap, patternCount);
		} else {
			resultMap.compute(helpMap, (key, val) -> val + patternCountFinal);
		}
		
		
	}



	private static void reorderSet(Set<String> set, String pattern) {
		Set<String> reorderedSet = new LinkedHashSet<String>();
		
		for(int i = 0; i < pattern.length(); i++) {
			if(set.contains(String.valueOf(pattern.charAt(i)))) {
				reorderedSet.add(String.valueOf(pattern.charAt(i)));
				//System.out.println(reorderedSet);
			}
		}
		
		set.removeAll(set);
		set.addAll(reorderedSet);
	}



	private static int countTotalCharactersInWord(String word) {
		word = word.trim();
		String invalidCharacters = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		int count = 0;
		for(int i = 0; i < word.length(); i++) {
			if(!invalidCharacters.contains(String.valueOf(word.charAt(i)))) {
				count++;
			}
		}
		
		return count;
	}


	private static int countPatternCharactersInWord(String word, String pattern) {
		int patternCount = 0;
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++) {
			if(pattern.contains(String.valueOf(word.charAt(i)))) {
				patternCount++;
			}
		}
		
		return patternCount;
	}
	
	
	private static int countPatternCharacters(String[] wordsInSentence,String pattern) {
		int totalPatternCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			totalPatternCount += countPatternCharactersInWord(wordsInSentence[i], pattern);
		}
		return totalPatternCount;
	}

	
	private static int countTotalCharacters(String[] wordsInSentence) {
		int totalCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			 if(wordsInSentence[i].trim().equals("")) continue;
			 totalCount += countTotalCharactersInWord(wordsInSentence[i]);
		}
		
		return totalCount;
	}
}



