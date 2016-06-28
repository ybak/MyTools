package org.ybak.hystrix.basic.rxjava;

import org.junit.Test;
import rx.Observable;

/**
 * Created by isaac on 16/6/28.
 */
public class FilteringTest {
    @Test
    public void testSkip() {
        Observable.range(1, 100).skip(98).subscribe(System.out::println);
    }

}
