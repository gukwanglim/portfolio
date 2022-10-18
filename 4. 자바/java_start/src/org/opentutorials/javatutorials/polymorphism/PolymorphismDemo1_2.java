package org.opentutorials.javatutorials.polymorphism;

class A_2{
    public String x(){return "A.x";}
}
class B_2 extends A_2{
    public String x(){return "B.x";}
    public String y(){return "y";}
}
public class PolymorphismDemo1_2 {
    public static void main(String[] args) {
        A_2 obj = new B_2();
        System.out.println(obj.x());
    }
}
