package org.opentutorials.javatutorials.constant2;

enum Fruit_5{
    APPLE("red"), PEACH("pink"), BANANA("yellow");
    private String color;
    Fruit_5(String color){
        System.out.println("Call Constructor "+this);
        this.color = color;
    }
    String getColor(){
        return this.color;
    }
}
 
enum Company_5{
    GOOGLE, APPLE, ORACLE;
}
 
public class ConstantDemo_5 {
     
    public static void main(String[] args) {
        Fruit_5 type = Fruit_5.APPLE;
        switch(type){
            case APPLE:
                System.out.println(57+" kcal, "+Fruit_5.APPLE.getColor());
                break;
            case PEACH:
                System.out.println(34+" kcal"+Fruit_5.PEACH.getColor());
                break;
            case BANANA:
                System.out.println(93+" kcal"+Fruit_5.BANANA.getColor());
                break;
        }
    }
}
