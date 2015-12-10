package com.speedyllama.mtastatus;

public class Alert {
    public String timestamp, line, status, title, detail;

    public Alert(String timestamp, String line, String status, String title, String detail) {
        this.timestamp = timestamp;
        this.line = line;
        this.status = status;
        this.title = title;
        this.detail = detail;
    }
    
    public Alert(String timestamp, String line, String status) {
        this(timestamp, line, status, "", "");
    }
    
    public Alert() {}

	@Override
	public String toString() {
		return "Alert [timestamp=" + timestamp + ", line=" + line + ", status=" + status + ", title=" + title
				+ ", detail=" + detail + "]";
	}

	@Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Alert)) {
            return false;
        }
        Alert other = (Alert)obj;
        return other.line.equals(line) &&
                other.status.equals(status) &&
                other.title.equals(title) &&
                other.detail.equals(detail);
    }
}