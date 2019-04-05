package trd.algorithms.misc1;

import java.util.Stack;

public class ExpressionEvaluator {

	public static double evaluate(String line) {
		Stack<Double> operands  = new Stack<>();
		Stack<String> operators = new Stack<>();
		
		String[] tokens = ("( " + line + " )").split(" ()+-*");
		for (String token : tokens) {
			if (token.equalsIgnoreCase("(")) {
				operators.push(token);
			} else if (token.equalsIgnoreCase(")")) {
				while (!operators.isEmpty()) {
					String op = operators.pop();
					if (op.equals("("))
						break;
					Double v2 = operands.pop();
					Double v1 = operands.pop();
					Double v = evaluate(op, v1, v2);
					operands.push(v);
				}
			} else if ("-+*".indexOf(token) != -1) {
				operators.push(token);
			} else {
				Double v = Double.parseDouble(token);
				operands.push(v);
			}
		}
		return operands.pop();
	}
	
	private static double evaluate(String op, Double v1, Double v2) {
		switch (op) {
		case "-" : return v1 - v2;
		case "+" : return v1 + v2;
		case "*" : return v1 * v2;
		default:
			return 0;
		}
	}
	
	public static void main(String[] args) {
		String[] exps = { "3 + 2 + 4",
						  "3 + 2 * ( 1 + 3 )" };
		for (String s : exps) {
			System.out.printf("%s = %f\n", s, evaluate(s));
		}
	}
}
