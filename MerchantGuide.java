package com.virtusa.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MerchantGuide{

	static Map<String, String> romanValuesMap = new HashMap<String, String>();
	static Map<String, Float> integerValueMap = new HashMap<String, Float>();
	static Map<String, String> outputValueMap = new HashMap<String, String>(); 
	static ArrayList<String> missingFieldsMap = new ArrayList<String>(); 
	static Map<String, Float> elementsMap = new HashMap<String, Float>();

	public static List<String> processInputString(List<String> inputString){
		for(String input : inputString) {
			String inputStringSplit[] = input.split("((?<=:)|(?=:))|( )");

			if (input.endsWith("?")){
				outputValueMap.put(input,"");
			}
			else if (inputStringSplit.length == 3 && inputStringSplit[1].equalsIgnoreCase("is")){
				romanValuesMap.put(inputStringSplit[0], inputStringSplit[inputStringSplit.length-1]);
			}
			else if(input.toLowerCase().endsWith("credits")){
				missingFieldsMap.add(input);
			}
		}
		
		List<String> outputData = new ArrayList();
		String outputValue = null;
		for (Map.Entry<String, String> entry : outputValueMap.entrySet()) {
			outputValue = checkForString(entry.getKey());
			if(outputValue != null) outputData.add(outputValue);
		}
		return outputData;
	}
	public static void MapTokentoIntegerValue(){

		Iterator it = romanValuesMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry token = (Map.Entry)it.next();
			float integerValue = new RomanToDecimalConverter().romanToDecimal(token.getValue().toString());
			integerValueMap.put(token.getKey().toString(), integerValue);
		}
		mapMissingEntities();
	}

	/**
	 * FInds the value of elements by decoding queries like [glob glob Silver is 34 Credits]
	 */
	private static void mapMissingEntities(){
		for (int i = 0; i < missingFieldsMap.size(); i++) {
			checkForMissingData(missingFieldsMap.get(i));
		}
	}

		private static void checkForMissingData(String query){
		String array[] = query.split("((?<=:)|(?=:))|( )");
		int splitIndex = 0;
		int creditValue = 0; String element= null; String[] valueofElement = null;
		for (int i = 0; i < array.length; i++) {
			if(array[i].toLowerCase().equals("credits")){
				creditValue = Integer.parseInt(array[i-1]);
			}
			if(array[i].toLowerCase().equals("is")){
				splitIndex = i-1;
				element = array[i-1];
			}
			valueofElement = java.util.Arrays.copyOfRange(array, 0, splitIndex);
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (int j = 0; j < valueofElement.length; j++) {
			stringBuilder.append(romanValuesMap.get(valueofElement[j]));
		}
		float valueOfElementInDecimal = new RomanToDecimalConverter().romanToDecimal(stringBuilder.toString());
		elementsMap.put(element, creditValue/valueOfElementInDecimal);
	}
	
		
		
		private static String  checkForString(String query){
			if (query.toLowerCase().startsWith("how much")){
				return checkForRoman(query);
			}
			else if (query.toLowerCase().startsWith("how many")){
				return checkValueForDecimal(query);
			}
			return null;
		}
		
		public static String checkForRoman(String query){
			if (isValidinput(query)== true){
				ArrayList<String> tokenValueToRoman = new ArrayList<String>();
				ArrayList<String> tokenValue = splitData(query);
				for (int i = 0; i < tokenValue.size(); i++) {
					tokenValueToRoman.add(romanValuesMap.get(tokenValue.get(i)));
				}
				float value = new RomanToDecimalConverter().romanToDecimal(tokenValueToRoman.toString());
				tokenValue.add("is");tokenValue.add(Float.toString(value));
				return query+" "+formatOutput(tokenValue);
			}
			else{
				return query+" : I have no idea what you are talking about";
			}
		}
		
		private static String checkValueForDecimal(String query){
			if (isValidinput(query) == true){
				ArrayList<String> tokenValue = splitData(query);
				ArrayList<String> tokenValueToRoman = new ArrayList<String>();
				String element = null;
				for (int i = 0; i < tokenValue.size(); i++) {
					if(romanValuesMap.get(tokenValue.get(i)) != null){
						tokenValueToRoman.add(romanValuesMap.get(tokenValue.get(i)));
					}
					else if (elementsMap.get(tokenValue.get(i)) != null){
						element = tokenValue.get(i);
					}
					else{
						System.err.println(query+" : I have no idea what you are talking about");
					}
				}
				float elementValue = (new RomanToDecimalConverter().romanToDecimal(tokenValueToRoman.toString()) * elementsMap.get(element));
				tokenValue.add("is");tokenValue.add(Float.toString(elementValue));tokenValue.add("Credits");
				return query+" "+formatOutput(tokenValue);
			}
			else{
				return query+" : I have no idea what you are talking about";
			}
		}
		
		private static boolean isValidinput(String query){
			Pattern regex = Pattern.compile("[$&+,:;=@#|]");
			Matcher matcher = regex.matcher(query);
			if (matcher.find()){
				return false;
			}
			else{
				return true;
			}

		}
		
		private static ArrayList<String> splitData(String query){
			ArrayList<String> queryArray = new ArrayList<String>(Arrays.asList(query.split("((?<=:)|(?=:))|( )")));
			int startIndex = 0, endIndex = 0;
			for (int i = 0; i < queryArray.size(); i++) {
				if(queryArray.get(i).toLowerCase().equals("is")){
					startIndex = i+1;
				}
				else if(queryArray.get(i).toLowerCase().equals("?")){
					endIndex = i;

				}
			}
			String[] array = queryArray.toArray(new String[queryArray.size()]);
			return new ArrayList<String>(Arrays.asList(java.util.Arrays.copyOfRange(array, startIndex, endIndex)));

		}
		
		private static String formatOutput(ArrayList<String> output){
			return output.toString().replace(",", "").replace("[", "").replace("]", "");
		}
	

}