package com.speedyllama.mtastatus;

public class Status {
	private String line, title, detail;
	private TrainStatus status;

	public Status(String line, TrainStatus status, String title, String detail) {
		super();
		this.status = status;
		this.line = line;
		this.title = title;
		this.detail = detail;
	}

	public String getLine() {
		return line;
	}

	public TrainStatus getStatus() {
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
