package util;

public class Int {

    public static int clamp(int value, int min, int max){
        if(value < min) return min;
        if(value > max) return max;
        return value;
    }

}
