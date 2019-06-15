package com.hegp;

import com.hegp.invoker.ModuleInfoInvoker;
import com.hegp.modules.UserModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private UserModule userModule;
    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        userModule.test1();
        ModuleInfoInvoker.execute("userModule", "test1");
        nettyServer.setPort(9123).start();
    }
}
