package org.opentutorials.javatutorials.generic;

abstract class Info{
    public abstract int getLevel();
}
class EmployeeInfo_8 extends Info{
    public int rank;
    EmployeeInfo_8(int rank){ this.rank = rank; }
    public int getLevel(){
        return this.rank;
    }
}
class Person_8<T extends Info>{
    public T info;
    Person_8(T info){ this.info = info; }
}
public class GenericDemo_8 {
    public static void main(String[] args) {
//        Person_8<EmployeeInfo_8> p1 = new Person_8<EmployeeInfo_8>(new EmployeeInfo_8(1));
        Person_8<String> p2 = new Person_8<String>("부장");
    }
}
