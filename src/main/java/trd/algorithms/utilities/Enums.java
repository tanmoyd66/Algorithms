package trd.algorithms.utilities;

import java.security.InvalidParameterException;

public class Enums {
	public static enum Operators {
		Add			(0, "+"), 
		Subtract	(1, "-"), 
		Multiply	(2, "*"), 
		Divide		(3, "/");
		
		private final int id;
		private final String name ;

		Operators(int id, String name) {
			this.id = id;
			this.name = name;
		}

		public static Operators fromString(String _name) {
			switch (_name) {
			case "+": 	return Add;
			case "-": 	return Subtract;
			case "*": 	return Multiply;
			case "/": 	return Divide;
			}
			throw new InvalidParameterException("Invalid Operators specified: " + _name);
		}

		public static Operators FromShort(int _id) {
			for (Operators l : Operators.values())
				if (l.getValue() == _id)
					return l;
			throw new InvalidParameterException("Invalid Operators specified: " +  _id);
		}

		public int getValue() {
			return id;
		}

		public String getName() {
			return name;
		}
	}
}
