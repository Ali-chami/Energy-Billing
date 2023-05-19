package fr.ekwateur.energybilling.domain.pricing;

import fr.ekwateur.energybilling.domain.model.Client;
import fr.ekwateur.energybilling.domain.model.ClientType;
import fr.ekwateur.energybilling.domain.model.ProClient;
import fr.ekwateur.energybilling.infrastructure.config.ConfigurationReader;
import fr.ekwateur.energybilling.infrastructure.exception.InvalidClientTypeException;
import java.math.BigDecimal;

public class EnergyPriceFactory {

  private final ConfigurationReader configurationReader;

  public EnergyPriceFactory(ConfigurationReader configurationReader) {
    this.configurationReader = configurationReader;
  }

  public EnergyPrice createEnergyPrice(Client client) {
    validateClient(client);

    BigDecimal electricityPrice;
    BigDecimal gasPrice;

    if (client.getClientType() == ClientType.PRO) {
      ProClient proClient = (ProClient) client;
      boolean highTurnover =
          proClient.getTurnover().compareTo(configurationReader.getTurnoverLimit()) > 0;
      electricityPrice = configurationReader.getProElectricityPrice(highTurnover);
      gasPrice = configurationReader.getProGasPrice(highTurnover);
    } else if (client.getClientType() == ClientType.PARTICULAR) {
      electricityPrice = configurationReader.getElectricityPrice();
      gasPrice = configurationReader.getGasPrice();
    } else {
      throw new InvalidClientTypeException("Invalid client type");
    }
    return new EnergyPrice(electricityPrice, gasPrice);
  }

  private void validateClient(Client client) {
    if (client == null) {
      throw new IllegalArgumentException("Client cannot be null");
    }
  }
}
