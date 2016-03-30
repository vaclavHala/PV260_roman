package pv260.romancalc.implementation.equation;

import pv260.romancalc.framework.EquationNode;

public abstract class NodeOperator implements EquationNode {

	private EquationNode left;
	private EquationNode right;

	public NodeOperator(EquationNode left, EquationNode right) {
		this.left = left;
		this.right = right;
	}

	protected EquationNode left() {
		return left;
	}

	protected EquationNode right() {
		return right;
	}

}
