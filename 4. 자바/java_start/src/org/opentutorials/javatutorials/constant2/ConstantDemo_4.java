package org.opentutorials.javatutorials.constant2;

enum Fruit_4{
    APPLE("red"), PEACH("pink"), BANANA("yellow");
    public String color;
    Fruit_4(String color){
        System.out.println("Call Constructor "+this);
        this.color = color;
    }
}
 
enum Company_4{
    GOOGLE, APPLE, ORACLE;
}
 
public class ConstantDemo_4 {
     
    public static void main(String[] args) {
        /*
        if(Fruit.APPLE == Company.APPLE){
            System.out.println("과일 애플과 회사 애플이 같다.");
        }
        */
        Fruit_4 type = Fruit_4.APPLE;
        switch(type){
            case APPLE:
                System.out.println(57+" kcal, "+Fruit_4.APPLE.color);
                break;
            case PEACH:
                System.out.println(34+" kcal"+Fruit_4.PEACH.color);
                break;
            case BANANA:
                System.out.println(93+" kcal"+Fruit_4.BANANA.color);
                break;
        }
    }
}
