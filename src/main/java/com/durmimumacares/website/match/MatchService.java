package com.durmimumacares.website.match;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public List<Match> allMatches() {
        return matchRepository.findAll();
    }

    public Optional<List<Match>> matchByDatetime(String datetime) {
        return matchRepository.findMatchByDatetime(datetime);
    }

    public Match insertMatch(Match match) {
        return matchRepository.insert(match);
    }
}
