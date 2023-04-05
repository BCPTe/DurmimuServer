package com.durmimumacares.website.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
@Document(collection = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    private ObjectId id;
    private String datetime;

    private List<Object> players;

    public Match(String datetime, List<Object> players) {
        this.datetime = datetime;
        this.players = players;
    }
}
