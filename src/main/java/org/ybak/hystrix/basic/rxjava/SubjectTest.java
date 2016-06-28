package org.ybak.hystrix.basic.rxjava;

import org.junit.Test;
import rx.subjects.BehaviorSubject;

/**
 * Created by isaac on 16/6/28.
 */
public class SubjectTest {

    @Test
    public void test() {
        BehaviorSubject<Object> subject = BehaviorSubject.create("default");
        subject.subscribe(System.out::println);
        subject.onNext("one");
        subject.onNext("two");
        subject.onNext("three");
        System.out.println(subject.getValue());
    }

}
