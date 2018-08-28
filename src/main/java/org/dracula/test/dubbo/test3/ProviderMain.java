package org.dracula.test.dubbo.test3;

import com.alibaba.dubbo.container.Main;

/**
 * @author dk
 */
public class ProviderMain {

    public static void main(String[] args){
        System.setProperty("dubbo.spring.config", "/META-INF/spring/main.xml");
        Main.main(args);
    }

}
