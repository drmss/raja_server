package ir.msisoft.Filters;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ir.msisoft.Sentry.Authentication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class AuthenticationFilter implements Filter {
    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (request.getMethod().equals("OPTIONS") ||
                request.getRequestURI().equals("/login") ||
                request.getRequestURI().equals("/cities") ||
                request.getRequestURI().equals("/search") ||
                request.getRequestURI().equals("/register")) {
            chain.doFilter(req, res);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) res;

        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!Authentication.verifyToken(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int userId = Authentication.getUserId(token);
        if (userId <= 0) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        req.setAttribute("userId", userId);
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}
}
