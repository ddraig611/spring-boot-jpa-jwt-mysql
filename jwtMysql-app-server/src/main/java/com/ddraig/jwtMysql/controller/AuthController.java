package com.ddraig.jwtMysql.controller;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ddraig.jwtMysql.DTO.RoleDTO;
import com.ddraig.jwtMysql.entity.Role;
import com.ddraig.jwtMysql.entity.RoleName;
import com.ddraig.jwtMysql.entity.User;
import com.ddraig.jwtMysql.exception.AppException;
import com.ddraig.jwtMysql.exception.ResourceNotFoundException;
import com.ddraig.jwtMysql.model.ApiResponse;
import com.ddraig.jwtMysql.model.JwtAuthenticationResponse;
import com.ddraig.jwtMysql.model.LoginRequest;
import com.ddraig.jwtMysql.model.SignUpRequest;
import com.ddraig.jwtMysql.security.JwtTokenProvider;
import com.ddraig.jwtMysql.service.RoleService;
import com.ddraig.jwtMysql.service.UserService;
import com.ddraig.jwtMysql.util.RestResult;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;

	@Autowired
	private JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Set role user - you have enter input role in signUpRequest and create user with other role
        Role userRole = roleService.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));

        User result = userService.CreateUser(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/assignrole")
	public ResponseEntity<RestResult> assignRoles(@RequestBody RoleDTO roleDto) {
		// get user
		User user = userService.findByEmail(roleDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", roleDto.getEmail()));
		String[] lstRole = null;
		if (roleDto.getName().contains(";")) {
			lstRole = roleDto.getName().split(";");
		}
		if (lstRole != null) {
			for(String role : lstRole) {
				if (role != "") {
					Role roleUser = roleService.findByName(RoleName.valueOf(role))
							.orElseThrow(() -> new AppException("User Role not set."));
			        user.setRoles(Collections.singleton(roleUser));
				}
			}
		}

		User resultUser = userService.CreateUser(user);
		RestResult result = new RestResult("0", "createRoles", resultUser);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/createrole")
    public ResponseEntity<RestResult> createRoles(@RequestParam String roleName) {
    	Optional<Role> userRole = roleService.findByName(RoleName.valueOf(roleName));
		if (userRole == null) {
			Role role = roleService.createRole(roleName);
			RestResult result = new RestResult("0", "createRoles", role);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else {
			RestResult result = new RestResult("1", "Role đã tồn tại", new Object());
			return new ResponseEntity<>(result, HttpStatus.CONFLICT);
		}
    }
}
