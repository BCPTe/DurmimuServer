package com.durmimumacares.website.survey;

import com.durmimumacares.website.user.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyController {
    @Autowired
    SurveyService surveyService;

    @GetMapping("/date/{date}")
    public ResponseEntity<Survey> getSurveyByDate(@PathVariable Date date) {
        return new ResponseEntity<Survey>(surveyService.surveyByDate(date),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Survey>> getSurveysByUsername(@RequestParam String username) {
        return new ResponseEntity<List<Survey>>(surveyService.surveysByUsername(username),HttpStatus.OK);
    }

//    @PostMapping("/user/{username}")
//    public ResponseEntity<List<Survey>> getSurveysByUsername(@RequestBody String payload) {
//        String username = new JSONObject(payload).getString("username");
//        return new ResponseEntity<List<Survey>>(surveyService.surveysByUsername(username),HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody String payload) {
        JSONObject obj = new JSONObject(payload);
        Survey survey = new Survey(obj.getLong("date"), (List<String>)(List)obj.getJSONArray("players").toList());

        return new ResponseEntity<Survey>(surveyService.insertSurvey(survey), HttpStatus.CREATED);
    }
}
