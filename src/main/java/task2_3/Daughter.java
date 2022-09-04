package task2_3;

public class Daughter extends Parent{

    @Override
    public long getFactorial(long n) {
        n = 10;
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
