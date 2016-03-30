package pv260.romancalc.framework;

public interface EquationBuilder {

	EquationNode buildEquationTree(TokenStream tokens) throws InvalidInputException;

}
