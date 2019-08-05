package com.twitter.polls.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.polls.dao.PollDAO;
import com.twitter.polls.dao.UserDAO;
import com.twitter.polls.dao.VoteDAO;
import com.twitter.polls.exception.ResourceNotFoundException;
import com.twitter.polls.model.User;
import com.twitter.polls.payload.PagedResponse;
import com.twitter.polls.payload.PollResponse;
import com.twitter.polls.payload.UserIdentityAvailability;
import com.twitter.polls.payload.UserProfile;
import com.twitter.polls.payload.UserSummary;
import com.twitter.polls.security.CurrentUser;
import com.twitter.polls.security.UserPrincipal;
import com.twitter.polls.service.PollService;
import com.twitter.polls.util.AppConstants;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private PollDAO pollDAO;
	
	@Autowired
	private VoteDAO voteDAO;
	
	@Autowired
	private PollService pollService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
		return userSummary;
	}
	
	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userDAO.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}
	
	@GetMapping("/user/checkEmailAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userDAO.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}
	
	@GetMapping("/users/{username}")
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userDAO.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
		
		long pollCount = pollDAO.countByCreatedBy(user.getId());
		long voteCount = voteDAO.countByUserId(user.getId());
		
		UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);
		
		return userProfile;
	}
	
	@GetMapping("/users/{username}/polls")
	public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
														 @CurrentUser UserPrincipal currentUser,
														 @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
														 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getPollsCreatedBy(username, currentUser, page, size);
	}

	@GetMapping("/users/{username}/votes")
	public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
													   @CurrentUser UserPrincipal currentUser,
													   @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
													   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getPollsVotedBy(username, currentUser, page, size);
	}
		
}


