package org.opentutorials.javatutorials.exception;

class DivideException_2 extends Exception {
    DivideException_2(){
        super();
    }
    DivideException_2(String message){
        super(message);
    }
}
class Calculator_6{
    int left, right;
    public void setOprands(int left, int right){        
        this.left = left;
        this.right = right;
    }
    public void divide(){
        if(this.right == 0){
            try {
                throw new DivideException("0으로 나누는 것은 허용되지 않습니다.");
            } catch (DivideException e) {
                e.printStackTrace();
            }
        }
        System.out.print(this.left/this.right);
    }
}
public class CalculatorDemo_6 {
    public static void main(String[] args) {
        Calculator_6 c1 = new Calculator_6();
        c1.setOprands(10, 0);
        c1.divide();
    }
}
