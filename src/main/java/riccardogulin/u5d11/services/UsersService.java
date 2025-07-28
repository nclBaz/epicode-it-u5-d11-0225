package riccardogulin.u5d11.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import riccardogulin.u5d11.entities.User;
import riccardogulin.u5d11.exceptions.BadRequestException;
import riccardogulin.u5d11.exceptions.NotFoundException;
import riccardogulin.u5d11.payloads.NewUserDTO;
import riccardogulin.u5d11.repositories.UsersRepository;

import java.util.UUID;

@Service
@Slf4j
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;


	public User save(NewUserDTO payload) {
		// 1. Verifico che l'email non sia già in uso
		this.usersRepository.findByEmail(payload.email()).ifPresent(user -> {
			throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
		});

		// 2. Aggiungo valori server-generated
		User newUser = new User(payload.name(), payload.surname(), payload.email(), payload.password());
		newUser.setAvatarURL("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());

		// 3. Salvo
		User savedUser = this.usersRepository.save(newUser);

		// 4. Log
		log.info("L'utente con id: " + savedUser.getId() + " è stato salvato correttamente!");

		// 5. Ritorno l'utente salvato
		return savedUser;
	}

	public Page<User> findAll(int pageNumber, int pageSize, String sortBy) {
		if (pageSize > 50) pageSize = 50;
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).descending());
		return this.usersRepository.findAll(pageable);
	}

	public User findById(UUID userId) {
		return this.usersRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
	}

	public User findByIdAndUpdate(UUID userId, NewUserDTO payload) {
		// 1. Cerco l'utente nel db
		User found = this.findById(userId);

		// 2. Controllo che la nuova email non sia già in uso
		if (!found.getEmail().equals(payload.email())) // Il controllo dell'email lo faccio solo quando effettivamente mi sta passando una nuova email
			this.usersRepository.findByEmail(payload.email()).ifPresent(user -> {
				throw new BadRequestException("L'email " + user.getEmail() + " è già in uso!");
			});

		// 3. Modifico l'utente trovato nel db
		found.setName(payload.name());
		found.setSurname(payload.surname());
		found.setEmail(payload.email());
		found.setPassword(payload.password());
		found.setAvatarURL("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());

		// 4. Salvo
		User modifiedUser = this.usersRepository.save(found);

		// 5. Log
		log.info("L'utente con id " + found.getId() + " è stato modificato!");

		// 6. Return dell'utente modificato
		return modifiedUser;
	}

	public void findByIdAndDelete(UUID userId) {
		User found = this.findById(userId);
		this.usersRepository.delete(found);
	}

	public User findByEmail(String email) {
		return this.usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
	}

}
