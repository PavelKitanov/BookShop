package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ebookstore.microservices.bookservice.models.Genre;
import com.ebookstore.microservices.bookservice.services.GenreService;

@RestController
@RequestMapping("/genres")
public class GenreController {

	@Autowired
	private final AuthenticationProxy authenticationProxy;
	private final GenreService genreService;

	public GenreController(AuthenticationProxy authenticationProxy, GenreService genreService) {
		this.authenticationProxy = authenticationProxy;
		this.genreService = genreService;
	}
	
	@GetMapping
	public List<Genre> getAll(){
		return genreService.findAll();
	}
	
	@PostMapping("/createGenre")
	public Genre addGenre(@RequestHeader("Authorization") String tokenHeader,
						  @RequestBody Genre genre) {
		authenticationProxy.validateToken(tokenHeader);
		if(genre.getGenreId() == null)
			return genreService.save(genre);
		else	
			return genreService.update(genre.getGenreId(), genre.getGenre());
	}

	@DeleteMapping("/deleteGenre/{genreId}")
	public void deleteGenre(@RequestHeader("Authorization") String tokenHeader,
							@PathVariable Long genreId){
		authenticationProxy.validateToken(tokenHeader);
		genreService.deleteById(genreId);
	}
}
