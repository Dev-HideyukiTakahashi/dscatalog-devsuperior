# Bootcamp Dev-Superior
---

## Anotações CRUD - cap 01

### Arquivos de configuração

#### application.properties
```
spring.profiles.active=test

spring.jpa.open-in-view=false
```

#### application-test.properties
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### application-dev.properties
```
spring.jpa.properties.javax.persistence.schema-generation.create-source=metadata
spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create
spring.jpa.properties.javax.persistence.schema-generation.scripts.create-target=create.sql
spring.jpa.properties.hibernate.hbm2ddl.delimiter=;

spring.datasource.url=jdbc:postgresql://localhost:5432/dspesquisa
spring.datasource.username=postgres
spring.datasource.password=1234567

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.hibernate.ddl-auto=none
```

#### application-prod.properties
```
spring.datasource.url=${DATABASE_URL}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
```


---
## Anotações Security / OAuth - cap 03
### Classe de configuração
```
@Configuration
public class AppConfig {

	// Bean para criptografar a senha
	// Exemplo de utilização
	// user.setPassword(passwordEncoder.encode(dto.getPassword()));
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
```
### Configuração provisória para liberar todos endpoints
```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	// web.ignoring().antMatchers("/actuator/**");  -> Configuração para biblioteca OAuth funcionar
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**");
	}
}
```
---
### Referências sobre Bean Validation

https://beanvalidation.org/
https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/overview-summary.html
https://docs.jboss.org/hibernate/beanvalidation/spec/2.0/api/javax/validation/constraints/package-summary.html
https://www.baeldung.com/java-bean-validation-not-null-empty-blank
https://www.baeldung.com/spring-custom-validation-message-source
https://pt.stackoverflow.com/questions/133691/formatar-campo-cpf-ou-cnpj-usando-regex
https://regexlib.com/
https://regexr.com/

---
### Spring Security

##### Interfaces que devem ser implementadas
* UserDetails
	
```
@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
                      .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                      .toList();
	} 
```

* UserDetailsService:

    
```
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		
		if(user == null) {
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException ("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
```

* Essas interfaces são implementadas na entidade 'User' e no serviço 'UserService'

* Classe para configuração de segurança web
	* WebSecurityConfigurerAdapter

* Bean para efetuar autenticação
	* AuthenticationManager
---
### Spring Cloud OAuth2
* Classe de configuração para Authorization Server
	* AuthorizationServerConfigurerAdapter

* Classe de configuração para Resource Server
	* ResourceServerConfigurerAdapter
		* Liberando o h2-console

```
private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**" };
	@Autowired
	private Environment env;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		//considerando que o profile do application.properties está ativo como "test"
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
			.antMatchers(PUBLIC).permitAll();
```

* Beans para implementar o padrão JWT
	* JwtAccessTokenConverter
	* JwtTokenStore

---
### Logger
```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
private static Logger logger = LoggerFactory.getLogger(Exemplo.class);

// Exemplo de aplicação para aparecer no console
logger.error("User not found " + username);
logger.info("User found: " + username);
```
