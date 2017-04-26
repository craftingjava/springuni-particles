package com.springuni.user.domain.model;

import com.springuni.commons.domain.DomainEvent;
import java.util.Optional;
import lombok.Data;

/**
 * Created by lcsontos on 4/25/17.
 */
@Data
public class UserEvent<P> implements DomainEvent<UserEvent> {

  private final Long userId;
  private final UserEventType userEventType;

  private final P payload;

  public UserEvent(Long userId, UserEventType userEventType) {
    this(userId, userEventType, null);
  }

  public UserEvent(Long userId, UserEventType userEventType, P payload) {
    this.userId = userId;
    this.userEventType = userEventType;
    this.payload = payload;
  }

  public Optional<P> getPayload() {
    return Optional.ofNullable(payload);
  }

  @Override
  public boolean sameEventAs(UserEvent other) {
    return equals(other);
  }

}
