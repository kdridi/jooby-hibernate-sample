package com.arykow.app;

public class FeedAddForm {
	private final String location;

	public FeedAddForm(String location) {
		super();
		this.location = location;
	}

	@Override
	public String toString() {
		return "FeedAddForm [location=" + location + "]";
	}

	public String getLocation() {
		return this.location;
	}
}