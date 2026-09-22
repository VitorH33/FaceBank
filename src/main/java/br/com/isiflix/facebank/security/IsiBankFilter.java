package br.com.isiflix.facebank.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class IsiBankFilter extends OncePerRequestFilter {

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		

		if (request.getHeader("Authorization") != null) {
			Authentication auth = TokenUtil.decode(request);
			if (auth != null) {
				
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			else {
				response.setStatus(401);
				response.getWriter().println("Acesso Nao Autorizado");
				return;
			}
		}		
		chain.doFilter(request, response);
	}

}