package com.durmimumacares.website.survey;

import com.durmimumacares.website.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "surveys")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Survey {
    @Id
    private ObjectId id;
    private Long date;
    private List<User> players;

    public Survey(Long date, List<User> players) {
        this.date = date;
        this.players = players;
    }
}
