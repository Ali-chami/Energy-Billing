package fr.ekwateur.energybilling.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Consumption {

  private final LocalDate date;
  private final Client client;
  private final EnergyType energyType;
  private final BigDecimal consumption;

}
