package com.test;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

public class TestInfo {

    @Test
    public void testHashMd5() {
        String salt = "qwerty";
        Md5Hash md5Hash = new Md5Hash("111111",salt,1);
        String password = md5Hash.toString();
        System.out.println(password.equalsIgnoreCase("f3694f162729b7d0254c6e40260bf15c"));
    }
}
