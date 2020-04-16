package com.jmx.Test;

import java.util.Properties;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/3
 *  @Time: 11:22
 *  
 */
public class UtilTest {

    public static int getInt123(Properties config, String key, int defaultValue) {
        String val = config.getProperty(key);
        if (val == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Value for configuration key='" + key + "' is not set correctly. " +
                        "Entered value='" + val + "'. Default value='" + defaultValue + "'");
            }
        }
    }


}
