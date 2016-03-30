package pv260.romancalc.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pv260.romancalc.framework.Lexer;
import pv260.romancalc.framework.TokenStream;

public class SimpleLexer implements Lexer {

	private static final Map<Character, String> OPERATOR_TOKEN_MAPPING;

	static {
		OPERATOR_TOKEN_MAPPING = new HashMap<>();
		OPERATOR_TOKEN_MAPPING.put('+', "+");
		OPERATOR_TOKEN_MAPPING.put('-', "-");
	}

	@Override
	public TokenStream tokenize(String rawInput) {
		StringBuilder currentNumeral = new StringBuilder();
		List<String> tokens = new ArrayList<>();
		String trimmedInput = rawInput.replace(" ", "");
		for (char c : trimmedInput.toCharArray()) {
			if (isOperator(c)) {
				appendNumeralIfNotEmpty(currentNumeral, tokens);
				tokens.add(OPERATOR_TOKEN_MAPPING.get(c));
				continue;
			}
			currentNumeral.append(c);
		}
		appendNumeralIfNotEmpty(currentNumeral, tokens);
		return new IteratorTokenStream(tokens);
	}

	private boolean isOperator(char c) {
		return OPERATOR_TOKEN_MAPPING.containsKey(c);
	}

	private void appendNumeralIfNotEmpty(StringBuilder numeral, List<String> tokens) {
		if (numeral.length() != 0) {
			tokens.add(numeral.toString());
			numeral.setLength(0);
		}
	}

}
