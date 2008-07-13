package net.deuce.cl.service;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompressionFilter implements Filter {
    private FilterConfig config = null;

    private int compressionThreshold;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        compressionThreshold = 0;
        if (config != null) {
            String str = config.getInitParameter("compressionThreshold");
            if (str != null) {
                compressionThreshold = Integer.parseInt(str);
            }
        }
    }

    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        boolean supportCompression = false;
        if (request instanceof HttpServletRequest) {
            Enumeration e = ((HttpServletRequest) request)
                .getHeaders("Accept-Encoding");
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                if (name.indexOf("gzip") != -1) {
                    supportCompression = true;
                }
            }
        }
        if (!supportCompression) {
            chain.doFilter(request, response);
        } else {
            if (response instanceof HttpServletResponse) {
                CompressionResponseWrapper wrappedResponse = new CompressionResponseWrapper(
                    (HttpServletResponse) response);
                wrappedResponse.setCompressionThreshold(compressionThreshold);
                chain.doFilter(request, wrappedResponse);
            }
        }
    }
}