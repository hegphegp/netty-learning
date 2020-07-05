package com.hegp;

import com.hegp.common.constants.Constants;
import com.hegp.common.invoker.ModuleActionInvoker;
import com.hegp.init.InitModuleActionMethod;

public class ClientApp {

    public static void main(String[] args) throws Exception {
        InitModuleActionMethod.init();
        ModuleActionInvoker.execute(Constants.USER_MODULE, Constants.USER_MODULE_TEST1);
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connect("127.0.0.1", 9123);

        ClientMsgThread thread = new ClientMsgThread(clientConfig.getChannel());
        thread.start();
    }
}
