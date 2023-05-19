package fr.ekwateur.energybilling.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ParticularClient extends Client {

  private final String civility;
  private final String firstName;
  private final String lastName;

  public ParticularClient(String referenceClient, String civility, String firstName,
      String lastName) {
    super(referenceClient);
    this.civility = civility;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public ClientType getClientType() {
    return ClientType.PARTICULAR;
  }
}
