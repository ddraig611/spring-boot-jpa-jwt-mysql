package com.ddraig.jwtMysql.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddraig.jwtMysql.DTO.RoleDTO;
import com.ddraig.jwtMysql.DTO.UserProfile;
import com.ddraig.jwtMysql.entity.Role;
import com.ddraig.jwtMysql.entity.RoleName;
import com.ddraig.jwtMysql.entity.User;
import com.ddraig.jwtMysql.exception.ResourceNotFoundException;
import com.ddraig.jwtMysql.model.PollResponse;
import com.ddraig.jwtMysql.model.UserIdentityAvailability;
import com.ddraig.jwtMysql.model.UserSummary;
import com.ddraig.jwtMysql.repository.PollRepository;
import com.ddraig.jwtMysql.repository.VoteRepository;
import com.ddraig.jwtMysql.security.CurrentUser;
import com.ddraig.jwtMysql.security.UserPrincipal;
import com.ddraig.jwtMysql.service.PollService;
import com.ddraig.jwtMysql.service.RoleService;
import com.ddraig.jwtMysql.service.UserService;
import com.ddraig.jwtMysql.util.AppConstants;
import com.ddraig.jwtMysql.util.PagedResponse;
import com.ddraig.jwtMysql.util.RestResult;

@RestController
@RequestMapping("/api")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService rollService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    //hasAnyAuthority('CUSTOMER', 'ADMIN')
    //@PreAuthorize("isAuthenticated() || hasRole('ROLE_USER')")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userService.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userService.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);

        return userProfile;
    }
    
    @GetMapping("/users/role/{rolename}")
    public ResponseEntity<RestResult> getUserByRoleName(@PathVariable(value = "rolename") RoleName rolename) {
	    try {
	    	List<User> users = userService.findByRolesRoleName(rolename);
			RestResult result = new RestResult("0", "Successful", users);
			logger.info("<======End call findByRolesRoleName method=========>");
			return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (DataAccessException ex) {
			logger.error(ex.getLocalizedMessage());
	        return null;
	    }
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
