package com.luther.base;

import com.luther.base.intf.Demo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DubboClient {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("dubbo.xml");
        applicationContext.refresh();
        Demo idService = (Demo) applicationContext.getBean("demo");
        System.out.println(idService.demo());
    }
}
