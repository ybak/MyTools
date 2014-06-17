package org.isaac.exception;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * VM Args: -Xms20M -XX:MaxDirectMemorySize=10M
 * 
 */
public class DirectMemoryOOm {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        Field unsafeFiled = Unsafe.class.getDeclaredFields()[0];
        unsafeFiled.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeFiled.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }
    }
}
