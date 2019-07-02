package com.virtusa.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RomanToDecimalConverter {

	private static final Character[] nonRepeatingRomans = {'D', 'L', 'V'};
	private static final Character[] repeatingRomans = {'I','V','X','M'};
	private static Map<Character,Integer> repeatRomans = getRepeatableLiteralsCount();

	private static Map<Character,Integer> getRepeatableLiteralsCount(){
		Map<Character,Integer>map = new HashMap<Character,Integer>() {
			{
				put('I', 0);
				put('X', 0);
				put('C', 0);
				put('M', 0);
			}
		};
		return map;
	}

	private static Map<Character,Integer> NonRepeatableLiteralsCount = getNonRepeatableLiteralsCount();

	private static Map<Character,Integer> getNonRepeatableLiteralsCount(){
		Map<Character,Integer>map = new HashMap<Character,Integer>() {
			{
				put('V', 0);
				put('L', 0);
				put('D', 0);
			}
		};
		return map;
	}

	private static Map<Integer, Integer[]> ROMAN_SUBTRACTABLE_MAPPING = Collections.unmodifiableMap(
			new HashMap<Integer, Integer[]>() {
				{
					put(1, new Integer[] {5, 10});
					put(5, new Integer[] {});
					put(10, new Integer[] {50,100});
					put(50, new Integer[] {});
					put(100, new Integer[] {100,1000});
					put(500, new Integer[] {});
					put(1000, new Integer[] {});
				}
			});

	private static Map<Character, Integer> ROMAN_TO_NUMERIC_MAPPING = Collections.unmodifiableMap(
			new HashMap<Character, Integer>() {
				{
					put('I', 1);
					put('V', 5);
					put('X', 10);
					put('L', 50);
					put('C', 100);
					put('D', 500);
					put('M', 1000);
				}
			});

	/**
	 * checkLiteralCountValidity() method keeps the count of all repeatable and non repeatable Literals.
	 * 
	 */
	public static void checkLiteralCountValidity(Character CurrentLiteral){
		if(checkIfLiteralPresent(nonRepeatingRomans, CurrentLiteral)){
			NonRepeatableLiteralsCount.put(CurrentLiteral, NonRepeatableLiteralsCount.get(CurrentLiteral) + 1);
			if(NonRepeatableLiteralsCount.containsValue(3)){
				System.err.println("Error : Roman Numeral V,L,D cannot be repeated.");	
				System.exit(0);
			}
		}
		else if(checkIfLiteralPresent(repeatingRomans, CurrentLiteral)){
			Character keyForValueContainingThree = getKeyForValueContainingThree();
			if(keyForValueContainingThree != '\0'){
				if (CurrentLiteral.equals(keyForValueContainingThree)){
					System.err.println("Error : Roman Numeral "+CurrentLiteral+" cannot repeat 4 times successively");
					System.exit(0);
				}
				else if(CurrentLiteralSmallerThanPrevious(CurrentLiteral, keyForValueContainingThree)) {
					repeatRomans.put(CurrentLiteral, repeatRomans.get(CurrentLiteral) +1);
					repeatRomans.put(keyForValueContainingThree, 0);
				}
			}
			else{
				repeatRomans.put(CurrentLiteral, repeatRomans.get(CurrentLiteral) +1);
			}
		}
	}

	/**
	 * getKeyForValueContainingThree() method returns the key corresponding to value 3.This is used while checking the count of Literals 
	 * that cannot be repeated more than 3 times.
	 * 
	 */
	private static char getKeyForValueContainingThree(){
		char key = '\0';
		Iterator<Map.Entry<Character,Integer>> iter = repeatRomans.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Character,Integer> entry = iter.next();
			if (entry.getValue().equals(3)) {
				return Character.valueOf(entry.getKey());
			}
		}
		return key;
	}

	/**
	 * checks if currentLiteral is smaller than the previous one. This rule is applied when repeatable literals can
	 * occur 4 times only if the 3rd and 4th occurance has a smaller value between them.
	 * @param CurrentLiteral
	 * @param keyForValueContainingThree
	 * @return
	 */
	private static boolean CurrentLiteralSmallerThanPrevious(char CurrentLiteral, char keyForValueContainingThree){
		if (ROMAN_TO_NUMERIC_MAPPING.get(CurrentLiteral)> ROMAN_TO_NUMERIC_MAPPING.get(keyForValueContainingThree)){
			System.err.println("Error : Should have been a lesser Roman Numeral next instead of "+CurrentLiteral);
			System.exit(0);
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Applies the subtaction logic and checks if the element is subtractable by the other or not.
	 * @param lastDecimal
	 * @param decimal
	 * @param lastNumber
	 * @return
	 */
	public static float subtractionLogic(Float lastDecimal, Float decimal, Float lastNumber){
		if(Arrays.asList(ROMAN_SUBTRACTABLE_MAPPING.get(Math.round(decimal))).contains(Math.round(lastNumber))){
			return lastDecimal - decimal;
		}
		else
			return lastDecimal + decimal;
	}


	/**
	 * Checks if an element is present in an array
	 * @param array
	 * @param literal
	 * @return
	 */
	public static boolean checkIfLiteralPresent(Character[] array, Character literal){
		boolean result = false;
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(literal))
				result =  true;
		}
		return result;
	}

	public static void resetLiteralsCounter(){
		repeatRomans = getRepeatableLiteralsCount();
		NonRepeatableLiteralsCount = getNonRepeatableLiteralsCount();

	}
	
	public float romanToDecimal(java.lang.String romanNumber) {

		float decimal = 0;
		float lastNumber = 0;
		char[] romanNumeral = romanNumber.toUpperCase().toCharArray();

		//Operation to be performed on upper cases even if user enters Roman values in lower case chars
		for (int x = romanNumeral.length- 1; x >= 0 ; x--) {
			Character convertToDecimal = romanNumeral[x];

			switch (convertToDecimal) {
			case 'M':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(1000, lastNumber, decimal);
				lastNumber = 1000;
				break;

			case 'D':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(500, lastNumber, decimal);
				lastNumber = 500;
				break;

			case 'C':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(100, lastNumber, decimal);
				lastNumber = 100;
				break;

			case 'L':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(50, lastNumber, decimal);
				lastNumber = 50;
				break;

			case 'X':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(10, lastNumber, decimal);
				lastNumber = 10;
				break;

			case 'V':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(5, lastNumber, decimal);
				lastNumber = 5;
				break;

			case 'I':
				checkLiteralCountValidity(convertToDecimal);
				decimal = processDecimal(1, lastNumber, decimal);
				lastNumber = 1;
				break;
			}
		}
		resetLiteralsCounter();
		return decimal;
	}

	/**
	 * processDecimal() applied all subtraction logic and returns the result
	 * @param decimal
	 * @param lastNumber
	 * @param lastDecimal
	 * @return
	 */
	public float processDecimal(float decimal, float lastNumber, float lastDecimal) {
		if (lastNumber > decimal) {
			return subtractionLogic(lastDecimal, decimal, lastNumber);
		}
		else {
			return lastDecimal + decimal;
		}
	}
}
