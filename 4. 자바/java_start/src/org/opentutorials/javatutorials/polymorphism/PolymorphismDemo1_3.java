package org.opentutorials.javatutorials.polymorphism;

class A_3{
    public String x(){return "A.x";}
}
class B_3 extends A_3{
    public String x(){return "B.x";}
    public String y(){return "y";}
}
class B_3_2 extends A_3{
    public String x(){return "B2.x";}
}
public class PolymorphismDemo1_3 {
    public static void main(String[] args) {
        A_3 obj = new B_3();
        A_3 obj2 = new B_3_2();
        System.out.println(obj.x());
        System.out.println(obj2.x());
    }
}
