package ir.sahab.rest.common.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Web filter for authenticating requests. If the requested URL matches with protected URLs (based on SecurityConfigurer
 * config) then it attempts to create the {@link Authentication} from HTTP request using an {@link Authenticator}
 * object.
 */
public final class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Authenticator authenticator;

    AuthenticationFilter(Authenticator authenticator, final RequestMatcher protectedUrls) {
        super(protectedUrls);
        this.authenticator = authenticator;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        return getAuthenticationManager().authenticate(authenticator.authenticate(request));
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        // Delegate request to filters chain in application. If we remove this code, our rest services won't call.
        chain.doFilter(request, response);
    }
}
