package org.ybak.hystrix.basic.rxjava;

import org.apache.commons.lang.RandomStringUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by isaac on 16/6/24.
 */
public class RxJavaTest {
    public static void main(String[] args) throws InterruptedException {
//        windowTest();
//        bufferTest();
        baseTest();
    }

    private static void baseTest() throws InterruptedException {
        Observable.interval(1, TimeUnit.SECONDS)
                .window(3).subscribe(window -> {
            System.out.println(window);
            window.map(Object::toString).reduce((sum, i) -> sum + i).subscribe(System.out::println);
        });

        TimeUnit.SECONDS.sleep(12);
    }

    private static void bufferTest() throws InterruptedException {
        final String[] mails = new String[]{"Here is an email!", "Another email!", "Yet another email!"};
        //每隔1秒就随机发布一封邮件
        Observable<String> endlessMail = Observable.create((Subscriber<? super String> subscriber) -> {
            try {
                if (subscriber.isUnsubscribed()) return;
                Random random = new Random();
                while (true) {
                    String mail = mails[random.nextInt(mails.length)];
                    subscriber.onNext(mail);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                subscriber.onError(ex);
            }
        }).subscribeOn(Schedulers.io());

        //把上面产生的邮件内容缓存到列表中，并每隔3秒通知订阅者
        endlessMail.buffer(3, TimeUnit.SECONDS).subscribe((list -> {
                    System.out.println(String.format("You've got %d new messages!  Here they are!", list.size()));
                    list.forEach(i -> System.out.println("**" + i.toString()));
                })
        );
        TimeUnit.SECONDS.sleep(10);

    }

    private static void windowTest() throws InterruptedException {
        int count = 12;
        CountDownLatch latch = new CountDownLatch(count);
        Observable.interval(250, TimeUnit.MILLISECONDS).take(count)
                .window(1, TimeUnit.SECONDS)
                .subscribe(observable -> {
                    System.out.println("subdivide begin......");
                    observable.subscribe(aLong -> {
                        System.out.println("Next:" + aLong);
                        latch.countDown();
                    });
                });

        latch.await();
    }
}
