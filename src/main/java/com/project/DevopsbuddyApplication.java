package com.project;

import com.project.backend.service.MailClient;
import com.project.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableDiscoveryClient
//@EnableEurekaClient
public class DevopsbuddyApplication implements CommandLineRunner {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(DevopsbuddyApplication.class);

	@Autowired
	private UserService userService;

	@Value("${webmaster.username}")
	private String webmasterUsername;

	@Value("${webmaster.password}")
	private String webmasterPassword;

	@Value("${webmaster.email}")
	private String webmasterEmail;

    @Autowired
    private MailClient mailClient;

	public static void main(String[] args) {
		LOG.info("start spring boot application");
		SpringApplication.run(DevopsbuddyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
        //Locale locale =new Locale();
        //mailClient.prepareAndSend("pierre.therrode@gmail.com", "alerte zone de connexion", "welcome", locale);
	/*	User user = UserUtils.createBasicUser(webmasterEmail);
		Company company =  new Company();
		company.setCompanyName("kindaptitude");
		company.setWebSite("http://mon-personal-mba.fr/articles/methodes/");
		company.setBusinessType("corporation");
		company.setSiren("824117055");
		company.setTvaNumber("FR45 824117055");
		company.setStreet("toto");
		company.setZipCode("toto");
		company.setTown("tutu");
		company.setCountry("toto");
		company.setContactPersonEmail("contact@kindaptitude.com");
		company.setPhoneNumberContact("0660383693");

		user.setPassword(webmasterPassword);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, new Role(RolesEnum.ADMIN)));

		LOG.debug("Creating user with username {}", user.getUsername());
		userService.createUser(user, PlansEnum.USER, userRoles, company);
		LOG.info("User {} created", user.getUsername());
		*/

	}
}