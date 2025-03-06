package com.example.cruddemo.example;

import com.example.cruddemo.util.AppLogger;
import org.springframework.stereotype.Component;

/**
 * A simple demonstration class for the dual logging system.
 * This class shows how to use the AppLogger utility from any class in the project.
 */
@Component
public class LoggingDemo {

    /**
     * Demonstrates basic logging to both log files
     */
    public void demonstrateBasicLogging() {
        // Log to application.log
        AppLogger.log1Info("LoggingDemo: Basic logging to application.log");
        AppLogger.log1Debug("LoggingDemo: Debug message to application.log");
        AppLogger.log1Warn("LoggingDemo: Warning message to application.log");
        AppLogger.log1Error("LoggingDemo: Error message to application.log");
        
        // Log to secondary.log
        AppLogger.log2Info("LoggingDemo: Basic logging to secondary.log");
        AppLogger.log2Debug("LoggingDemo: Debug message to secondary.log");
        AppLogger.log2Warn("LoggingDemo: Warning message to secondary.log");
        AppLogger.log2Error("LoggingDemo: Error message to secondary.log");
    }
    
    /**
     * Demonstrates conditional logging based on a flag
     * @param useSecondLog flag to determine which log file to use
     */
    public void demonstrateConditionalLogging(boolean useSecondLog) {
        String logTarget = useSecondLog ? "secondary.log" : "application.log";
        
        // Log a message about which log file will be used
        AppLogger.log1Info("LoggingDemo: About to log conditionally to " + logTarget);
        
        // Use conditional logging methods
        AppLogger.logInfo("LoggingDemo: This message goes to " + logTarget, useSecondLog);
        AppLogger.logDebug("LoggingDemo: Debug message to " + logTarget, useSecondLog);
        AppLogger.logWarn("LoggingDemo: Warning message to " + logTarget, useSecondLog);
        AppLogger.logError("LoggingDemo: Error message to " + logTarget, useSecondLog);
    }
    
    /**
     * Demonstrates logging with dynamic data
     * @param data the data to include in log messages
     */
    public void logWithData(String data) {
        // Log to application.log with the data
        AppLogger.log1Info("LoggingDemo: Processing data: " + data);
        
        // Log more detailed information to secondary.log
        AppLogger.log2Info("LoggingDemo: Detailed data processing: " + data);
        AppLogger.log2Debug("LoggingDemo: Data length: " + data.length());
    }
}
