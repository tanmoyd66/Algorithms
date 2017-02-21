package trd.algorithms.MachineLearning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

public class Cart {

	public static class Values {
		public String valueName = new String();
		public List<String> classes = new ArrayList<String>();
		public List<Integer> classesCount = new ArrayList<Integer>();
		public double gain = 0.0;

		public Values(String valName, String newClass) {
			this.valueName = valName;
			this.classes.add(newClass);
			this.classesCount.add(1);
		}

		public void setGain() {
			double temp = 0.0;

			int totalNumClasses = 0;
			for (int i : this.classesCount) {
				totalNumClasses += i;
			}

			for (double d : classesCount) {
				temp = (-1 * (d / totalNumClasses)) * (Math.log((d / totalNumClasses)) / Math.log(2));
				this.gain += temp;
			}
		}

		public void update(Val inVal) {
			if (this.classes.contains(inVal.itClass)) {
				this.classesCount.set(this.classes.indexOf(inVal.itClass),
						this.classesCount.get(this.classes.indexOf(inVal.itClass)) + 1);
			} else {
				this.classes.add(inVal.itClass);
				this.classesCount.add(this.classes.indexOf(inVal.itClass), 1);
			}
		}

	}

	public static class Val {
		public String valueName = "";
		public String itClass = "";

		public Val(String name, String inClass) {
			this.valueName = new String(name);
			this.itClass = new String(inClass);
		}

		public boolean isNameEqual(Val inV) {
			if (this.valueName.equals(inV.valueName))
				return true;
			return false;
		}
	}

	public static class Attribute {
		public String name = new String();
		public List<Values> values = new ArrayList<Values>();
		public double gain = 0.0;

		public Attribute(String name) {
			this.name = name;
		}

		public void setGain(double IofD, int totalNumClasses) {
			int totalValClasses = 0;
			double temp = 0.0;
			for (Values v : values) {
				v.setGain();
				for (int i : v.classesCount) {
					totalValClasses += i;
				}
				gain += (totalValClasses / (double) totalNumClasses) * v.gain;
				totalValClasses = 0;
			}
			this.gain = IofD - this.gain;
		}

		public void insertVal(Val inVal) {
			if (this.values.isEmpty()) {
				values.add(new Values(inVal.valueName, inVal.itClass));
			} else {
				for (Values v : values) {
					if (v.valueName.equals(inVal.valueName)) {
						v.update(inVal);
						return;
					}
				}
				values.add(new Values(inVal.valueName, inVal.itClass));
			}
		}

		public String toString() {
			String out = new String("attribute: " + this.name + "\n");
			for (Values v : values) {
				out += "\tvalue: " + v.valueName + ", ";
				out += "\n\t\tclasses: ";
				for (String c : v.classes) {
					out += c + ", ";
				}
				out += "\n\t\tcounts: ";
				for (Integer i : v.classesCount) {
					out += i + ", ";
				}
				out += "\n";
			}

			return out;
		}

	}

	public static void main(String[] args) throws IOException {
		// .csv data sets
		String files[] = { "data_sets/tictactoe.txt" };
		Scanner scan;

		// start loop for all files HERE
		scan = new Scanner(new File(files[0]));
		String headerLine = scan.nextLine();
		String headers[] = headerLine.split(",");

		// class index is assumed to be the last column
		int classIndex = headers.length - 1;
		int numAttributes = headers.length - 1;

		// store data set attributes
		Attribute attributes[] = new Attribute[numAttributes];
		for (int x = 0; x < numAttributes; x++) {
			attributes[x] = new Attribute(headers[x]);
		}

		// for storing classes and class count
		List<String> classes = new ArrayList<String>();
		List<Integer> classesCount = new ArrayList<Integer>();

		// store are values into respected attributes
		// along with respected classes
		while (scan.hasNextLine()) {
			Val data = null;
			String inLine = scan.nextLine();
			String lineData[] = inLine.split(",");

			// insert class into classes List
			if (classes.isEmpty()) {
				classes.add(lineData[classIndex]);
				classesCount.add(classes.indexOf(lineData[classIndex]), 1);
			} else {
				if (!classes.contains(lineData[classIndex])) {
					classes.add(lineData[classIndex]);
					classesCount.add(classes.indexOf(lineData[classIndex]), 1);
				} else {
					classesCount.set(classes.indexOf(lineData[classIndex]),
							classesCount.get(classes.indexOf(lineData[classIndex])) + 1);
				}
			}

			// insert data into attributes
			for (int x = 0; x < numAttributes; x++) {
				data = new Val(lineData[x], lineData[classIndex]);
				attributes[x].insertVal(data);
			}
		}
		int totalNumClasses = 0;
		for (int i : classesCount) {
			totalNumClasses += i;
		}
		double IofD = calcIofD(classesCount); // Set information criteria

		// TESTING DATA
		Attribute age = new Attribute("age");

		Val inV = new Val("30", "yes");
		age.insertVal(inV);
		inV = new Val("30", "yes");
		age.insertVal(inV);
		inV = new Val("30", "no");
		age.insertVal(inV);
		inV = new Val("30", "no");
		age.insertVal(inV);
		inV = new Val("30", "no");
		age.insertVal(inV);
		inV = new Val("35", "yes");
		age.insertVal(inV);
		inV = new Val("35", "yes");
		age.insertVal(inV);
		inV = new Val("35", "yes");
		age.insertVal(inV);
		inV = new Val("35", "yes");
		age.insertVal(inV);
		inV = new Val("40", "yes");
		age.insertVal(inV);
		inV = new Val("40", "yes");
		age.insertVal(inV);
		inV = new Val("40", "yes");
		age.insertVal(inV);
		inV = new Val("40", "no");
		age.insertVal(inV);
		inV = new Val("40", "no");
		age.insertVal(inV);

		System.out.println(age.toString());

		List<Integer> testCount = new ArrayList<Integer>();
		testCount.add(9);
		testCount.add(5);

		double testIofD = calcIofD(testCount);
		age.setGain(testIofD, 14);

		System.out.println("I of D: " + testIofD);
		System.out.println("age: " + age.gain);
	}

	public static double calcIofD(List<Integer> classesCount) {
		double IofD = 0.0;
		double temp = 0.0;

		int totalNumClasses = 0;
		for (int i : classesCount) {
			totalNumClasses += i;
		}

		for (double d : classesCount) {
			temp = (-1 * (d / totalNumClasses)) * (Math.log((d / totalNumClasses)) / Math.log(2));
			IofD += temp;
		}
		return IofD;
	}
}