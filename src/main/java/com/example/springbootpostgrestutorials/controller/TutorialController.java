package com.example.springbootpostgrestutorials.controller;

import com.example.springbootpostgrestutorials.exception.ResourceNotFoundException;
import com.example.springbootpostgrestutorials.model.Tutorial;
import com.example.springbootpostgrestutorials.repository.TutorialDetailsRepository;
import com.example.springbootpostgrestutorials.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @Autowired
    TutorialDetailsRepository detailsRepository;

    @GetMapping("/tutorials")
    public ResponseEntity<HashMap<String, Object>> getAllTutorials(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
            ) {
        Pageable paging = PageRequest.of(page, size);

        Page<Tutorial> tutorialsPage = title == null ? tutorialRepository.findAll(paging) : tutorialRepository.findByTitleContaining(title, paging);


        HashMap<String, Object> response = new HashMap<>();
        response.put("tutorials", tutorialsPage.getContent());
        response.put("currentPage", tutorialsPage.getNumber());
        response.put("totalItems", tutorialsPage.getTotalElements());
        response.put("totalPages", tutorialsPage.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

        return new ResponseEntity<>(tutorial, HttpStatus.OK);
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        Tutorial _tutorial = tutorialRepository.save(tutorial);
        return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorialRequest) {
        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

        if (tutorialRequest.getTitle() != null) {
            tutorial.setTitle(tutorialRequest.getTitle());
        }
        if (tutorialRequest.getDescription() != null) {
            tutorial.setDescription(tutorialRequest.getDescription());
        }
        tutorial.setPublished(tutorialRequest.isPublished());

        return new ResponseEntity<>(tutorialRepository.save(tutorial), HttpStatus.OK);
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        if (detailsRepository.existsById(id)) {
            detailsRepository.deleteById(id);
        }
        tutorialRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        detailsRepository.deleteAll();
        tutorialRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<HashMap<String, Object>> findByPublished(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
            ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tutorial> tutorialsPage = tutorialRepository.findByPublished(true, pageable);

        HashMap<String, Object> response = new HashMap<>();
        response.put("tutorials", tutorialsPage.getContent());
        response.put("currentPage", tutorialsPage.getNumber());
        response.put("totalItems", tutorialsPage.getTotalElements());
        response.put("totalPages", tutorialsPage.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
