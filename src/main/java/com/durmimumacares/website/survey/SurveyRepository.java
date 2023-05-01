package com.durmimumacares.website.survey;

import com.durmimumacares.website.match.Match;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface SurveyRepository extends MongoRepository<Survey, ObjectId> {
    Survey findSurveyByDate(Date date);
//    List<Survey> findSurveyByUsername(String username);

}
