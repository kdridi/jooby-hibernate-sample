package com.arykow.app;

import javax.persistence.*;

@Entity
public class Feed {
	@Id
	@GeneratedValue
	private Long id;
	public Long getId() { return id; }
	private void setId(Long id) { this.id = id; }

	private String userId;
	public String getUserId() { return userId; }
	private void setUserId(String userId) { this.userId = userId; }
	
	private String uri;
	public String getUri() { return uri; }
	private void setUri(String uri) { this.uri = uri; }
	
	private String title;
	public String getTitle() { return title; }
	private void setTitle(String title) { this.title = title; }
	
	public Feed() {
		this(null, null, null);
	}
	
	public Feed(String userId, String uri, String title) {
		this(null, userId, uri, title);
	}
	
	private Feed(Long id, String userId, String uri, String title) {
		super();
		setId(id);
		setUserId(userId);
		setUri(uri);
		setTitle(title);
	}
}
