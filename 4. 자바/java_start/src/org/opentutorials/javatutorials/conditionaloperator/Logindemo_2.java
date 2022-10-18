package org.opentutorials.javatutorials.conditionaloperator;

public class Logindemo_2 {

	public static void main(String[] args) {
		String id = args[0];
        String password = args[1];
        if (id.equals("a") && password.equals("111111")) {
            System.out.println("right");
        } else {
            System.out.println("wrong");
        }

	}

}
