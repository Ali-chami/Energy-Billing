package fr.ekwateur.energybilling.application;

import fr.ekwateur.energybilling.domain.model.Client;
import fr.ekwateur.energybilling.domain.model.Consumption;
import fr.ekwateur.energybilling.domain.model.EnergyType;
import fr.ekwateur.energybilling.domain.pricing.EnergyPrice;
import fr.ekwateur.energybilling.domain.pricing.EnergyPriceFactory;
import fr.ekwateur.energybilling.infrastructure.config.ConfigurationReader;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import lombok.Getter;

@Getter
public class EnergyBillingService {

  private final EnergyPriceFactory energyPriceFactory;

  public EnergyBillingService(final ConfigurationReader configurationReader) {
    this.energyPriceFactory = new EnergyPriceFactory(configurationReader);
  }

  public BigDecimal calculateBillForCalendarMonthAndEnergyType(Client client, YearMonth yearMonth,
      EnergyType energyType, List<Consumption> consumptions) {
    EnergyPrice energyPrice = energyPriceFactory.createEnergyPrice(client);

    if (energyPrice == null) {
      throw new RuntimeException("Failed to create EnergyPrice for client: " + client);
    }

    // JUST FOR TEST
    BigDecimal priceForSout =
        energyType == EnergyType.ELECTRICITY ? energyPrice.getElectricityPrice()
            : energyPrice.getGasPrice();
    System.out.println(energyType + " price for this client is : " + priceForSout);

    return consumptions.stream()
        .filter(consumption -> consumption.getClient().equals(client)
            && consumption.getDate().getYear() == yearMonth.getYear()
            && consumption.getDate().getMonth() == yearMonth.getMonth()
            && consumption.getEnergyType() == energyType)
        .map(consumption ->
            getPricePerKwh(energyPrice, consumption).multiply(consumption.getConsumption())
        )
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal getPricePerKwh(EnergyPrice energyPrice, Consumption consumption) {
    return consumption.getEnergyType() == EnergyType.ELECTRICITY
        ? energyPrice.getElectricityPrice()
        : energyPrice.getGasPrice();
  }
}
