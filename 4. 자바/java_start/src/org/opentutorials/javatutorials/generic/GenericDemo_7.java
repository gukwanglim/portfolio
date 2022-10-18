package org.opentutorials.javatutorials.generic;

class EmployeeInfo_7{
    public int rank;
    EmployeeInfo_7(int rank){ this.rank = rank; }
}
class Person_7<T, S>{
    public T info;
    public S id;
    Person_7(T info, S id){ 
        this.info = info;
        this.id = id;
    }
    public <U> void printInfo(U info){
        System.out.println(info);
    }
}
public class GenericDemo_7 {
    public static void main(String[] args) {
        EmployeeInfo_7 e = new EmployeeInfo_7(1);
        Integer i = new Integer(10);
        Person_7 p1 = new Person_7(e, i);
        p1.printInfo(e);
    }
}
