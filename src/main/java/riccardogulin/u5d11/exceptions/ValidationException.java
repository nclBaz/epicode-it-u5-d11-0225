package riccardogulin.u5d11.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {
	/*public ValidationException(List<String> errorMessages) {
		super(errorMessages.stream().collect(Collectors.joining(" ")));
	}*/

	private List<String> errorMessages;

	public ValidationException(List<String> errorMessages) {
		super("Errori vari di validazione!");
		this.errorMessages = errorMessages;
	}
}
