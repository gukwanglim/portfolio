package org.opentutorials.javatutorials.constant2;

enum Fruit_6{
    APPLE("red"), PEACH("pink"), BANANA("yellow");
    private String color;
    Fruit_6(String color){
        System.out.println("Call Constructor "+this);
        this.color = color;
    }
    String getColor(){
        return this.color;
    }
}
 
enum Company_6{
    GOOGLE, APPLE, ORACLE;
}
 
public class ConstantDemo_6 {
     
    public static void main(String[] args) {
        for(Fruit_6 f : Fruit_6.values()){
            System.out.println(f+", "+f.getColor());
        }
    }
}
