package globallogic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


public class Logic {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		
		System.out.print("Enter sentence: ");
		String sentence = sc.nextLine();
		
		String[] wordsInSentence = sentence.split(" ");
		
		Map<Map<String, Integer>, Integer> resultMap = new HashMap<Map<String,Integer>, Integer>();
		
		int logicCharacters = countLogicCharacters(wordsInSentence);
		
		int totalCharacters =  countTotalCharacters(wordsInSentence);
		
		if(totalCharacters == 0) {
			System.out.println("No valid characters in this sentence");
		} else {
			for(int i = 0; i < wordsInSentence.length; i++) {
				if(wordsInSentence[i].trim().equals("")) continue;
				updateMap(wordsInSentence[i], resultMap);
			}
			
			
			List<Map.Entry<Map<String, Integer>, Integer>> list = new ArrayList<Map.Entry<Map<String, Integer>, Integer>>(resultMap.entrySet());
			Collections.sort(list, new ValueThenKeyComparator<Map<String, Integer>, Integer>());
			
			printResult(list, logicCharacters, totalCharacters);
		}
		
		sc.close();
		
	}

	

	private static void printResult(List<Entry<Map<String, Integer>, Integer>> list, int logicCharacters,int totalCharacters) {
		
		for(int i = 0; i < list.size(); i++) {
			String partOfWord = "";
			int lengthOfWord = 0;
			for(Map.Entry<String, Integer> entry : list.get(i).getKey().entrySet()) {
				partOfWord = entry.getKey();
				lengthOfWord = entry.getValue(); 
			}
			System.out.println("{ (" + partOfWord + "), " + lengthOfWord + "} = " + (double) Math.round(list.get(i).getValue()*1.0/logicCharacters * 100) / 100 +" (" + list.get(i).getValue() + "/" + logicCharacters + ")");
		}
		
		System.out.println("TOTAL Frequency: " + (double) Math.round(logicCharacters*1.0/totalCharacters * 100) / 100 + " (" + logicCharacters + "/" + totalCharacters + ")");
	}



	private static void updateMap(String word, Map<Map<String, Integer>, Integer> resultMap) {
		Set<String> set = new LinkedHashSet<String>();
		String logic = "logic";
		word = word.toLowerCase();
		int logicCount = 0;
		
		for(int i = 0; i < word.length(); i++) {
			if(logic.contains(String.valueOf(word.charAt(i))) && !set.contains(String.valueOf(word.charAt(i)))) {
				set.add(String.valueOf(word.charAt(i)));
				logicCount++;
			} else if(logic.contains(String.valueOf(word.charAt(i)))) {
				logicCount++;
			}
		}
		
		final int logicCountFinal = logicCount;
		
		if(set.isEmpty()) {
			return;
		}
		
		reorderSet(set);
		
		Map<String, Integer> helpMap = new HashMap<String, Integer>();
		helpMap.put(set.toString(), countTotalCharactersInWord(word));
		
		if(!resultMap.containsKey(helpMap)) {
			resultMap.put(helpMap, logicCount);
		} else {
			resultMap.compute(helpMap, (key, val) -> val + logicCountFinal);
		}
		
		
	}



	private static void reorderSet(Set<String> set) {
		Set<String> reorderedSet = new LinkedHashSet<String>();
		
		String logic = "logic";
		
		for(int i = 0; i < logic.length(); i++) {
			if(set.contains(String.valueOf(logic.charAt(i)))) {
				reorderedSet.add(String.valueOf(logic.charAt(i)));
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


	private static int countLogicCharactersInWord(String word) {
		String logic = "logic";
		int logicCount = 0;
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++) {
			if(logic.contains(String.valueOf(word.charAt(i)))) {
				logicCount++;
			}
		}
		
		return logicCount;
	}
	
	
	private static int countLogicCharacters(String[] wordsInSentence) {
		int totalLogicCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			totalLogicCount += countLogicCharactersInWord(wordsInSentence[i]);
		}
		return totalLogicCount;
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
