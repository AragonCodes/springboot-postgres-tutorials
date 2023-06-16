package com.example.springbootpostgrestutorials.controller;

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
        try {
            List<TutorialDetails> details = detailsRepository.findAll();

            if (details.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(details, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<TutorialDetails> getDetailsById(@PathVariable(value = "id") Long id) {
        try {
            Optional<TutorialDetails> details = detailsRepository.findById(id);

            return details.map(tutorialDetails -> new ResponseEntity<>(tutorialDetails, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tutorials/{tutorialId}/details")
    public ResponseEntity<TutorialDetails> createDetails(@PathVariable(value = "tutorialId") Long tutorialId, @RequestBody TutorialDetails detailsRequest) {
        try {
            Optional<Tutorial> tutorialData = tutorialRepository.findById(tutorialId);

            if (tutorialData.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            detailsRequest.setCreatedOn(new java.util.Date());
            detailsRequest.setTutorial(tutorialData.get());
            TutorialDetails details = detailsRepository.save(detailsRequest);

            return new ResponseEntity<>(details, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<TutorialDetails> updateDetails(@PathVariable(value = "id") Long id, @RequestBody TutorialDetails detailsRequest) {
        Optional<TutorialDetails> detailsData = detailsRepository.findById(id);

        if (detailsData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TutorialDetails _details = detailsData.get();
        _details.setCreatedBy(detailsRequest.getCreatedBy());

        return new ResponseEntity<>(detailsRepository.save(_details), HttpStatus.OK);
    }

    @DeleteMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<HttpStatus> deleteTutorialDetails(@PathVariable(value = "id") Long id) {
        detailsRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
