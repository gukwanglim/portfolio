package org.opentutorials.javatutorials.progenitor;

class Student_3 implements Cloneable{
    String name;
    Student_3(String name){
        this.name = name;
    }
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
 
class ObjectDemo_3 {
 
    public static void main(String[] args) {
        Student_3 s1 = new Student_3("kwanglim");
        try {
            Student_3 s2 = (Student_3)s1.clone();
            System.out.println(s1.name);
            System.out.println(s2.name);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
