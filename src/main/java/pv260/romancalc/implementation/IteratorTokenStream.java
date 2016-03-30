package pv260.romancalc.implementation;

import java.util.Iterator;
import pv260.romancalc.framework.TokenStream;

public class IteratorTokenStream implements TokenStream {

	private Iterator<String> iterator;

	public IteratorTokenStream(Iterable<String> iteratorTarget) {
		this.iterator = iteratorTarget.iterator();
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public String next() {
		return iterator.next();
	}

}
