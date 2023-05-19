package fr.ekwateur.energybilling.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

public class PropertiesConfigurationReader implements ConfigurationReader {

  private static final String CONFIG_FILE = "energy_prices.properties";
  private final Properties properties;

  public PropertiesConfigurationReader() {
    properties = new Properties();
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load configuration file: " + CONFIG_FILE, e);
    }
  }

  @Override
  public BigDecimal getTurnoverLimit() {
    String price = properties.getProperty("pro.turnover");
    return new BigDecimal(price);
  }

  @Override
  public BigDecimal getElectricityPrice() {
    String price = properties.getProperty("electricity.price");
    return new BigDecimal(price);
  }

  @Override
  public BigDecimal getGasPrice() {
    String price = properties.getProperty("gas.price");
    return new BigDecimal(price);
  }

  @Override
  public BigDecimal getProElectricityPrice(boolean highTurnover) {
    String propertyName =
        highTurnover ? "pro.electricity.price.highTurnover" : "pro.electricity.price.lowTurnover";
    String price = properties.getProperty(propertyName);
    return new BigDecimal(price);
  }

  @Override
  public BigDecimal getProGasPrice(boolean highTurnover) {
    String propertyName = highTurnover ? "pro.gas.price.highTurnover" : "pro.gas.price.lowTurnover";
    String price = properties.getProperty(propertyName);
    return new BigDecimal(price);
  }
}
