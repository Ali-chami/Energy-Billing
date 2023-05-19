package fr.ekwateur.energybilling.domain.pricing;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EnergyPrice {

  private final BigDecimal electricityPrice;
  private final BigDecimal gasPrice;

}
