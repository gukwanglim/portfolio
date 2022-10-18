package org.opentutorials.javatutorials.generic;

class StudentInfo_4{
    public int grade;
    StudentInfo_4(int grade){ this.grade = grade; }
}
class EmployeeInfo_4{
    public int rank;
    EmployeeInfo_4(int rank){ this.rank = rank; }
}
class Person_4<T>{
    public T info;
    Person_4(T info){ this.info = info; }
}
public class GenericDemo_4 {
    public static void main(String[] args) {
        Person_4<EmployeeInfo_4> p1 = new Person_4<EmployeeInfo_4>(new EmployeeInfo_4(1));
        EmployeeInfo_4 ei1 = p1.info;
        System.out.println(ei1.rank); // 성공
         
        Person_4<String> p2 = new Person_4<String>("부장");
        String ei2 = p2.info;
        System.out.println(ei2.rank); // 컴파일 실패
    }
}
