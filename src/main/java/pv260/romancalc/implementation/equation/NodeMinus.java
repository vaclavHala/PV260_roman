package pv260.romancalc.implementation.equation;

import pv260.romancalc.framework.EquationNode;

public class NodeMinus extends NodeOperator {

	public NodeMinus(EquationNode left, EquationNode right) {
		super(left, right);
	}

	@Override
	public int evaluate() {
		return left().evaluate() - right().evaluate();
	}

}
