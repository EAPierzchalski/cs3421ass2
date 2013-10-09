package ass2.util;

/**
 * Created with IntelliJ IDEA.
 * User: Edward
 * Date: 9/10/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tuple2<T1, T2> {
    private T1 _1;
    private T2 _2;

    public Tuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    public T1 get1() {
        return _1;
    }

    public T2 get2() {
        return _2;
    }
}
