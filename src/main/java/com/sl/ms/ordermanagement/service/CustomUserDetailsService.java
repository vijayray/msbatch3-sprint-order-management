package com.sl.ms.ordermanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class.getName());
	
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    	return new User("foo", "foo",new ArrayList<>());
    }
}