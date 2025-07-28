package riccardogulin.u5d11.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import riccardogulin.u5d11.entities.User;
import riccardogulin.u5d11.exceptions.ValidationException;
import riccardogulin.u5d11.payloads.LoginDTO;
import riccardogulin.u5d11.payloads.LoginRespDTO;
import riccardogulin.u5d11.payloads.NewUserDTO;
import riccardogulin.u5d11.payloads.NewUserRespDTO;
import riccardogulin.u5d11.services.AuthService;
import riccardogulin.u5d11.services.UsersService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private AuthService authService;
	@Autowired
	private UsersService usersService;

	@PostMapping("/login")
	public LoginRespDTO login(@RequestBody LoginDTO body) {
		String accessToken = authService.checkCredentialsAndGenerateToken(body);
		return new LoginRespDTO(accessToken);
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public NewUserRespDTO save(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
		if (validationResult.hasErrors()) {
			//validationResult.getFieldErrors().forEach(fieldError -> System.out.println(fieldError.getDefaultMessage()));
			throw new ValidationException(validationResult.getFieldErrors()
					.stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
		} else {
			User newUser = this.usersService.save(payload);
			return new NewUserRespDTO(newUser.getId());
		}

	}

}
