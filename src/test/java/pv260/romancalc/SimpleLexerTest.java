/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pv260.romancalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import pv260.romancalc.framework.Lexer;
import pv260.romancalc.framework.TokenStream;
import pv260.romancalc.implementation.SimpleLexer;

public class SimpleLexerTest {

	@Test
	public void testEmptyInput() {
		String input = " ";
		Lexer lexer = new SimpleLexer();
		TokenStream tokens = lexer.tokenize(input);
		assertFalse(tokens.hasNext());
	}

	@Test
	public void testSingleNumeral() {
		String input = "  IV ";
		Lexer lexer = new SimpleLexer();
		TokenStream tokens = lexer.tokenize(input);
		assertEquals("IV", tokens.next());
		assertFalse(tokens.hasNext());
	}

	@Test
	public void testSimpleOperator() {
		String input = "  IV- X ";
		Lexer lexer = new SimpleLexer();
		TokenStream tokens = lexer.tokenize(input);
		assertEquals("IV", tokens.next());
		assertEquals("-", tokens.next());
		assertEquals("X", tokens.next());
		assertFalse(tokens.hasNext());
	}

	@Test
	public void testOperatorNextToOperator() {
		String input = "  + * ";
		Lexer lexer = new SimpleLexer();
		TokenStream tokens = lexer.tokenize(input);
		assertEquals("+", tokens.next());
		assertEquals("*", tokens.next());
		assertFalse(tokens.hasNext());
	}

	@Test
	public void testSpaceInNumeral() {
		String input = "  I X ";
		Lexer lexer = new SimpleLexer();
		TokenStream tokens = lexer.tokenize(input);
		assertEquals("IX", tokens.next());
		assertFalse(tokens.hasNext());
	}

}
