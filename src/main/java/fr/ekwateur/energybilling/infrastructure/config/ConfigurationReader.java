package fr.ekwateur.energybilling.infrastructure.config;

import java.math.BigDecimal;

public interface ConfigurationReader {

  BigDecimal getTurnoverLimit();

  BigDecimal getElectricityPrice();

  BigDecimal getGasPrice();

  BigDecimal getProElectricityPrice(boolean highTurnover);

  BigDecimal getProGasPrice(boolean highTurnover);

}
