package be.iccbxl.pid.reservations_springboot.validation;

import be.iccbxl.pid.reservations_springboot.dto.UserProfileDto;
import be.iccbxl.pid.reservations_springboot.dto.UserRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		String pwd = null;
		String confirm = null;

		if (obj instanceof UserRegistrationDto) {
			pwd = ((UserRegistrationDto) obj).getPassword();
			confirm = ((UserRegistrationDto) obj).getConfirmPassword();
		} else if (obj instanceof UserProfileDto) {
			pwd = ((UserProfileDto) obj).getPassword();
			confirm = ((UserProfileDto) obj).getConfirmPassword();
		} else {
			return true;
		}

		// Si les deux champs sont vides, on considère que le mot de passe n’est pas modifié => OK
		if ((pwd == null || pwd.isBlank()) && (confirm == null || confirm.isBlank())) {
			return true;
		}

		// Sinon, il faut qu’ils soient identiques
		if (pwd == null) return false;
		return pwd.equals(confirm);
	}
}

