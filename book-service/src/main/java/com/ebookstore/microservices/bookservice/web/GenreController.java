package com.ebookstore.microservices.bookservice.web;

import java.util.List;

import com.ebookstore.microservices.bookservice.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ebookstore.microservices.bookservice.models.Genre;
import com.ebookstore.microservices.bookservice.services.GenreService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/genres")
public class GenreController {

	@Autowired
	private final TokenService tokenService;
	private final GenreService genreService;

	public GenreController(TokenService tokenService, GenreService genreService) {
		this.tokenService = tokenService;
		this.genreService = genreService;
	}
	
	@GetMapping
	public ResponseEntity<List<Genre>> getAll(){
		return ResponseEntity.ok(genreService.findAll());
	}

	@GetMapping("/{genreId}")
	public ResponseEntity<Genre> getGenreById(@PathVariable Long genreId){
		Genre genre = genreService.findById(genreId);
		return ResponseEntity.ok(genre);
	}
	
	@PostMapping("/createGenre")
	public ResponseEntity<Genre> addGenre(@RequestHeader("Authorization") String tokenHeader,
						  @RequestBody Genre genre) {
		tokenService.callValidateToken(tokenHeader);
		if(genre.getGenreId() == null)
			return ResponseEntity.ok(genreService.save(genre));
		else	
			return ResponseEntity.ok(genreService.update(genre.getGenreId(), genre.getGenre()));
	}

	@DeleteMapping("/deleteGenre/{genreId}")
	public ResponseEntity<Void> deleteGenre(@RequestHeader("Authorization") String tokenHeader,
							@PathVariable Long genreId){
		tokenService.callValidateToken(tokenHeader);
		genreService.deleteById(genreId);
		return ResponseEntity.noContent().build();
	}
}
