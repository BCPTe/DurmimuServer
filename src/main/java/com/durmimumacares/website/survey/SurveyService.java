package com.durmimumacares.website.survey;

import com.durmimumacares.website.match.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SurveyService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private SurveyRepository surveyRepository;

    public Survey surveyByDate(Date date) {
        return surveyRepository.findSurveyByDate(date);
    }

    public List<Survey> surveysByUsername(String username) {
        Query query = new Query()
                .addCriteria(Criteria.where("players").in(username));
        return mongoTemplate.find(query, Survey.class);
    }

    public Survey insertSurvey(Survey survey) {
        return surveyRepository.insert(survey);
    }

}
