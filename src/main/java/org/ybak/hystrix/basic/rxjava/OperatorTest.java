package org.ybak.hystrix.basic.rxjava;

import org.junit.Test;
import rx.Observable;

/**
 * Created by isaac on 16/6/27.
 */

public class OperatorTest {

    @Test
    public void testFlatMap() {
        Observable.just("a", "b", "c")
                .map(s -> "prefix-" + s)
                .subscribe(System.out::println);

        Observable.just("a", "b", "c")
                .flatMap(s -> Observable.just("prefix-" + s))
                .subscribe(System.out::println);
    }

}
