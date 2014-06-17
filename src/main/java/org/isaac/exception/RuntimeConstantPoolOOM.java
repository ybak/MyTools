package org.isaac.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -XX:MaxPermSize=10M, 只有在JDK1.6,才会抛出OOM.
 * 
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        int i = 0;
        while (true) {
            list.add(String.valueOf(i++).intern());
        }
    }

}
