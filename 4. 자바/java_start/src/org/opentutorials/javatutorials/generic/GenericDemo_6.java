package org.opentutorials.javatutorials.generic;

class EmployeeInfo_6{
    public int rank;
    EmployeeInfo_6(int rank){ this.rank = rank; }
}
class Person_6<T, S>{
    public T info;
    public S id;
    Person_6(T info, S id){ 
        this.info = info;
        this.id = id;
    }
    public <U> void printInfo(U info){
        System.out.println(info);
    }
}
public class GenericDemo_6 {
    public static void main(String[] args) {
        EmployeeInfo_6 e = new EmployeeInfo_6(1);
        Integer i = new Integer(10);
        Person_6<EmployeeInfo_6, Integer> p1 = new Person_6<EmployeeInfo_6, Integer>(e, i);
        p1.<EmployeeInfo_6>printInfo(e);
    }
}