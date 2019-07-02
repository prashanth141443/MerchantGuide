package com.virtusa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class MerchantGuideTest {

	@Test
	void testProcessInputString() {
		List<String> input = new ArrayList();
		input.add("glob is I");
		input.add("prok is V");
		input.add("pish is X");
		input.add("tegj is L");
		input.add("glob glob Silver is 34 Credits");
		input.add("glob prok Gold is 57800 Credits");
		input.add("pish pish Iron is 3910 Credits");
		input.add("how much is pish tegj glob glob ?");
		input.add("how many Credits is glob prok Silver ?");
		input.add("how many Credits is glob prok Gold ?");
		input.add("how many Credits is glob prok Iron ?");
		input.add("how much wood could a woodchuck chuck if a woodchuck could chuck= wood ?");
		
		MerchantGuide merchantGuide = new MerchantGuide();
		List<String> expectedOutput = new ArrayList();
		expectedOutput.add("pish tegj glob glob is 42");
		expectedOutput.add("glob prok Silver is 68 Credits");
		expectedOutput.add("glob prok Gold is 57800 Credits");
		expectedOutput.add("glob prok Iron is 782 Credits");
		expectedOutput.add("I have no idea what you are talking about");
		assertEquals(expectedOutput, merchantGuide.processInputString(input));
	}

}
