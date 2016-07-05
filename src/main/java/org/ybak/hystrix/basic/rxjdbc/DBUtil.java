package org.ybak.hystrix.basic.rxjdbc;

import com.alibaba.fastjson.JSON;
import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.TupleN;
import org.junit.Test;

/**
 * Created by happy on 2016/4/2.
 */
public class DBUtil {

    public static Database getDB() {
        return Database.builder()
                .url("jdbc:mysql://192.168.99.100:3306/crawler?characterEncoding=UTF-8")
                .username("root").password("123456")
                .pool(5, 10).build();
    }

    @Test
    public void testGetMail(){


        getDB().select("show tables").getTupleN().subscribe(
                System.out::println
        );
    }

}
