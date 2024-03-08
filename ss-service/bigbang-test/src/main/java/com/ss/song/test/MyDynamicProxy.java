package com.ss.song.test;

import java.lang.reflect.Proxy;

public class MyDynamicProxy {

    public static void main(String[] args) {
        HelloImpl hello = new HelloImpl();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(hello);
        Hello  proxyHello = (Hello)Proxy.newProxyInstance(HelloImpl.class.getClassLoader(), new Class[]{Hello.class} , myInvocationHandler);
        proxyHello.sayHello();
    }

}
