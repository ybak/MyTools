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

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by isaac on 16/6/24.
 */
public class RxJavaTest {


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
    public void windowTest() throws InterruptedException {
        Observable<String> endlessMail = generateEndlessMails();
        Subscription subscription1 = endlessMail.buffer(3, TimeUnit.SECONDS).subscribe(buffer -> {
            System.out.println(String.format("#BUFFER# You've got %d new messages!  Here they are!", buffer.size()));
            buffer.forEach(i -> System.out.println("#BUFFER# " + i.toString()));
        });

        //把上面产生的邮件内容缓存到列表中，并每隔3秒通知订阅者
        Subscription subscription2 = endlessMail.window(3, TimeUnit.SECONDS).subscribe(window -> {
            window.subscribe(i -> System.out.println("#WINDOW# " + i.toString()));
        });
        TimeUnit.SECONDS.sleep(10);
        subscription1.unsubscribe();
        TimeUnit.SECONDS.sleep(10);
        subscription2.unsubscribe();

    }

    private static Observable<String> generateEndlessMails() {
        //每隔1秒就随机发布一封邮件
        return Observable.interval(1, TimeUnit.SECONDS).map(i -> "mail" + i);
    }
}
