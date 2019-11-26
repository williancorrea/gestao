package br.com.gestao.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class CustomizedRequestLoggingFilter extends AbstractRequestLoggingFilter {
	
	public CustomizedRequestLoggingFilter() {
		this.setIncludePayload(true);
		this.setIncludeQueryString(true);
		this.setMaxPayloadLength(1000);
	}

    @Override
    protected void beforeRequest(HttpServletRequest httpServletRequest, String message) {
    	// do nothing
    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String message) {
    	this.logger.debug(message);
    }
}