package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.exceptions.GenreNotFoundException;
import com.ebookstore.microservices.bookservice.proxy.AuthenticationProxy;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<List<Genre>> getAll(){
		return ResponseEntity.ok(genreService.findAll());
	}

	@GetMapping("/{genreId}")
	public ResponseEntity<?> getGenreById(@PathVariable Long genreId){
		try{
			Genre genre = genreService.findById(genreId);
			return ResponseEntity.ok(genre);
		}
		catch (GenreNotFoundException exception){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	@PostMapping("/createGenre")
	public ResponseEntity<Genre> addGenre(@RequestHeader("Authorization") String tokenHeader,
						  @RequestBody Genre genre) {
		authenticationProxy.validateToken(tokenHeader);
		if(genre.getGenreId() == null)
			return ResponseEntity.ok(genreService.save(genre));
		else	
			return ResponseEntity.ok(genreService.update(genre.getGenreId(), genre.getGenre()));
	}

	@DeleteMapping("/deleteGenre/{genreId}")
	public ResponseEntity<Void> deleteGenre(@RequestHeader("Authorization") String tokenHeader,
							@PathVariable Long genreId){
		authenticationProxy.validateToken(tokenHeader);
		genreService.deleteById(genreId);
		return ResponseEntity.noContent().build();
	}
}
