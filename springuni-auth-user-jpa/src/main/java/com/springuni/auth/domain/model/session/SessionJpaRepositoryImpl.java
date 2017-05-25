package com.springuni.auth.domain.model.session;

import static com.springuni.commons.util.Maps.entriesToMap;
import static com.springuni.commons.util.Maps.entry;
import static com.springuni.commons.util.DateTimeUtil.nowUtc;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lcsontos on 5/25/17.
 */
@Transactional
public class SessionJpaRepositoryImpl implements SessionRepository {

  private static final String FIND_BY_SESSION_ID_QUERY = "findBySessionIdQuery";
  private static final String FIND_BY_USER_ID_QUERY = "findByUserIdQuery";

  private static final Logger LOGGER = LoggerFactory.getLogger(SessionJpaRepositoryImpl.class);

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Optional<Session> findById(Long id) {
    Map parameters = Stream.of(entry("sessionId", id)).collect(entriesToMap());
    return doFindSession(FIND_BY_SESSION_ID_QUERY, parameters);
  }

  @Override
  public List<Session> findByUserId(Long userId) {
    TypedQuery<Session> sessionQuery =
        entityManager.createNamedQuery(FIND_BY_USER_ID_QUERY, Session.class);

    Stream.of(entry("userId", userId), entry("now", nowUtc()))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue))
        .forEach(sessionQuery::setParameter);

    return sessionQuery.getResultList();
  }

  @Override
  public Session save(Session session) {
    if (session.isNew()) {
      entityManager.persist(session);
    } else {
      session = entityManager.merge(session);
    }
    return session;
  }

  Optional<Session> doFindSession(String queryName, Map<String, ?> parameters) {
    TypedQuery<Session> sessionQuery = entityManager.createNamedQuery(queryName, Session.class);
    parameters.forEach(sessionQuery::setParameter);
    try {
      return Optional.of(sessionQuery.getSingleResult());
    } catch (NoResultException nre) {
      LOGGER.debug(nre.getMessage(), nre);
      return Optional.empty();
    }
  }

}
