package ass2.util;

/**
 * User: Pierzchalski
 * Date: 18/10/13
 * Package: ass2.util
 * Project: cs3421ass2
 */
public class Tuple3<T1, T2, T3> {
    private T1 _1;
    private T2 _2;
    private T3 _3;

    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
    }

    public T1 get1() {
        return _1;
    }

    public T2 get2() {
        return _2;
    }

    public T3 get3() {
        return _3;
    }
}
