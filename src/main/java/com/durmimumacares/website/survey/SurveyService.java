package com.durmimumacares.website.survey;

import com.durmimumacares.website.user.User;
import com.durmimumacares.website.user.UserRepository;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyService {

    public enum op {
        CREATED, UPDATED, ALREADYPRESENT
    }
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Survey> allSurveys() {
        return surveyRepository.findAll();
    }

    public Survey surveyByDate(Long date) {
        return surveyRepository.findSurveyByDate(date);
    }

    public Survey surveyById(ObjectId id) {
        return surveyRepository.findSurveyById(id);
    }

    public List<Survey> surveysByUsername(String username) {
        User actualUser = userRepository.findUserByUsername(username).get();
        Query query = new Query()
                .addCriteria(Criteria.where("players").in(actualUser));
        return mongoTemplate.find(query, Survey.class);
    }

    public Survey insertSurvey(Survey survey) {
        return surveyRepository.insert(survey);
    }

    public JSONObject manageSurvey(JSONObject info) {
        JSONObject return_values = new JSONObject();
        String operation = info.getString("operation");
        String playerUsername = info.getString("username");
        Long date = info.getLong("date");
        Boolean conflict = false;

        User actualUser = userRepository.findUserByUsername(playerUsername).get();
        Survey survey = surveyByDate(date);
        if(survey != null) {
            // check if user is already present
            for (User user : survey.getPlayers()) {
                if (operation.equals("del"))
                    break;
                if (actualUser.getUsername().equals(user.getUsername())) {
                    conflict = true;
                    return_values.put("survey", survey);
                    return_values.put("code", op.ALREADYPRESENT);
                }
            }
        }
        if(!conflict) {
            if (survey != null) {
                // update present survey

                // operations on players
                List<User> players = survey.getPlayers();
                if (operation.equals("del")) {
                    // remove a player from "players" list in db
                    players.remove(actualUser);
                } else if (operation.equals("add")) {
                    // add a player to "players" list in db
                    players.add(actualUser);
                }
                survey.setPlayers(players);
                surveyRepository.save(survey);
                return_values.put("code", op.UPDATED);
            } else {
                // insert new survey with date and player received
                List<User> players = new ArrayList<>();
                players.add(actualUser);
                survey = new Survey(date, players);
                insertSurvey(survey);
                return_values.put("code", op.CREATED);
            }
        }

        return_values.put("survey", survey);


        return return_values;
    }

}
