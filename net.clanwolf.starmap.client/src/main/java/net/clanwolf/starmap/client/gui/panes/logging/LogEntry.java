public class LogEntry {
    private Integer lineNumber;
    private String level;
    private String loggingClass;
    private String loggingClassMethod;
    private String message;

    public Person(Integer lineNumber, String level, String loggingClass, String loggingClassMethod, String message) {
        this.lineNumber = lineNumber;
        this.level = level;
        this.loggingClass = loggingClass;
        this.loggingClassMethod = loggingClassMethod;
        this.message = message;
    }

    public String getLineNumber() {
        return lineNumber;
    }
    public String getLevel() {
        return level;
    }
    public String getLoggingClass() {
        return loggingClass;
    }
    public String getLoggingClassMethod() {
        return loggingClassMethod;
    }
    public String getMessage() {
        return message;
    }
}
