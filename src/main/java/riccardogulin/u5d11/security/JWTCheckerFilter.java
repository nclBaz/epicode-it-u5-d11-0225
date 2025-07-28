package riccardogulin.u5d11.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import riccardogulin.u5d11.exceptions.UnauthorizedException;
import riccardogulin.u5d11.tools.JWTTools;

import java.io.IOException;

@Component // Ricordiamoci di questa annotazione altrimenti non potremo inserire questo filtro nella Security Filter Chain!
public class JWTCheckerFilter extends OncePerRequestFilter {
	// Estendendo OncePerRequestFilter sto "conformando" il mio filtro alla Filter Chain. Sarò costretto ad implementare il metodo
	// astratto ereditato doFilterInternal

	@Autowired
	private JWTTools jwtTools;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// Questo è il metodo che verrà richiamato ad ogni richiesta che dovrà verificare il token (quindi a parte /login e /register)
		// Questo filtro sarà responsabile di recuperare il token, verificare che questo sia valido, se tutto è ok mandare avanti la richiesta
		// al prossimo filtro, in caso invece di problemi, segnalare un errore
		// Una delle caratteristiche interessanti dei filtri, è quella di avere l'accesso a tutte le parti della richiesta e quindi anche agli headers
		// Il token sarà posizionato negli headers (Authorization header)

		// Piano di battaglia
		// 1. Verifichiamo se nella richiesta è presente l'Authorization Header e se esso è ben formato ("Bearer 34j1k2lkjxcljxkjclkj..."), se
		// non c'è oppure se non ha il formato giusto --> 401
		String authHeader = request.getHeader("Authorization"); // "Bearer k1lm2m34lkmxc0898u213lk21nm390.213489us09c.123u91283"
		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Inserire il token nell'Authorization Header nel formato corretto!");

		// 2. Estraiamo il token dall'header
		String accessToken = authHeader.replace("Bearer ", "");

		// 3. Verifichiamo se il token è ok, cioè: controlliamo se è stato manipolato (tramite signature), o se è scaduto (tramite Expiration Date)
		jwtTools.verifyToken(accessToken);

		// 4. Se tutto è OK, passiamo la richiesta al prossimo (che può essere o un filtro o il controller direttamente)
		filterChain.doFilter(request, response); // Tramite .doFilter(req,res) richiamo il prossimo membro della catena (o un filtro o il controller)

		// 5. Se qualcosa non va con il token --> 401
	}

	// Disabilitiamo questo filtro per determinati endpoints tipo /auth/login e /auth/register
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
		// Ignoriamo il filtro per tutte le richieste su http://localhost:3001/auth/.....
	}
}
