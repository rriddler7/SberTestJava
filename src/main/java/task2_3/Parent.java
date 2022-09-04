package task2_3;

abstract public class Parent {
    public long getFactorial(long n) {

        if (n < 0) {
            System.err.println("Number must be >= 0");
            System.exit(-1);
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
