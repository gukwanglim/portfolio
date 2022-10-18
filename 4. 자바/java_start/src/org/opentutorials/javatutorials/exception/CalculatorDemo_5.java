package org.opentutorials.javatutorials.exception;

class DivideException extends RuntimeException {
    DivideException(){
        super();
    }
    DivideException(String message){
        super(message);
    }
}
class Calculator_5{
    int left, right;
    public void setOprands(int left, int right){        
        this.left = left;
        this.right = right;
    }
    public void divide(){
        if(this.right == 0){
            throw new DivideException("0으로 나누는 것은 허용되지 않습니다.");
        }
        System.out.print(this.left/this.right);
    }
}
public class CalculatorDemo_5 {
    public static void main(String[] args) {
        Calculator_5 c1 = new Calculator_5();
        c1.setOprands(10, 0);
        c1.divide();
    }
}
