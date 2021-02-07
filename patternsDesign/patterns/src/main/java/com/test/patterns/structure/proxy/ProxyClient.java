package com.test.patterns.structure.proxy;

import com.test.patterns.service.OrderService;
import com.test.patterns.service.impl.OutOrderServiceImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理模式
 * 代售进口水果
 */
public class ProxyClient {
    public static void main(String[] args) throws Exception {
        saveOrder();
    }

    private static void saveOrder() throws Exception {
        //本地代理类
        //静态代理：
        OrderService orderService = new ProxyOrder();
        orderService.saveOrder();

//        OrderService proxy = (OrderService) loadProxy(new OutOrderServiceImpl());
        OrderService proxy = (OrderService) loadProxy2(new OutOrderServiceImpl());

        proxy.saveOrder();
        proxy.first();
        proxy.second();
        proxy.third();

        //其它业务代码。。。。
    }

    public static Object loadProxy(final Object target) throws Exception {
        Class<?> proxyClass = Proxy.getProxyClass(target.getClass().getClassLoader(), target.getClass().getInterfaces());

        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);

        Object proxy = constructor.newInstance(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                //下单前添加操作
                System.out.println("添加下单前操作AAAAA");
                Object result = method.invoke(target, args);
                return result;
            }
        });

        return proxy;

    }

    public static Object loadProxy2(final Object object) {
        return Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        //会有传入的Object的相关方法调用，本例 OutOrderServiceImpl 中的
                        //saveOrder first second third 这四个方法会传过来
                        Object result = null;
                        if (method.getName().equals("second")) {
                            //仅在方法名为 second 的前后添加自己的特殊逻辑
                            System.out.println("before *************");
                            result = method.invoke(object, args);
                            System.out.println("after -------------");
                        } else {
                            result = method.invoke(object, args);
                        }


                        return result;
                    }
                });
    }


}
