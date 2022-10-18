package org.opentutorials.javatutorials.exception;

class DivideException_3 extends Exception {
	public int left;
	public int right;
	
	DivideException_3(){
        super();
    }
	DivideException_3(String message){
        super(message);
    }
	DivideException_3(String message, int left, int right){
    	super(message);
    	this.left = left;
    	this.right = right;
    }
}
class Calculator_7{
    int left, right;
    public void setOprands(int left, int right){        
        this.left = left;
        this.right = right;
    }
    public void divide() throws DivideException_3{
        if(this.right == 0){
        	 throw new DivideException_3("0으로 나누는 것은 허용되지 않습니다.", this.left, this.right);
        }
        System.out.print(this.left/this.right);	
    }
}
public class CalculatorDemo_7 {
    public static void main(String[] args) {
        Calculator_7 c1 = new Calculator_7();
        c1.setOprands(10, 0);
        try {
        	c1.divide();
        } catch (DivideException_3 e) {
        	System.out.println(e.getMessage());
        	System.out.println(e.left);
        	System.out.println(e.right);
        }
    }
}
