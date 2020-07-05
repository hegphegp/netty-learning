package com.hegp;

import com.hegp.common.constants.Constants;
import com.hegp.common.invoker.ModuleActionInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ModuleActionInvoker.execute(Constants.USER_MODULE, Constants.USER_MODULE_TEST1);
        nettyServer.bind(9123);
    }
}
