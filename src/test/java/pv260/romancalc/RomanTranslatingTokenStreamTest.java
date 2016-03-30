/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv260.romancalc;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import pv260.romancalc.framework.RomanTranslator;
import pv260.romancalc.framework.TokenStream;
import pv260.romancalc.implementation.IteratorTokenStream;
import pv260.romancalc.implementation.RomanTranslatingTokenStream;

public class RomanTranslatingTokenStreamTest {

	/**
	 * translatorSpy is a Test Spy
	 * We are testing that the translatingStream calls the translator internally,
	 * but only when it comes across a Roman numeral
	 * ("-" and "abc" tokens are ignored)
	 */
	@Test
	public void testRecognizesRomanNumeral_Mockito() {
		RomanTranslator translatorSpy = mock(RomanTranslator.class);
		TokenStream backingStream = new IteratorTokenStream(asList("IX", "-", "L", "abc"));
		TokenStream translatingStream = new RomanTranslatingTokenStream(backingStream, translatorSpy);
		translatingStream.next();
		translatingStream.next();
		translatingStream.next();
		translatingStream.next();
		verify(translatorSpy).toDecimal("IX");
		verify(translatorSpy).toDecimal("L");
	}

	@Test
	public void testRecognizesRomanNumeral_Manual() {
		//we have to declare the most specific type here
		//so that we can call the .getAllCalls() of the spy
		RomanTranslatorSpy translatorSpy = new RomanTranslatorSpy();
		TokenStream backingStream = new IteratorTokenStream(asList("IX", "-", "L", "abc"));
		TokenStream translatingStream = new RomanTranslatingTokenStream(backingStream, translatorSpy);
		translatingStream.next();
		translatingStream.next();
		translatingStream.next();
		translatingStream.next();
		List<String> calls = translatorSpy.getAllCalls();
		assertEquals(2, calls.size());
		assertTrue(calls.contains("IX"));
		assertTrue(calls.contains("L"));
	}

	private static class RomanTranslatorSpy implements RomanTranslator {

		private List<String> calledFor = new ArrayList<>();

		@Override
		public int toDecimal(String romanNumeral) {
			calledFor.add(romanNumeral);
			return 0;
		}

		@Override
		public String toRoman(int decimalNumber) {
			throw new UnsupportedOperationException();
		}

		public List<String> getAllCalls() {
			return Collections.unmodifiableList(calledFor);
		}

	}

	/**
	 * translatorStub is a Test Stub, specifically a Responder
	 * We are testing that the translatingStream correctly
	 * uses output of the translator
	 */
	@Test
	public void testConvertsToDecimalTokens_Mockito() {
		RomanTranslator translatorStub = mock(RomanTranslator.class);
		when(translatorStub.toDecimal("IX")).thenReturn(9);
		when(translatorStub.toDecimal("L")).thenReturn(50);
		TokenStream backingStream = new IteratorTokenStream(asList("IX", "-", "L"));
		TokenStream translatingStream = new RomanTranslatingTokenStream(backingStream, translatorStub);
		assertEquals("9", translatingStream.next());
		assertEquals("-", translatingStream.next());
		assertEquals("50", translatingStream.next());
		assertFalse(translatingStream.hasNext());
	}

	@Test
	public void testConvertsToDecimalTokens_Manual() {
		Map<String, Integer> translations = new HashMap<>();
		translations.put("IX", 9);
		translations.put("L", 50);
		RomanTranslator translatorStub = new RomanTranslator() {

			private Map<String, Integer> translations;

			@Override
			public int toDecimal(String romanNumeral) {
				return translations.get(romanNumeral);
			}

			@Override
			public String toRoman(int decimalNumber) {
				throw new UnsupportedOperationException();
			}

			//we cannot access constructor of an annonymous inner type
			//also we can not alter signatures of the methods called
			//so to pass in the test specific translations we need to use this inject technique
			//another solution would be to turning this class into a
			//private static class RomanTranslatorStub
			//as seen in one of earlier examples
			public RomanTranslator inject(Map<String, Integer> translations) {
				this.translations = translations;
				return this;
			}
		}.inject(translations);
		TokenStream backingStream = new IteratorTokenStream(asList("IX", "-", "L"));
		TokenStream translatingStream = new RomanTranslatingTokenStream(backingStream, translatorStub);
		assertEquals("9", translatingStream.next());
		assertEquals("-", translatingStream.next());
		assertEquals("50", translatingStream.next());
		assertFalse(translatingStream.hasNext());
	}

}
