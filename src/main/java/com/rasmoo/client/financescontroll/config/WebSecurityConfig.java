package com.rasmoo.client.financescontroll.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rasmoo.client.financescontroll.v1.service.UserInfoService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
	return super.authenticationManager();
	}


	@Bean
	public PasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configuracaoGlobal(AuthenticationManagerBuilder auth, UserInfoService userinfo) throws Exception{
        // PasswordEncoder pass = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // auth.inMemoryAuthentication().withUser("vinicius").password(pass.encode("vinicius")).roles("ADM");

		auth.userDetailsService(userinfo).passwordEncoder(this.encoder());
		
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		String[] allowed = new String[] {
				"/webjars","/v1/usuario","/static/**","/v2/api-docs", "/swagger*/**", "/webjars/*"
//				.antMatchers("v2/api-docs", "/swagger*/**", "/webjars/*")
		};
		
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers(allowed).permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
	}
	
}