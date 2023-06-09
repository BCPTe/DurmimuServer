package com.durmimumacares.website.match;

import com.durmimumacares.website.user.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
    @Autowired
    MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return new ResponseEntity<List<Match>>(matchService.allMatches(), HttpStatus.OK);
    }

    @GetMapping(params = "datetime")
    public ResponseEntity<Optional<List<Match>>> getMatchesByDatetime(@RequestParam("datetime") Long datetime) {
        return new ResponseEntity<Optional<List<Match>>>(matchService.matchByDatetime(datetime), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody String payload) {
        JSONObject obj = new JSONObject(payload);
        Match match = new Match(obj.getString("datetime"), obj.getJSONArray("players").toList());

        return new ResponseEntity<Match>(matchService.insertMatch(match), HttpStatus.CREATED);
    }
}
