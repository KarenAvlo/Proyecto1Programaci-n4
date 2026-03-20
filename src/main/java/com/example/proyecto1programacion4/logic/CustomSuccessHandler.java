package com.example.proyecto1programacion4.logic;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            System.out.println("Admin Logeado, cargando HTML");
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_EMPRESA")) {
            System.out.println("Empresa Logeado, cargando HTML");
            response.sendRedirect("presentation/empresa/dashboard");
        } else if (roles.contains("ROLE_OFERENTE")) {
            System.out.println("Oferente Logeado, cargando HTML");
            response.sendRedirect("presentation/oferente/show");
        } else {
            response.sendRedirect("/"); // Ruta por defecto
        }
    }

}
