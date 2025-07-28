package riccardogulin.u5d11.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import riccardogulin.u5d11.entities.User;
import riccardogulin.u5d11.exceptions.UnauthorizedException;
import riccardogulin.u5d11.payloads.LoginDTO;
import riccardogulin.u5d11.tools.JWTTools;

@Service
public class AuthService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private JWTTools jwtTools;

	public String checkCredentialsAndGenerateToken(LoginDTO body) {
		// 1. Controllo credenziali
		// 1.1 Cerco nel DB se esiste uno user con quella email (quella fornita nel body)
		User found = this.usersService.findByEmail(body.email());
		// 1.2 Se esiste, verifico se la sua pw combacia con quella fornita nel body

		// 1.3 Se una delle 2 verifiche non va a buon fine --> punto 4
		if (found.getPassword().equals(body.password())) {
			// 2. Se sono OK --> Generiamo il Token
			String accessToken = jwtTools.createToken(found);
			// 3. Ritorno il Token
			return accessToken;
		} else {
			// 4. Se le credenziali sono errate --> 401 (Unauthorized)
			throw new UnauthorizedException("Credenziali errate!");
		}
	}
}
