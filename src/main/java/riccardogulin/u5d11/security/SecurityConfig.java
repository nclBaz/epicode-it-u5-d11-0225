package riccardogulin.u5d11.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Annotazione che serve per stabilire che questa non sarà una classe di configurazione qualsiasi ma servirà per configurare
// Spring Security
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		// Per poter sovrascrivere i comportamenti di default di Spring Security devo utilizzare questo Bean, il quale mi consentirà di:

		// - disabilitare i comportamenti di default che non mi interessano
		httpSecurity.formLogin(formLogin -> formLogin.disable()); // Non voglio il form di login (avremo React per quello)
		httpSecurity.csrf(csrf -> csrf.disable()); // Disabilito la protezione da CSRF (perché non la utilizziamo ed inoltre
		// complicherebbe sia BE che FE)
		httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		// Non vogliamo utilizzare le sessioni perché JWT non utilizza le sessioni (STATELESS)

		// - personalizzare il comportamento di funzionalità pre-esistenti
		httpSecurity.authorizeHttpRequests(h -> h.requestMatchers("/**").permitAll());
		// Disabilitiamo i vari errori 401/403 che riceviamo di default andando a sproteggere ogni endpoint. Lo facciamo perché andremo
		// ad implementare un meccanismo di autenticazione custom, per il quale stabiliremo noi su quali endpoint intervenire

		// - aggiungere ulteriori meccanismi di autenticazione personalizzati

		return httpSecurity.build();

	}
}
