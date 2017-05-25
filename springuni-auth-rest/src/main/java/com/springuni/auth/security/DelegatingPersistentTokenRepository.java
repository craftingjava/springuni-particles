package com.springuni.auth.security;

import com.springuni.auth.domain.model.session.SessionRepository;
import java.util.Date;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Created by lcsontos on 5/24/17.
 */
public class DelegatingPersistentTokenRepository implements PersistentTokenRepository {

  private final SessionRepository sessionRepository;

  public DelegatingPersistentTokenRepository(SessionRepository sessionRepository) {
    this.sessionRepository = sessionRepository;
  }

  @Override
  public void createNewToken(PersistentRememberMeToken token) {

  }

  @Override
  public void updateToken(String series, String tokenValue, Date lastUsed) {

  }

  @Override
  public PersistentRememberMeToken getTokenForSeries(String seriesId) {
    return null;
  }

  @Override
  public void removeUserTokens(String username) {

  }

}
