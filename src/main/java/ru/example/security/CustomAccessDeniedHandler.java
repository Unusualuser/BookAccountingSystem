package ru.example.security;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    public static final Logger LOGGER = Logger.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            LOGGER.warn("Пользователь: " + auth.getName()
                    + " пытался получить доступ к защищённому URL: "
                    + request.getRequestURI());
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        response.getWriter().write("{\"error\": \"У пользователя не хватает прав доступа для выполнения данной операции.\"}");
    }
}
