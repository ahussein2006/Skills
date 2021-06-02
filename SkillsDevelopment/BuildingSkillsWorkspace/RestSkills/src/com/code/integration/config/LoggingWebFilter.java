package com.code.integration.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

@WebFilter("/xyz/*")
public class LoggingWebFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest req = (HttpServletRequest) request;
	RequestWrapper requestWrapper = new RequestWrapper(req);

	HttpServletResponse res = (HttpServletResponse) response;
	ResponseWrapper responseWrapper = new ResponseWrapper(res);

	chain.doFilter(requestWrapper, responseWrapper);

	System.out.println("==================== Request Body =========================");
	System.out.println(requestWrapper.getBody());
	System.out.println("==================== Response Body =========================");
	System.out.println(responseWrapper.getBody());
	res.getOutputStream().write(responseWrapper.getBody().getBytes());
    }

    private class RequestWrapper extends HttpServletRequestWrapper {
	private final String body;

	public RequestWrapper(HttpServletRequest request) throws IOException {
	    super(request);
	    Scanner scanner = new Scanner(request.getInputStream());
	    StringBuilder stringBuilder = new StringBuilder();
	    while (scanner.hasNext())
		stringBuilder.append(scanner.nextLine());
	    body = stringBuilder.toString();
	}

	@Override
	public ServletInputStream getInputStream() {
	    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
	    ServletInputStream servletInputStream = new ServletInputStream() {

		@Override
		public int read() throws IOException {
		    return byteArrayInputStream.read();
		}

		@Override
		public boolean isFinished() {
		    return byteArrayInputStream.available() < 0;
		}

		@Override
		public boolean isReady() {
		    return byteArrayInputStream.available() >= 0;
		}

		@Override
		public void setReadListener(ReadListener arg0) {

		}
	    };
	    return servletInputStream;
	}

	public String getBody() {
	    return body;
	}
    }

    public class ResponseWrapper extends HttpServletResponseWrapper {
	private ByteArrayOutputStream byteArrayOutputStream;
	private PrintWriter printWriter;
	private ServletOutputStream servletOutputStream;
	private boolean usingWriter;

	public ResponseWrapper(HttpServletResponse response) {
	    super(response);
	    byteArrayOutputStream = new ByteArrayOutputStream();
	    printWriter = new PrintWriter(byteArrayOutputStream);
	    servletOutputStream = new ServletOutputStream() {
		@Override
		public void write(int b) throws IOException {
		    byteArrayOutputStream.write(b);
		}

		@Override
		public boolean isReady() {
		    return true;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {

		}
	    };
	    usingWriter = false;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
	    if (usingWriter)
		super.getOutputStream();

	    usingWriter = true;
	    return servletOutputStream;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
	    if (usingWriter)
		super.getWriter();

	    usingWriter = true;
	    return printWriter;
	}

	public String getBody() {
	    return new String(byteArrayOutputStream.toByteArray());
	}
    }
}
