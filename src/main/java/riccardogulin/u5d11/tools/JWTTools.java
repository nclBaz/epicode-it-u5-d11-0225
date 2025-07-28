package riccardogulin.u5d11.tools;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import riccardogulin.u5d11.entities.User;

import java.util.Date;

@Component
public class JWTTools {
	@Value("${jwt.secret}")
	private String secret;

	public String createToken(User user) {
		// La classe Jwts (proveniente da jjwt-api) ha principalmente 2 metodi: builder() e parser() che useremo rispettivamente per creare e verificare i token

		return Jwts.builder()
				.issuedAt(new Date(System.currentTimeMillis())) // Data di emissione del token (IAT - Issued At), va messa in millisecondi
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza del Token (Expiration Date), anche questa in millisecondi
				.subject(String.valueOf(user.getId())) // Subject, ovvero a chi appartiene il token <-- N.B. MAI METTERE DATI SENSIBILI QUA DENTRO!!!!!!!!!!!!!!
				.signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token tramite un algoritmo specifico che si chiama HMAC, passandogli il segreto!
				.compact(); // Assemblo il tutto ottenendo la stringa finale che sarà il token!
	}

	public void verifyToken(String accessToken) {
	}
}
