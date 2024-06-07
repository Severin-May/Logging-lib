package logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

final class Message {
    private final String timestamp;
    private final LogLevel level;
    private final String message;
    private final String[] params;
    private final int id;
    private static final AtomicInteger ID_GEN = new AtomicInteger(1);

    public Message(LogLevel level, String message, String ...params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(formatter);

        this.level = level;
        this.message = message;
        this.params = params;
        id = ID_GEN.getAndIncrement();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String[] getParams() {
        return params;
    }

    public int getId() {
        return id;
    }
}
