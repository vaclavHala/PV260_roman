package pv260.romancalc.implementation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pv260.romancalc.framework.RomanTranslator;
import pv260.romancalc.framework.TokenStream;

public class RomanTranslatingTokenStream implements TokenStream {

	private static Pattern ROMAN_NUMERAL = Pattern.compile("[ivxlcdm]+", Pattern.CASE_INSENSITIVE);

	private TokenStream backingStream;
	private RomanTranslator translator;

	public RomanTranslatingTokenStream(TokenStream backingStream, RomanTranslator translator) {
		this.backingStream = backingStream;
		this.translator = translator;
	}

	@Override
	public boolean hasNext() {
		return backingStream.hasNext();
	}

	@Override
	public String next() {
		String nextToken = backingStream.next();
		Matcher romanNumeralMatcher = ROMAN_NUMERAL.matcher(nextToken);
		if (romanNumeralMatcher.matches()) {
			return String.valueOf(translator.toDecimal(nextToken));
		}
		return nextToken;
	}

}
