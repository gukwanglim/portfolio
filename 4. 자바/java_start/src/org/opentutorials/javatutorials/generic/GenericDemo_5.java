package org.opentutorials.javatutorials.generic;

class EmployeeInfo_5{
    public int rank;
    EmployeeInfo_5(int rank){ this.rank = rank; }
}
class Person_5<T, S>{
    public T info;
    public S id;
    Person_5(T info, S id){ 
        this.info = info; 
        this.id = id;
    }
}
public class GenericDemo_5 {
    public static void main(String[] args) {
    	EmployeeInfo_5 e = new EmployeeInfo_5(1);
        Integer i = new Integer(10);
        Person_5<EmployeeInfo_5, Integer> p1 = new Person_5<EmployeeInfo_5, Integer>(e, i);
        System.out.println(p1.id.intValue());
    }
}
