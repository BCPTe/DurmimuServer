package com.durmimumacares.website.match;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/matches")
public class MatchController {
    @Autowired
    MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        return new ResponseEntity<List<Match>>(matchService.allMatches(), HttpStatus.OK);
    }

    @GetMapping("/{datetime}")
    public ResponseEntity<Optional<List<Match>>> getMatchesByDatetime(@PathVariable String datetime) {
        return new ResponseEntity<Optional<List<Match>>>(matchService.matchByDatetime(datetime), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody String payload) {
        JSONObject obj = new JSONObject(payload);
        Match match = new Match(obj.getString("datetime"), obj.getJSONArray("players").toList());

        System.out.println(match);

        return new ResponseEntity<Match>(matchService.insertMatch(match), HttpStatus.CREATED);
    }
}
