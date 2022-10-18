package org.opentutorials.javatutorials.generic;

interface Info_9{
    int getLevel();
}
class EmployeeInfo_9 implements Info_9{
    public int rank;
    EmployeeInfo_9(int rank){ this.rank = rank; }
    public int getLevel(){
        return this.rank;
    }
}
class Person_9<T extends Info_9>{
    public T info;
    Person_9(T info){ this.info = info; }
}
public class GenericDemo_9 {
    public static void main(String[] args) {
        Person_9 p1 = new Person_9(new EmployeeInfo_9(1));
//        Person<String> p2 = new Person<String>("부장");
    }
}
