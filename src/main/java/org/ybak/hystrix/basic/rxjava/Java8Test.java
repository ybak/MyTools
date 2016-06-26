package org.ybak.hystrix.basic.rxjava;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ybak on 16/6/25.
 */
public class Java8Test {
    public static void main(String[] args) {
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5).stream();
        System.out.println(stream.map(Objects::toString).collect(Collectors.joining()));
    }
}
