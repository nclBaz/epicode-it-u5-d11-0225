package riccardogulin.u5d11.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riccardogulin.u5d11.payloads.LoginDTO;
import riccardogulin.u5d11.payloads.LoginRespDTO;
import riccardogulin.u5d11.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public LoginRespDTO login(@RequestBody LoginDTO body) {
		String accessToken = authService.checkCredentialsAndGenerateToken(body);
		return new LoginRespDTO(accessToken);
	}

}
