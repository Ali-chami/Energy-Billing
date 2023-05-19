package fr.ekwateur.energybilling.application;

import com.github.javafaker.Faker;
import fr.ekwateur.energybilling.domain.model.Client;
import fr.ekwateur.energybilling.domain.model.Consumption;
import fr.ekwateur.energybilling.domain.model.EnergyType;
import fr.ekwateur.energybilling.domain.model.ParticularClient;
import fr.ekwateur.energybilling.domain.model.ProClient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ConsumptionFixture {

  private static final Faker faker = new Faker(new Locale("fr"));

  public static List<Consumption> createConsumptions(Client client, LocalDate startDate,
      LocalDate endDate, EnergyType energyType, BigDecimal minConsumption,
      BigDecimal maxConsumption) {
    return startDate.datesUntil(endDate.plusDays(1))
        .map(date -> new Consumption(date, client, energyType,
            generateRandomConsumption(minConsumption, maxConsumption)))
        .collect(Collectors.toList());
  }

  public static Client createParticularClient() {
    String reference = generateReference();
    String civility = faker.name().prefix();
    String firstName = faker.name().firstName();
    String lastName = faker.name().lastName();
    return new ParticularClient(reference, civility, firstName, lastName);
  }

  public static Client createProClient(Boolean higherTurnover) {
    String reference = generateReference();
    String siret = faker.number().digits(14);
    String companyName = faker.company().name();
    BigDecimal turnover = generateRandomTurnover(higherTurnover);
    return new ProClient(reference, siret, companyName, turnover);
  }

  private static String generateReference() {
    return "REF" + faker.number().digits(3);
  }

  private static BigDecimal generateRandomConsumption(BigDecimal min, BigDecimal max) {
    BigDecimal range = max.subtract(min);
    BigDecimal randomFactor = BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1));
    return min.add(range.multiply(randomFactor));
  }

  private static BigDecimal generateRandomTurnover(Boolean higherTurnover) {
    return higherTurnover ? new BigDecimal(faker.number().numberBetween(1000001, 2000000000))
        : new BigDecimal(faker.number().numberBetween(1, 1000000));
  }
}
