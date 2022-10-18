package org.opentutorials.javatutorials.polymorphism;

class A_1{
    public String x(){return "x";}
}
class B_1 extends A_1{
    public String y(){return "y";}
}
public class PolymorphismDemo1_1 {
    public static void main(String[] args) {
        A_1 obj = new B_1();
        obj.x();
        obj.y();
    }
}
