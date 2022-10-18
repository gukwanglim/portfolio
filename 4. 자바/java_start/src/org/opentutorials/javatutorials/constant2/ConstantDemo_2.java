package org.opentutorials.javatutorials.constant2;
 
enum Fruit_1{
    APPLE, PEACH, BANANA;
	}
enum Company_1{
    GOOGLE, APPLE, ORACLE;
	}
 
public class ConstantDemo_2 {
     
    public static void main(String[] args) {
        /*
        if(Fruit.APPLE == Company.APPLE){
            System.out.println("과일 애플과 회사 애플이 같다.");
        }
        */
        Fruit_1 type = Fruit_1.APPLE;
        switch(type){
            case APPLE:
                System.out.println(57+" kcal");
                break;
            case PEACH:
                System.out.println(34+" kcal");
                break;
            case BANANA:
                System.out.println(93+" kcal");
                break;
        }
    }
}
