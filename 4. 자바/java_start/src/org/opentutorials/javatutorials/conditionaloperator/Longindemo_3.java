package org.opentutorials.javatutorials.conditionaloperator;

public class Longindemo_3 {

	public static void main(String[] args) {
        String id = args[0];
        String password = args[1];
        if ((id.equals("kwanglim") || id.equals("k8805") || id.equals("sorialgi"))
                && password.equals("111111")) {
            System.out.println("right");
        } else {
            System.out.println("wrong");
	    }

	}

}
