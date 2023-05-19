package fr.ekwateur.energybilling.domain.model;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ProClient extends Client {

  private final String siret;
  private final String companyName;
  private final BigDecimal turnover;

  public ProClient(String referenceClient, String siret, String companyName, BigDecimal turnover) {
    super(referenceClient);
    this.siret = siret;
    this.companyName = companyName;
    this.turnover = turnover;
  }


  @Override
  public ClientType getClientType() {
    return ClientType.PRO;
  }
}