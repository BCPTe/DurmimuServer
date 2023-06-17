package com.durmimumacares.website.survey;

import com.durmimumacares.website.match.Match;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, ObjectId> {
    Survey findSurveyByDate(Long date);
    Survey findSurveyById(ObjectId id);
}
