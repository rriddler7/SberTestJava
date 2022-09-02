package task2;

public class Main2 {
    public static void main(String[] args) {
        Daughter daughter = new Daughter();
        System.out.println("Daughter: " + daughter.getFactorial(3));
        System.out.println("Other: " + Other.getFactorial(5));
    }
}
