package org.opentutorials.javatutorials.constant2;

enum Fruit_3{
    APPLE, PEACH, BANANA;
    Fruit_3(){
        System.out.println("Call Constructor "+this);
    }
}
 
enum Company_3{
    GOOGLE, APPLE, ORACLE;
}
 
public class ConstantDemo_3 {
     
    public static void main(String[] args) {
     
        /*
        if(Fruit.APPLE == Company.APPLE){
            System.out.println("과일 애플과 회사 애플이 같다.");
        }
        */
        Fruit_3 type = Fruit_3.APPLE;
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
