package org.ybak.hystrix.basic.java8;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import rx.Observable;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

/**
 * Created by isaac on 16/6/29.
 */
public class MapReduceTest {
    static List<String> lines = Observable.range(1, 10000).map(x -> RandomStringUtils.randomAlphabetic(1)).toList().toBlocking().single();

    @Test
    public void wordCountBasic() throws IOException {
        Map<String, Long> wordCount = lines.stream()
                .flatMap(line -> Arrays.stream(line.trim().split(" ")))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())//根据内容过滤
                .filter(word -> word.length() > 0)// 根据长度过滤
                .map(word -> new SimpleEntry<>(word, 1))  // 转换为entry list
                .reduce(new LinkedHashMap<>(), (acc, entry) -> {
                    acc.put(entry.getKey(), acc.compute(entry.getKey(), (k, v) -> v == null ? 1 : v + 1));
                    return acc;
                }, (m1, m2) -> m1);// reduce

        wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((x, y) -> -Long.compare(x, y))) //排序
                .forEach(entry -> System.out.println(String.format("%s ==>> %d", entry.getKey(), entry.getValue())));
    }

    @Test
    public void wordCountWithCollector() throws IOException {
        Map<String, Long> wordCount = lines.stream()
                .flatMap(line -> Arrays.stream(line.trim().split(" "))) // 分隔
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim()) //根据内容过滤
                .filter(word -> word.length() > 0)// 根据长度过滤
                .map(word -> new SimpleEntry<>(word, 1L))// 转换为entry list
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v1 + v2)); // reduce

        wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((x, y) -> -Long.compare(x, y))) //排序
                .forEach(entry -> System.out.println(String.format("%s ==>> %d", entry.getKey(), entry.getValue())));
    }


    @Test
    public void wordCountWithGroupAndCount() throws IOException {
        Map<String, Long> wordCount = lines.stream()
                .flatMap(line -> Arrays.stream(line.trim().split(" "))) // 分隔
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> word.length() > 0)
                .map(word -> new SimpleEntry<>(word, 1L))// map
                .collect(groupingBy(SimpleEntry::getKey, counting())); //reduce

        wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((x, y) -> -Long.compare(x, y))) //排序
                .forEach(entry -> System.out.println(String.format("%s ==>> %d", entry.getKey(), entry.getValue())));
    }

    @Test
    public void wordCountWithRxJava() throws IOException {
        Observable.from(lines).flatMap(line -> Observable.from(line.trim().split(" ")))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> word.length() > 0)
                .map(word -> new SimpleEntry<>(word, 1L))
                .reduce(new LinkedHashMap<String, Long>(), (acc, entry) -> {
                    acc.put(entry.getKey(), acc.compute(entry.getKey(), (k, v) -> v == null ? 1 : v + 1));
                    return acc;
                })
                .toBlocking().single().entrySet().forEach(entry -> System.out.println(String.format("%s ==>> %d", entry.getKey(), entry.getValue())));
//                .flatMap(x -> Observable.from(x.entrySet()))
//                .toSortedList((a, b) -> -Long.compare(a.getValue(), b.getValue()))
//                .toBlocking().single().forEach(entry -> System.out.println(String.format("%s ==>> %d", entry.getKey(), entry.getValue())));
    }

}
