package com.salesmanager.core.business.system.model;

public class ModuleConfig {

	
	private String scheme;
	private String host;
	private String port;
	private String uri;
	private String env;
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getEnv() {
		return env;
	}
	
}
