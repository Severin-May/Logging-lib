package logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {

    private final static Logger instance = new Logger();

    private final AtomicInteger lineCount = new AtomicInteger(0);
    private final AtomicInteger fileCount = new AtomicInteger(0);

    private final ArrayList<Message> errors = new ArrayList<>();
    // Configuration default values
    private boolean consoleEnabled = true;
    private final Format consoleMessageFormat = new Format(true, true, true);
    private LogLevel consoleLogLevel = LogLevel.DEBUG;
    private boolean logIntoFileEnabled = true;
    private long logFileCapacity = 100;
    private LogLevel fileLogLevel = LogLevel.DEBUG;
    private final Format fileMessageFormat = new Format(true, true, true);

    private String CONFIG_FILE_NAME = "src/main/resources/log-config.json";
    private final String LOG_OUTPUT_LOCATION = "src/main/resources/log-outputs";

    private Logger() {
        cleanLogDir();
        readConfigFromFile();
    }

//    /**
//     * The user of the library can pass a path to a config file
//     * @param pathToFileName
//     */
//    public void readConfigFile (String pathToFileName) {
//        CONFIG_FILE_NAME = pathToFileName;
//        readConfigFromFile();
//    }

    public static Logger getLogger() {
        return instance;
    }

    /**
     * logs messages to console/file if enabled in config file
     * @param level log level
     * @param message log message
     * @param params optional parameters
     */
    public void log(LogLevel level, String message, String ...params) {
        final Message m = new Message(level, message, params);
        if(consoleEnabled && consoleLogLevel.ordinal() <= level.ordinal()) {
            logToConsole(m);
        }
        if(logIntoFileEnabled && fileLogLevel.ordinal() <= level.ordinal()) {
            logToFile(m);
        }
    }

    /**
     * logs messages of level Debug
     * @param message message
     */
    public void logDebug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * logs messages of level Info
     * @param message message
     */
    public void logInfo(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * logs messages of level Warning
     * @param message message
     */
    public void logWarning(String message) {
        log(LogLevel.WARNING, message);
    }

    /**
     * logs messages of level Error
     * @param message message
     */
    public void logError(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * prints all the error messages to console
     */
    public void getErrors() {
        errors.forEach(e -> System.out.println(e.getTimestamp() + " " + e.getLevel() + " " + e.getId() + " " + e.getMessage()));
    }

    /**
     * clears the error by the given id
     * @param id if of the error
     */
    public void clear(int id) {
        errors.forEach((e) -> {
            if(e.getId() == id) {
                errors.remove(e);
            }
        });
    }

    /**
     * cleans the directory of log files
     */
    private void cleanLogDir() {
        File directory = new File(LOG_OUTPUT_LOCATION);
        for (File file: Objects.requireNonNull(directory.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    /**
     * logs messages to console with the specified format from config file
     * @param m message
     */
    private void logToConsole (Message m) {
        String finalMessage = "";
        if (consoleMessageFormat.isShowDate()) {
            finalMessage += m.getTimestamp() + " ";
        }
        if (consoleMessageFormat.isShowLevel()) {
            finalMessage += m.getLevel() + " ";
        }
        if (consoleMessageFormat.isShowMessage()) {
            finalMessage += m.getMessage() + " ";
        }

        if(!finalMessage.isBlank()) {
            System.out.println(finalMessage);
        }
    }

    /**
     * logs messages to file with the specified format from config file
     * @param m message
     */
    private void logToFile (Message m) {
        String finalMessage = "";
        if (fileMessageFormat.isShowDate()) {
            finalMessage += m.getTimestamp() + " ";
        }
        if (fileMessageFormat.isShowLevel()) {
            finalMessage += m.getLevel() + " ";
        }
        if (fileMessageFormat.isShowMessage()) {
            finalMessage += m.getMessage() + " ";
        }

        if(!finalMessage.isBlank()) {
            writeIntoFile(finalMessage);
        }
    }

    /**
     * handles writing of the message into a file & creates new log file when the capacity is reached
     * @param message message
     */
    private void writeIntoFile(String message) {
        try(FileWriter fileWriter = new FileWriter(LOG_OUTPUT_LOCATION + "/log" + fileCount.get() + ".txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(message);
            lineCount.getAndIncrement();
            if(lineCount.get() > logFileCapacity) {
                fileCount.getAndIncrement();
                lineCount.set(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads all config details from config file,
     * otherwise default values of config will be used
     */
    private void readConfigFromFile() {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(CONFIG_FILE_NAME)) {
            Object obj = jsonParser.parse(reader);
            JSONObject config = (JSONObject) obj;

            Boolean consoleLogEnabled = (Boolean) config.get("consoleLogEnabled");
            if (consoleLogEnabled != null) {
                this.consoleEnabled = consoleLogEnabled;
            }
            JSONObject consoleFormat = (JSONObject) config.get("consoleMessageFormat");
            convertJsonConsoleFormat(
                    (Boolean) consoleFormat.get("showDate"),
                    (Boolean) consoleFormat.get("showMessage"),
                    (Boolean) consoleFormat.get("showLevel")
            );
            //TODO: check for NULL case
            this.consoleLogLevel = LogLevel.valueOf((String) config.get("consoleLogLevel"));
            Boolean logIntoFileEnabled = (Boolean) config.get("logIntoFileEnabled");
            if (logIntoFileEnabled != null) {
                this.logIntoFileEnabled = logIntoFileEnabled;
            }
            Long logFileCapacity = (Long) config.get("logFileCapacity");
            if (logFileCapacity != null) {
                this.logFileCapacity = logFileCapacity;
            }
            this.fileLogLevel = LogLevel.valueOf((String) config.get("fileLogLevel"));
            JSONObject fileFormat = (JSONObject) config.get("fileMessageFormat");
            convertJsonFileFormat(
                    (Boolean) fileFormat.get("showDate"),
                    (Boolean) fileFormat.get("showMessage"),
                    (Boolean) fileFormat.get("showLevel")
            );
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void convertJsonFileFormat(Boolean showDate, Boolean showMessage, Boolean showLevel) {
        if (showDate != null) {
            fileMessageFormat.setShowDate(showDate);
        }
        if (showMessage != null) {
            fileMessageFormat.setShowMessage(showMessage);
        }
        if (showLevel != null) {
            fileMessageFormat.setShowLevel(showLevel);
        }
    }

    private void convertJsonConsoleFormat(Boolean showDate, Boolean showMessage, Boolean showLevel) {
        if (showDate != null) {
            consoleMessageFormat.setShowDate(showDate);
        }
        if (showMessage != null) {
            consoleMessageFormat.setShowMessage(showMessage);
        }
        if (showLevel != null) {
            consoleMessageFormat.setShowLevel(showLevel);
        }
    }
}
