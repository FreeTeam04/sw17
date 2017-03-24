package at.sw2017.calculator;

/**
 * Created by fabiowallner on 24/03/2017.
 */

public class Calculations {
    private Calculations() {
    }
    static int doAddition(int firstNumber, int secondNumber) {
        return firstNumber + secondNumber;
    }
    static int doSubtraction(int firstNumber, int secondNumber) {
        return firstNumber - secondNumber;
    }
    static int doMultiplication(int firstNumber, int secondNumber) {
        return firstNumber * secondNumber;
    }
    static int doDivision(int firstNumber, int secondNumber) {
        if (secondNumber == 0) {
            return 0;
        }
        return firstNumber / secondNumber;
    }
}
