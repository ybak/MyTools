package org.ybak.hystrix.basic.rxjava;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import rx.Observable;
import rx.Subscription;
import rx.internal.util.InternalObservableUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by isaac on 16/6/28.
 */
public class TransformingTest {

    @Test
    public void scan() {
        Observable.range(1, 4).scan((sum, item) -> sum + item).subscribe(System.out::println);
    }

    @Test
    public void testFlatMap() {
        Observable.just("a", "b", "c")
                .map(s -> "prefix-" + s)
                .subscribe(System.out::println);

        Observable.just("a", "b", "c")
                .flatMap(s -> Observable.just("prefix-" + s))
                .subscribe(System.out::println);
    }

    @Test
    public void testMapReduceWithWindow() throws InterruptedException {
        Observable<String> endlessNumbers = Observable.interval(20, TimeUnit.MILLISECONDS).map(i -> "" + RandomUtils.nextInt(20));

        Subscription subscription1 = endlessNumbers.buffer(3, TimeUnit.SECONDS).subscribe(buffer -> {
            Observable.from(buffer);
        });

        TimeUnit.SECONDS.sleep(5);
        subscription1.unsubscribe();
    }


    @Test
    public void timeWindowAndBufferTest() throws InterruptedException {
        //每隔1秒就随机发布一封邮件
        Observable<String> endlessMail = Observable.interval(1, TimeUnit.SECONDS).map(i -> "" + RandomUtils.nextInt(20));
        Subscription subscription1 = endlessMail.buffer(3, TimeUnit.SECONDS).subscribe(buffer -> {
            System.out.println(String.format("#BUFFER# You've got %d new messages!  Here they are!", buffer.size()));
            buffer.forEach(i -> System.out.println("#BUFFER# " + i.toString()));
        });

        //把上面产生的邮件内容缓存到列表中，并每隔3秒通知订阅者
        Subscription subscription2 = endlessMail.window(3, TimeUnit.SECONDS).subscribe(window -> {
            window.subscribe(i -> System.out.println("#WINDOW# " + i.toString()),
                    InternalObservableUtils.ERROR_NOT_IMPLEMENTED,
                    () -> System.out.println("#WINDOW# You've got new messages!  Here they are!"));
        });

        TimeUnit.SECONDS.sleep(5);
        subscription1.unsubscribe();
        subscription2.unsubscribe();
    }

    @Test
    public void windowSkipTest() {
        Observable<Integer> range = Observable.range(1, 10);
        range.subscribe(System.out::println);

        System.out.println();

        range.window(3, 1).subscribe(
                window -> window.subscribe(x -> System.out.print(x + " "),
                        InternalObservableUtils.ERROR_NOT_IMPLEMENTED,
                        () -> System.out.println())
        );
    }

}
