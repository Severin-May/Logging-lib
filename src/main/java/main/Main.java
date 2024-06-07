package main;

import logger.Logger;

public class Main {
    public static void main(String[] args) {

        Logger logger = Logger.getLogger();
        int i = 0;
        while (i < 100){
            logger.logDebug("MessageDebug" + i++);
            logger.logInfo("MessageInfo" + i++);
        }
    }
}
