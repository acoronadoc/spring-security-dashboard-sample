package com.lostsys.data.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

@Configuration
@EnableWebSecurity

public class Security extends WebSecurityConfigurerAdapter {
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider( new AuthenticationProvider() {
       			 public Authentication authenticate(Authentication authentication) throws AuthenticationException {
 			        String name = authentication.getName();
 			        String password = authentication.getCredentials().toString();
 			        
 			        if ( name.equals("admin") && password.equals("admin") ) {       			            
 			            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
 			            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
 			            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
 			            
 			            return auth;
 			        	}
 			        
       				return null;
       			 	}
       			 
       			 public boolean supports(java.lang.Class<?> authentication) {
       				return authentication.equals(UsernamePasswordAuthenticationToken.class);
       			 	}        			 
       			});
    	}  	

}