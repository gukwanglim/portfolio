package org.opentutorials.javatutorials.exception;

import java.io.*;

class B{
    void run() throws FileNotFoundException{
    	BufferedReader bReader = null;
    	String input = null;
    	
		bReader = new BufferedReader(new FileReader("out.txt"));
		
		try {
			input = bReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         System.out.println(input);
    }
}
class C{
    void run(){
        B b = new B();
        try {
			b.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
public class ThrowExceptionDemo {
    public static void main(String[] args) {
         C c = new C();
         c.run();
    }  
}
