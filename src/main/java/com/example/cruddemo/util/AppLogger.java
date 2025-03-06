package com.example.cruddemo.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Minimal dual logging utility that provides access to two separate log files.
 * This class uses only Lombok's @Slf4j annotation and is accessible throughout the project.
 */
@Slf4j(topic = "log1")
public class AppLogger {

    /**
     * Inner class to access the second logger
     */
    @Slf4j(topic = "log2")
    private static class Log2Holder {
        // This class exists only to hold the log2 logger
    }

    // Static reference to the second logger
    private static final org.slf4j.Logger log2 = Log2Holder.log;

    /**
     * Log to the primary log file (application.log)
     */
    public static void log1Info(String message) {
        log.info(message);
    }
    
    /**
     * Log to the primary log file with formatted message
     */
    public static void log1Info(String format, Object... args) {
        log.info(format, args);
    }

    /**
     * Log to the primary log file with debug level
     */
    public static void log1Debug(String message) {
        log.debug(message);
    }
    
    /**
     * Log to the primary log file with debug level and formatted message
     */
    public static void log1Debug(String format, Object... args) {
        log.debug(format, args);
    }

    /**
     * Log to the primary log file with warn level
     */
    public static void log1Warn(String message) {
        log.warn(message);
    }
    
    /**
     * Log to the primary log file with warn level and formatted message
     */
    public static void log1Warn(String format, Object... args) {
        log.warn(format, args);
    }

    /**
     * Log to the primary log file with error level
     */
    public static void log1Error(String message) {
        log.error(message);
    }
    
    /**
     * Log to the primary log file with error level and formatted message
     */
    public static void log1Error(String format, Object... args) {
        log.error(format, args);
    }

    /**
     * Log to the secondary log file (secondary.log)
     */
    public static void log2Info(String message) {
        log2.info(message);
    }
    
    /**
     * Log to the secondary log file with formatted message
     */
    public static void log2Info(String format, Object... args) {
        log2.info(format, args);
    }

    /**
     * Log to the secondary log file with debug level
     */
    public static void log2Debug(String message) {
        log2.debug(message);
    }
    
    /**
     * Log to the secondary log file with debug level and formatted message
     */
    public static void log2Debug(String format, Object... args) {
        log2.debug(format, args);
    }

    /**
     * Log to the secondary log file with warn level
     */
    public static void log2Warn(String message) {
        log2.warn(message);
    }
    
    /**
     * Log to the secondary log file with warn level and formatted message
     */
    public static void log2Warn(String format, Object... args) {
        log2.warn(format, args);
    }

    /**
     * Log to the secondary log file with error level
     */
    public static void log2Error(String message) {
        log2.error(message);
    }
    
    /**
     * Log to the secondary log file with error level and formatted message
     */
    public static void log2Error(String format, Object... args) {
        log2.error(format, args);
    }

    /**
     * Log to a specific file based on a condition
     */
    public static void logInfo(String message, boolean useSecondLog) {
        if (useSecondLog) {
            log2Info(message);
        } else {
            log1Info(message);
        }
    }
    
    /**
     * Log to a specific file based on a condition with formatted message
     */
    public static void logInfo(String format, boolean useSecondLog, Object... args) {
        if (useSecondLog) {
            log2Info(format, args);
        } else {
            log1Info(format, args);
        }
    }

    /**
     * Log with debug level to a specific file based on a condition
     */
    public static void logDebug(String message, boolean useSecondLog) {
        if (useSecondLog) {
            log2Debug(message);
        } else {
            log1Debug(message);
        }
    }
    
    /**
     * Log with debug level to a specific file based on a condition with formatted message
     */
    public static void logDebug(String format, boolean useSecondLog, Object... args) {
        if (useSecondLog) {
            log2Debug(format, args);
        } else {
            log1Debug(format, args);
        }
    }

    /**
     * Log with warn level to a specific file based on a condition
     */
    public static void logWarn(String message, boolean useSecondLog) {
        if (useSecondLog) {
            log2Warn(message);
        } else {
            log1Warn(message);
        }
    }
    
    /**
     * Log with warn level to a specific file based on a condition with formatted message
     */
    public static void logWarn(String format, boolean useSecondLog, Object... args) {
        if (useSecondLog) {
            log2Warn(format, args);
        } else {
            log1Warn(format, args);
        }
    }

    /**
     * Log with error level to a specific file based on a condition
     */
    public static void logError(String message, boolean useSecondLog) {
        if (useSecondLog) {
            log2Error(message);
        } else {
            log1Error(message);
        }
    }
    
    /**
     * Log with error level to a specific file based on a condition with formatted message
     */
    public static void logError(String format, boolean useSecondLog, Object... args) {
        if (useSecondLog) {
            log2Error(format, args);
        } else {
            log1Error(format, args);
        }
    }
}
