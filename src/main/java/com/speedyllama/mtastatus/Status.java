package com.speedyllama.mtastatus;

public class Status {
	private String line, status, title, detail;

	public Status(String line, String status, String title, String detail) {
		super();
		this.status = status;
		this.line = line;
		this.title = title;
		this.detail = detail;
	}

	public String getLine() {
		return line;
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	@Override
	public String toString() {
		return "Status [line=" + line + ", title=" + title + ", detail=" + detail + "]";
	}
}
