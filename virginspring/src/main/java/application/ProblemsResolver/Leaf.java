package application.ProblemsResolver;

public class Leaf extends Node {

	public Leaf(int nodeCounter) {
		super(nodeCounter);
	}

	@Override
	public void setAttribute(Attribute bestAttribute) {
		assert (bestAttribute == null);
	}
}
