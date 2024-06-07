package logger;

public class Format {
    private boolean showDate;
    private boolean showMessage;
    private boolean showLevel;

    public Format(boolean showDate, boolean showMessage, boolean showLevel) {
        this.showDate = showDate;
        this.showMessage = showMessage;
        this.showLevel = showLevel;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }

    public void setShowLevel(boolean showLevel) {
        this.showLevel = showLevel;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public boolean isShowLevel() {
        return showLevel;
    }
}
