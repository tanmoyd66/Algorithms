package trd.algorithms.DynamicProgramming;

import java.util.ArrayList;
import java.util.List;

public class OptimalParenthesization {
	//-----------------------------------------------------------------------------------------------------
	// Maximal Value By Parenthesization (Recursive Formulation)
	//-----------------------------------------------------------------------------------------------------
	public static class OptimalMaxParenthesization_Config {
		String[] 	Operators;
		Integer[] 	Operands;		
		public OptimalMaxParenthesization_Config(String[] Operators, Integer[] Operands) {
			this.Operators = Operators; this.Operands = Operands;
		}
	}
	public static class OptimalMaxParenthesization_Result {
		Integer		Result;
		String		stringizedResult = "";
		public OptimalMaxParenthesization_Result(Integer Result, String stringizedResult) {
			this.Result = Result; this.stringizedResult = stringizedResult;
		}
	}
	public static Integer getValue(String operator, Integer lhs, Integer rhs) {
		switch (operator) {
		case "+": return lhs + rhs;
		case "-": return lhs - rhs;
		case "*": return lhs * rhs;
		case "/": return lhs / rhs;
		}
		return 0;
	}
	public static OptimalMaxParenthesization_Result OptimalMaxParenthesization(OptimalMaxParenthesization_Config ompc, int pos){
		if (pos == 0) {
			Integer Result = getValue(ompc.Operators[0], ompc.Operands[0], ompc.Operands[1]);
			String  stringizedResult = "(" + ompc.Operands[0] + ompc.Operators[0] + ompc.Operands[1] + ")";
			return new OptimalMaxParenthesization_Result(Result, stringizedResult);
		} else if (pos < 0) {
			return null;
		} else {
			OptimalMaxParenthesization_Result res1 = OptimalMaxParenthesization(ompc, pos - 1);
			OptimalMaxParenthesization_Result res2 = OptimalMaxParenthesization(ompc, pos - 2);

			if (res1 == null)
				return null;
			
			res1.Result = getValue(ompc.Operators[pos], res1.Result, ompc.Operands[pos + 1]);
			res1.stringizedResult = "(" + res1.stringizedResult + ompc.Operators[pos] + ompc.Operands[pos + 1] + ")";
			if (res2 == null)
				return res1;
			
			res2.Result = getValue(ompc.Operators[pos - 1], res2.Result, getValue(ompc.Operators[pos], ompc.Operands[pos], ompc.Operands[pos + 1]));
			res2.stringizedResult = "(" + res2.stringizedResult + ompc.Operators[pos - 1] + "(" + ompc.Operands[pos] + ompc.Operators[pos] + ompc.Operands[pos + 1] + ")" + ")";
			return res2.Result > res1.Result ? res2 : res1;
		}
	}
	private static String getExpressionFromOperatorOperandArrays(String[] Operators, Integer[] Operands) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < Operators.length; i++) {
			sb.append(String.format("%d %s ", Operands[i], Operators[i]));
		}
		sb.append(String.format("%d]", Operands[Operands.length - 1]));
		return sb.toString();
	}
	
	public static void OptimalMaxParenthesization(String[] Operators, Integer[] Operands) {
		OptimalMaxParenthesization_Result ret = OptimalMaxParenthesization(new OptimalMaxParenthesization_Config(Operators, Operands), Operators.length - 1);
        System.out.printf("Maximal Value of %s is: %s = %d\n", getExpressionFromOperatorOperandArrays(Operators, Operands), ret.stringizedResult, ret.Result);
 	}

	public static void OptimalMaxParenthesization(String str) {
		char[] expr  = str.toCharArray();
		int    pos   = 0;
		int    start = 0;
		
		List<String>  Operators = new ArrayList<String>();
		List<Integer> Operands  = new ArrayList<Integer>();
		
		String currToken = "";
		while (start < expr.length) {
			if (Character.isDigit(expr[start])) {
				currToken += expr[start++];
			} else {
				if (expr[start] == '+')
					Operators.add("+"); 
				if (expr[start] == '*')
					Operators.add("*"); 
				if (expr[start] == '-')
					Operators.add("-"); 
				if (expr[start] == '/')
					Operators.add("/"); 
				Operands.add(Integer.parseInt(currToken));
				currToken = "";
				start++;
			}
		}
		if (!currToken.isEmpty())
			Operands.add(Integer.parseInt(currToken)); 
		
		String[]  _Operators = new String[Operators.size()];
		for (pos = 0; pos < Operators.size(); pos++)
			_Operators[pos] = Operators.get(pos);
		Integer[] _Operands  = new Integer[Operands.size()];
		for (pos = 0; pos < Operands.size(); pos++)
			_Operands[pos] = Operands.get(pos);
		OptimalMaxParenthesization(_Operators, _Operands);
 	}

	public static void main(String[] args) {
		OptimalMaxParenthesization("1+3*6+3");
	}

}
