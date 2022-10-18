package org.opentutorials.javatutorials.exception;

class A_1{
    private int[] arr = new int[3];
    A_1(){
        arr[0]=0;
        arr[1]=10;
        arr[2]=20;
    }
    public void z(int first, int second){
        try {
            System.out.println(arr[first] / arr[second]);
        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("ArrayIndexOutOfBoundsException");
        } catch(ArithmeticException e){
            System.out.println("ArithmeticException");
        } catch(Exception e){
            System.out.println("Exception");
        } finally {
            System.out.println("finally\n");
        }
    }
}
 
public class ExceptionDemo1_1 {
    public static void main(String[] args) {
        A_1 a = new A_1();
        a.z(10, 0);
        a.z(1, 0);
        a.z(2, 1);
    }
}
