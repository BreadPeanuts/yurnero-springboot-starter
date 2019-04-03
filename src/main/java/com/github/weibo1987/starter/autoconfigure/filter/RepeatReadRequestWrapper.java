package com.github.weibo1987.starter.autoconfigure.filter;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.http.HttpMethod;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 重写HttpServletRequestWrapper方法.
 *
 * @author huanguan
 */
public class RepeatReadRequestWrapper extends HttpServletRequestWrapper {
    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private final ByteArrayOutputStream cachedContent;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     * @throws IOException if the request is null
     */
    public RepeatReadRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        if (contentLength > 0) {
            if (!isFormPost()) {
                byte[] bytes = IOUtils.toByteArray(request.getInputStream());
                if (bytes.length > 0) {
                    this.cachedContent.write(bytes);
                }
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedContent.toByteArray());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), Charsets.UTF_8));
    }

    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null
            && (contentType.contains(FORM_CONTENT_TYPE) || contentType.contains(MULTIPART_FORM_DATA))
            && HttpMethod.POST.matches(getMethod()));
    }

}
