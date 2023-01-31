package app.foot.service;

import app.foot.exception.InternalServerException;
import app.foot.exception.NotFoundException;
import app.foot.model.Match;
import app.foot.model.PlayerScorer;
import app.foot.repository.MatchRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.mapper.MatchMapper;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MatchService {
  private final MatchRepository repository;
  private final MatchMapper mapper;
  private final PlayerScoreService scoreService;

  public List<Match> getMatches() {
    return repository.findAll().stream()
        .map(mapper::toDomain)
        .toList();
  }

  public Match getMatchById(int matchId) {
    return mapper.toDomain(
        repository.findById(matchId)
            .orElseThrow(() -> new NotFoundException("Match#" + matchId + " not found."))
    );
  }

  public Match addGoals(int matchId, List<PlayerScorer> scorers) {
    List<PlayerScorer> playerScorer = scoreService.addGoals(matchId, scorers);
    if(playerScorer.size() == 0){
      throw new InternalServerException("Can't persist data");
    }
    return getMatchById(matchId);
  }
}
