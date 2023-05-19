package fr.ekwateur.energybilling.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public abstract class Client {

  private final String referenceClient;

  public abstract ClientType getClientType();
}
