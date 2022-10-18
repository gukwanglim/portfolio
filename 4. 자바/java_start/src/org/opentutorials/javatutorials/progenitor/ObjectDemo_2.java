package org.opentutorials.javatutorials.progenitor;

import java.util.Objects;

class Student_2{
    String name;
    Student_2(String name){
        this.name = name;
    }
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student_2 other = (Student_2) obj;
		return Objects.equals(name, other.name);
	}
}
 
class ObjectDemo_2 {
 
    public static void main(String[] args) {
        Student_2 s1 = new Student_2("kwanglim");
        Student_2 s2 = new Student_2("kwanglim");
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s1.name == s2.name);
    }
}
