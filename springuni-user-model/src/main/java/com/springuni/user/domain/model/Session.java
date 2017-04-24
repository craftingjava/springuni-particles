package com.springuni.user.domain.model;

import static java.time.temporal.ChronoUnit.HOURS;

import com.springuni.commons.domain.ValueObject;
import com.springuni.commons.util.DateTimeUtil;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Created by lcsontos on 4/24/17.
 */
@Data
public class Session implements ValueObject<Session> {

  private final String sessionId;
  private final Long userId;
  private final LocalDateTime expiresAt;

  /**
   * Creates a new {@link Session} with the given ID for the given User ID.
   *
   * @param sessionId Session ID
   * @param userId User ID
   */
  public Session(String sessionId, Long userId) {
    this.sessionId = sessionId;
    this.userId = userId;
    expiresAt = DateTimeUtil.expireNowUtc(24, HOURS);
  }

  @Override
  public boolean sameValueAs(Session other) {
    return equals(other);
  }

}
