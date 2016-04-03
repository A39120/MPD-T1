package wthr.test;

import java.util.function.Function;

public class FunctionCounter<T, R> implements Function<T, R> {

    private int count;
    private Function<T, R> countable;

    public FunctionCounter(Function<T, R> countable){
        this.countable = countable;
    }

    @Override
    public R apply(T args) {
        count++;
        return countable.apply(args);
    }

    public int getCount(){
        return count;
    }
}
