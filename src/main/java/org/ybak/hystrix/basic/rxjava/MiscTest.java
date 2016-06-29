package org.ybak.hystrix.basic.rxjava;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.internal.util.InternalObservableUtils;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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

    @Test
    public void windowGroupTest() throws Exception {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .map(x -> RandomUtils.nextInt(10))
                .buffer(100)
                .take(3).toBlocking()
                .subscribe(
                        buffer -> {
                            buffer.stream()
                                    .map(word -> new AbstractMap.SimpleEntry<>(word, 1L))
                                    .collect(groupingBy(AbstractMap.SimpleEntry::getKey, counting()))
                                    .entrySet().stream()
                                    .forEach(System.out::println);
                            System.out.println();
                        }
                );
    }

    @Test
    public void testGroup() throws InterruptedException {
        Observable.interval(1, TimeUnit.MILLISECONDS)
                .map(x -> RandomUtils.nextInt(10))
                .take(100)
                .groupBy(n -> n)
                .flatMap(g -> g.toList())
                .map(list -> new AbstractMap.SimpleEntry(list.get(0), list.size()))
                .toBlocking()
                .forEach(System.out::println);
    }

}
