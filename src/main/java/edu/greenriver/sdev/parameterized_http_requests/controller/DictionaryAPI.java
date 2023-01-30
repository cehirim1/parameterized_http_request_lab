package edu.greenriver.sdev.parameterized_http_requests.controller;

import edu.greenriver.sdev.parameterized_http_requests.dtos.WordDefinition;
import edu.greenriver.sdev.parameterized_http_requests.service.DictionaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Chisom E.
 * @version 17
 * @Date 1/28/2023
 */
@RestController
@RequestMapping("api/v1/dict")
public class DictionaryAPI {

    private DictionaryService dictionaryService;

    /**
     * Parameterized constructor
     *
     * @param dictionaryService parameter
     */
    public DictionaryAPI(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }


    /**
     * Maps HTTP GET request to the method to retrieve all words
     *
     * @return all words in the dictionary
     */
    //http://localhost:8080/api/v1/dict
    @GetMapping("")
    public ResponseEntity<List<String>> allWords() {
        return new ResponseEntity<>(dictionaryService.allWords(), HttpStatus.OK);
    }


    /**
     * @param word word to get its definition
     * @return returns definition of the word or a status code NOT_FOUND if not found
     */
    @GetMapping("{word}")
    //http://localhost:8080/api/v1/dict/{word}
    public ResponseEntity<Optional<String>> wordMeaning(@PathVariable String word) {

        //if word is not in the dictionary, return status code NOT_FOUND
        if (!dictionaryService.allWords().contains(word)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //else, return definition of word if found and status code OK
        return new ResponseEntity<>(dictionaryService.getDefinition(word), HttpStatus.OK);
    }

    /**
     * @param word       word to add
     * @param definition definition of the word to add
     * @return word and definition or BAD_REQUEST if dictionary contains word or input is empty
     */
    //http://localhost:8080/api/v1/dict/param?word=Afang&definition=Soup made with spinach
    @PostMapping("param")
    public ResponseEntity<WordDefinition> addWordAndDef(@RequestParam String word, @RequestParam String definition) {

        //if word is in dictionary, return status code BAD_REQUEST
        if (dictionaryService.allWords().contains(word.toLowerCase())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //if word to add or definition to add is not in dictionary and is empty, return status code NO_CONTENT
        else if (word.replaceAll("[^a-zA-Z]", "").isEmpty()
                || definition.replaceAll("[^a-zA-Z]", "").isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //else, if valid data and does not exist, create and return status code CREATED
        return new ResponseEntity<>(dictionaryService.addWord(word, definition), HttpStatus.CREATED);

    }

    /**
     * @param word word to add
     * @param definition definition of word
     * @return the word and its definition if available or BAD_REQUEST
     * if word already exists in the dictionary or the request body is empty
     */
    //http://localhost:8080/api/v1/dict/path/{word}/{definition}
    @PostMapping("path/{word}/{definition}")
    public ResponseEntity<WordDefinition> addWord(@PathVariable String word, @PathVariable String definition) {


        //if word already exists, return status code BAD_REQUEST
        if (dictionaryService.allWords().contains(word.toLowerCase())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        //if word or definition is empty, return status code NO_CONTENT
        else {
            boolean exist = definition.replaceAll("[^a-zA-Z]", "").isEmpty();
            if (word.replaceAll("[^a-zA-Z]", "").isEmpty()

            || exist) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        //else, create if word does not exist and word and definition are valid data
        //return status code CREATED
        return new ResponseEntity<>(dictionaryService.addWord(word, definition), HttpStatus.CREATED);

    }


    /**
     * @param word word and definition to add
     * @return content and status code if successful or status code if not
     */
    //http://localhost:8080/api/v1/dict/body
    @PostMapping("body")
    public ResponseEntity<WordDefinition> addWordPair(@RequestBody WordDefinition word) {


        //if word already exists, return status code BAD_REQUEST
        if (dictionaryService.allWords().contains(word.getWord().toLowerCase())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        //if word or definition is empty, return status code NO_CONTENT
        else if (word.getWord().replaceAll("[^a-zA-Z]", "").isEmpty()
                    || word.getDefinition().replaceAll("[^a-zA-Z]", "").isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        //else, create if word does not exist and word and definition are valid data
        //return status code CREATED
        return new ResponseEntity<>(dictionaryService.addWord(word), HttpStatus.CREATED);

    }

    @Override
    public String toString() {
        return "DictionaryAPI{" + "dictionaryService=" + dictionaryService + '}';
    }
}
