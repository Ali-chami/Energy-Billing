package fr.ekwateur.energybilling;

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

// Cette classe a été ajouté spécialement pour avoir des jeux de données pour le main.
// Dans cette version on utilise pas de repository, car le but est de calculer les consommations
public class ConsumptionDataGenerator {

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
    final String reference = generateReference();
    final String civility = faker.name().prefix();
    final String firstName = faker.name().firstName();
    final String lastName = faker.name().lastName();
    return new ParticularClient(reference, civility, firstName, lastName);
  }

  public static Client createProClient(Boolean higherTurnover) {
    final String reference = generateReference();
    final String siret = faker.number().digits(14);
    final String companyName = faker.company().name();
    final BigDecimal turnover = generateRandomTurnover(higherTurnover);
    return new ProClient(reference, siret, companyName, turnover);
  }

  private static String generateReference() {
    return "REF" + faker.number().digits(3);
  }

  private static BigDecimal generateRandomConsumption(BigDecimal min, BigDecimal max) {
    final BigDecimal range = max.subtract(min);
    final BigDecimal randomFactor = BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1));
    return min.add(range.multiply(randomFactor));
  }

  private static BigDecimal generateRandomTurnover(Boolean higherTurnover) {
    return higherTurnover ? new BigDecimal(faker.number().numberBetween(1000001, 2000000000))
        : new BigDecimal(faker.number().numberBetween(1, 1000000));
  }
}
