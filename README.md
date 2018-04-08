# KindAptitude: Spring Boot Project

the project is a personal project to learn and practice Spring Boot and Spring 5.
This project has a component "fullStack".

# Goal
The main goal of this personal project is to create a web platform with many components:

 - Management of users
 - Setting up [Google Captcha](https://www.google.com/recaptcha/intro/invisible.html)
 - Payment system with [Stripe](https://stripe.com/fr)
 - Installation of 2fa
 - Navigation Audit
 - Test Strong password
 - Reset password
 - blocks the account at the end of n bad authentication(and notify by email)
 - application available in French and English

##  Stack:
 - Java
 - Spring Boot
 - Thymeleaf
 - HTML5
 - Bootstrap
 - htmlemail


##  How to:

> git clone https://github.com/pi-2r/kindaptitude.git

> cd kindaptitude


**note:** before start this command, you need to import kindaptitude.sql in your mysql Database
> mvn clean package -Dmaven.test.skip=true

> cd target && java -jar project-0.0.1-SNAPSHOT.jar


##  Previews:

![enter image description here](http://image.ibb.co/czMTKx/auth.png)
																				*login page*

![enter image description here](https://image.ibb.co/mSLkXH/2fa2.png)

![enter image description here](http://image.ibb.co/jO7PsH/2fa.png)
	*2FA*

![enter image description here](https://image.ibb.co/kxNXCH/dashboard.png)
	*dashboard*
