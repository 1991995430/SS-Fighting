package com.ss.song.utils;

import javax.annotation.PostConstruct;
import java.util.logging.*;

/**
 * author shangsong 2023/12/20
 */
public class LogUtil {
    private static final Logger LOGGER = Logger.getLogger(LogUtil.class.getName());

    public static void init() {
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(handler);
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }
}
