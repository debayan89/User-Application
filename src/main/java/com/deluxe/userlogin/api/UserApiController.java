package com.deluxe.userlogin.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.deluxe.userlogin.jwt.JwtEncoder;
import com.deluxe.userlogin.model.Token;
import com.deluxe.userlogin.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2020-11-04T20:36:26.436Z")

@Controller
public class UserApiController implements UserApi {

    private static final Logger log = LoggerFactory.getLogger(UserApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;
    
    @Autowired
    RestTemplate restTemplate;

    @org.springframework.beans.factory.annotation.Autowired
    public UserApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createUser(@ApiParam(value = "Created user object" ,required=true )  @Valid @RequestBody User body) {
        if(null == body) {
        	return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <User> entity = new HttpEntity<User>(body, headers);
        
        Integer id = restTemplate.exchange("http://localhost:8080/v1/user/add", HttpMethod.POST, entity, Integer.class).getBody();
        
        if(null != id) {
        	return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Token> loginUser(@NotNull @ApiParam(value = "The user name for login", required = true) @Valid @RequestParam(value = "username", required = true) String username,@NotNull @ApiParam(value = "The password for login in clear text", required = true) @Valid @RequestParam(value = "password", required = true) String password) {
    	if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    	
        final String baseUrl = "http://localhost:8080/v1/user/login?username=" + username + "&password=" + password;
		try {
			URI uri = new URI(baseUrl);
			String id = restTemplate.getForEntity(uri, String.class).getBody();
			 if(!StringUtils.isEmpty(id)) {
				 String token = JwtEncoder.createJWT(username);
				 Token tokenModel = new Token();
				 tokenModel.setToken(token);
				 log.info(token);
		        return new ResponseEntity<Token>(tokenModel, HttpStatus.OK);
		     }
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (URISyntaxException e) {
			log.error("Issue with URI", e);
		}
      
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<Void> logoutUser() {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
