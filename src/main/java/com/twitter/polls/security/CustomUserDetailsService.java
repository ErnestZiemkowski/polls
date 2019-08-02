package com.twitter.polls.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.twitter.polls.dao.UserDao;
import com.twitter.polls.model.User;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserDao userDAO;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		User user = userDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail));
		return UserPrincipal.create(user);
	}
	
	@Transactional
	public UserDetails loadUserById(Long id) {
		User user = userDAO.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));
		
		return UserPrincipal.create(user);
	}

}
