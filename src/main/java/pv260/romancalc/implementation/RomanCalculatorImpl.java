package pv260.romancalc.implementation;

import pv260.romancalc.framework.*;

public class RomanCalculatorImpl implements RomanCalculator {

	private Lexer lexer;
	private RomanTranslator translator;
	private EquationBuilder builder;

	public RomanCalculatorImpl(Lexer lexer, RomanTranslator translator, EquationBuilder builder) {
		this.lexer = lexer;
		this.translator = translator;
		this.builder = builder;
	}

	@Override
	public void solve(DataInput in, DataOutput out) {
		String rawInput;
		try {
			rawInput = in.getInput();
		} catch (NoInputException e) {
			out.setOutput("error: No Input");
			return;
		}
		TokenStream tokens = lexer.tokenize(rawInput);
		TokenStream translatedTokens = new RomanTranslatingTokenStream(tokens, translator);
		EquationNode equationRoot;
		try {
			equationRoot = builder.buildEquationTree(translatedTokens);
		} catch (InvalidInputException e) {
			out.setOutput("error : Invalid Input");
			return;
		}
		int result = equationRoot.evaluate();
		String romanResult = translator.toRoman(result);
		out.setOutput(romanResult);
	}

}
