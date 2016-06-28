package org.ybak.hystrix.basic.rxjava;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.internal.util.InternalObservableUtils;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by isaac on 16/6/24.
 */
public class MiscTest {


    @Test
    public void baseTest() {
        Observable
                .create(subscriber ->
                        IntStream.range(0, 10).forEach(
                                i -> subscriber.onNext(RandomStringUtils.randomAlphanumeric(5))
                        )
                )
                .subscribe(s -> System.out.println("Hello " + s + "!"));
    }

    @Test
    public void test() {
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5).stream();
        System.out.println(stream.map(Objects::toString).collect(Collectors.joining()));
    }

}
