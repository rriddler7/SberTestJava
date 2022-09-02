package task2;

import java.sql.SQLOutput;

public class Main2 {
    public static void main(String[] args) {
//      factorial(10)  3628800
//        Parent parent = new Parent();
//        System.out.println("Parent: " + parent.getFactorial(3));
        Daughter daughter = new Daughter();
        System.out.println("Daughter: " + daughter.getFactorial(3));
        System.out.println("Other: " + Other.getFactorial(5));
    }
}
