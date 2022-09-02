package task2;

public class Daughter extends Parent{

    @Override
    public int getFactorial(int n) {
        n = 10;
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
//        return 3628800;
    }

}
