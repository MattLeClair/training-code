package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.model.User;
import com.revature.service.UserService;

/*
 * Now, we take a look at @RestController
 * Which, as does @Controller, registers a class
 * as a controller (can handle HTTP requests)
 * One difference is that because @Controller can 
 * return view names, we must annotate every 
 * method that returns an actual HTTP response 
 * (and not a view name to then be resolved) 
 * with @ResponseBody, whereas, with @RestController
 * we do not
 */

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService service;
	
	/*
	 * A bit more on @RequestMapping 
	 * Applied to a class, if you give it a urlpattern, every method
	 * in the class (that also has the @RequestMapping tag) will be found
	 * at the context root of the URL pattern 
	 * 
	 * If applied to a method:
	 * - the default HTTP method is GET
	 * - we do not need to have another url pattern value if one is applied
	 * to the class, but if we do, the method will be reached 
	 * at /classPattern/methodPattern
	 * - we can also apply other info to our methods such as request body
	 * content type and response body content type, etc
	 */
	
	//GET  /users 
	//get all users
	@RequestMapping
	public List<User> getAll(){
		return service.getAll();
	}
	
	//GET /users/id
	//get user by id
	@RequestMapping(value="/{id}")
	public ResponseEntity<User> getById(@PathVariable int id) {
		User u =  service.getById(id);
		if(u == null) {
			//no user is found.. Http status of no content 
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}else {
			//return user with status OK
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}
	}
	
	//POST /users
	//add user to list from request body
	@RequestMapping(method=RequestMethod.POST, 
			consumes=MediaType.APPLICATION_JSON_VALUE, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> add(@RequestBody User u){
		u = service.save(u);
		if(u == null) {
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
		else {
			return new ResponseEntity<User>(HttpStatus.CREATED);
		}
	}
}
