package pv260.romancalc.implementation.equation;

import pv260.romancalc.framework.EquationNode;

public class NodeNumber implements EquationNode {

	private int value;

	public NodeNumber(int value) {
		this.value = value;
	}

	@Override
	public int evaluate() {
		return value;
	}

}
