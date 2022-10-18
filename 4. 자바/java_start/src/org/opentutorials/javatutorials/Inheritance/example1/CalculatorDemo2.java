package org.opentutorials.javatutorials.Inheritance.example1;

class MultiplicationableCalculator extends Calculator {
    public void multiplication() {
        System.out.println(this.left * this.right);
    }
}

class DivisionableCalculator extends MultiplicationableCalculator {
    public void division() {
        System.out.println(this.left / this.right);
    }
}
 
public class CalculatorDemo2 {
 
	public static void main(String[] args) {
		 
        DivisionableCalculator c1 = new DivisionableCalculator();
        c1.setOprands(10, 20);
        c1.sum();
        c1.avg();
        c1.multiplication();
        c1.division();
	}
}
