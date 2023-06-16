package com.example.springbootpostgrestutorials.controller;

import com.example.springbootpostgrestutorials.exception.ResourceNotFoundException;
import com.example.springbootpostgrestutorials.model.Tutorial;
import com.example.springbootpostgrestutorials.model.TutorialDetails;
import com.example.springbootpostgrestutorials.repository.TutorialDetailsRepository;
import com.example.springbootpostgrestutorials.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TutorialDetailsController {
    @Autowired
    private TutorialDetailsRepository detailsRepository;

    @Autowired
    private TutorialRepository tutorialRepository;

    @GetMapping("/details")
    public ResponseEntity<List<TutorialDetails>> getAllDetails() {
        List<TutorialDetails> details = detailsRepository.findAll();

        if (details.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(details, HttpStatus.OK);
        }
    }

    @GetMapping({"/details/{tutorialId}", "/tutorials/{tutorialId}/details"})
    public ResponseEntity<TutorialDetails> getDetailsById(@PathVariable(value = "tutorialId") Long tutorialId) {
        TutorialDetails details = detailsRepository.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found TutorialDetails for tutorialId = " + tutorialId));

        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @PostMapping("/tutorials/{tutorialId}/details")
    public ResponseEntity<TutorialDetails> createDetails(@PathVariable(value = "tutorialId") Long tutorialId, @RequestBody TutorialDetails detailsRequest) {
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + tutorialId));

        detailsRequest.setCreatedOn(new java.util.Date());
        detailsRequest.setTutorial(tutorial);
        TutorialDetails details = detailsRepository.save(detailsRequest);

        return new ResponseEntity<>(details, HttpStatus.CREATED);
    }

    @PutMapping({"/details/{tutorialId}", "/tutorials/{tutorialId}/details"})
    public ResponseEntity<TutorialDetails> updateDetails(@PathVariable(value = "tutorialId") Long tutorialId, @RequestBody TutorialDetails detailsRequest) {
        TutorialDetails details = detailsRepository.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found TutorialDetails for tutorialId = " + tutorialId));

        details.setCreatedBy(detailsRequest.getCreatedBy());

        return new ResponseEntity<>(detailsRepository.save(details), HttpStatus.OK);
    }

    @DeleteMapping({"/details/{tutorialId}", "/tutorials/{tutorialId}/details"})
    public ResponseEntity<HttpStatus> deleteTutorialDetails(@PathVariable(value = "tutorialId") Long tutorialId) {
        detailsRepository.deleteById(tutorialId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
