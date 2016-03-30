/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv260.romancalc;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import pv260.romancalc.framework.*;
import pv260.romancalc.implementation.IteratorTokenStream;
import pv260.romancalc.implementation.RomanCalculatorImpl;
import pv260.romancalc.implementation.SimpleLexer;
import pv260.romancalc.implementation.equation.NodeMinus;
import pv260.romancalc.implementation.equation.NodeNumber;

public class RomanCalculatorTest {

	/**
	 * Here we are using the builder both to verify that
	 * it is correctly used (expectedTokensStream)
	 * and to provide stubbed test specific input
	 * This way the execution is executed completely with the
	 * same result as if builder were a real object:
	 * it provides data for further execution with valid input
	 * it fails if input data is invalid
	 *
	 * Almost the whole test consists of test doubles.
	 * As we implement more of the classes, we might want to
	 * replace the doubles with real objects in the test case
	 * to verify that it still works. In doing so the test
	 * becomes less of a unit test and more of an integration test
	 */
	@Test
	public void testCorrectInputSingleOperator() throws NoInputException {
		Lexer lexer = new SimpleLexer();
		RomanTranslator translator = mock(RomanTranslator.class);
		when(translator.toDecimal("IX")).thenReturn(9);
		when(translator.toDecimal("V")).thenReturn(5);
		when(translator.toRoman(4)).thenReturn("IV");
		TokenStream expectedTokenStream = new IteratorTokenStream(asList("9", "-", "5"));
		EquationNode resultEquation = new NodeMinus(new NodeNumber(9), new NodeNumber(5));
		EquationBuilder builder = new EquationBuilderMock(expectedTokenStream, resultEquation);
		RomanCalculator calc = new RomanCalculatorImpl(lexer, translator, builder);
		DataInput input = mock(DataInput.class);
		when(input.getInput()).thenReturn("IX - V");
		DataOutput output = mock(DataOutput.class);
		calc.solve(input, output);
		verify(output).setOutput("IV");
	}

	private static class EquationBuilderMock implements EquationBuilder {

		private TokenStream expectedStream;
		private EquationNode result;

		public EquationBuilderMock(TokenStream expectedStream, EquationNode result) {
			this.expectedStream = expectedStream;
			this.result = result;
		}

		@Override
		public EquationNode buildEquationTree(TokenStream tokens) {
			while (tokens.hasNext()) {
				String actual = tokens.next();
				if (!expectedStream.hasNext()) {
					fail("There are still tokens on input, but no more tokens were expected. Failure on token: " + actual);
				}
				String expected = expectedStream.next();
				assertEquals(expected, actual);
			}
			if (expectedStream.hasNext()) {
				fail("More tokens were expected. Failure on token: " + expectedStream.next());
			}
			return result;
		}
	}

	/**
	 * Here we test what happens when the output provider
	 * throws an exception instead of giving us usefull input.
	 * Again the input is a Test Stub, specifically a Saboteur
	 */
	@Test
	public void testExceptionFromInput() throws NoInputException {
		DataInput input = mock(DataInput.class);
		when(input.getInput()).thenThrow(new NoInputException("ignored message"));
		DataOutput output = mock(DataOutput.class);
		//we can create our own dummy
		//or use mock() without configuring it any further
		//or pass null if it doesn't break anything
		RomanCalculator calc = new RomanCalculatorImpl(
				new DummyLexer(), mock(RomanTranslator.class), null);
		calc.solve(input, output);
		verify(output).setOutput("error: No Input");
	}

	private static class DummyLexer implements Lexer {

		@Override
		public TokenStream tokenize(String rawInput) {
			return null;
		}

	}

	/**
	 * Here is another example of passing invalid inputs
	 * through the Saboteur Test Stub
	 */
	@Ignore
	@Test
	public void testInvalidInput() throws NoInputException {
		DataInput input = mock(DataInput.class);
		when(input.getInput()).thenReturn("IV + a");
		DataOutput output = mock(DataOutput.class);
		RomanTranslator translator = mock(RomanTranslator.class);
		when(translator.toDecimal("IV")).thenReturn(4);
		//This test doesn't make much sense without real implementation
		//of the component expected to react to the bad input.
		//It is here only for illustartion
		EquationBuilder builder = null;
		RomanCalculator calc = new RomanCalculatorImpl(new SimpleLexer(), translator, null);
		calc.solve(input, output);
		verify(output).setOutput("error: Invalid Input");
	}

}
