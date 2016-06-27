package org.ybak.hystrix.basic.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.commons.lang.RandomStringUtils;

/**
 * Created by ybak on 16/6/26.
 */
public class HystrixDemoCommand extends HystrixCommand<String> {

    protected HystrixDemoCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
    }

    @Override
    protected String run() throws Exception {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static void main(String[] args) {
        new HystrixDemoCommand().execute();
    }
}
