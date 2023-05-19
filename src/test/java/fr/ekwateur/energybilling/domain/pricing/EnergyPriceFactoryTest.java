package fr.ekwateur.energybilling.domain.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.ekwateur.energybilling.application.ConsumptionFixture;
import fr.ekwateur.energybilling.domain.model.Client;
import fr.ekwateur.energybilling.infrastructure.config.ConfigurationReader;
import fr.ekwateur.energybilling.infrastructure.exception.InvalidClientTypeException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EnergyPriceFactoryTest {

  @Mock
  private ConfigurationReader configurationReader;

  private EnergyPriceFactory energyPriceFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    energyPriceFactory = new EnergyPriceFactory(configurationReader);
  }

  @Test
  void createEnergyPrice_WithProClientAndHighTurnover_ShouldReturnEnergyPrice() {
    // Arrange
    Client client = ConsumptionFixture.createProClient(true);
    boolean highTurnover = true;
    BigDecimal expectedElectricityPrice = new BigDecimal("0.20");
    BigDecimal expectedGasPrice = new BigDecimal("0.15");
    when(configurationReader.getTurnoverLimit()).thenReturn(new BigDecimal("1000000"));
    when(configurationReader.getProElectricityPrice(highTurnover))
        .thenReturn(expectedElectricityPrice);
    when(configurationReader.getProGasPrice(highTurnover)).thenReturn(expectedGasPrice);

    // Act
    EnergyPrice energyPrice = energyPriceFactory.createEnergyPrice(client);

    // Assert
    assertNotNull(energyPrice);
    assertEquals(expectedElectricityPrice, energyPrice.getElectricityPrice());
    assertEquals(expectedGasPrice, energyPrice.getGasPrice());
  }

  @Test
  void createEnergyPrice_WithProClientAndLowTurnover_ShouldReturnEnergyPrice() {
    // Arrange
    Client client = ConsumptionFixture.createProClient(false);
    boolean highTurnover = false;
    BigDecimal expectedElectricityPrice = new BigDecimal("0.15");
    BigDecimal expectedGasPrice = new BigDecimal("0.12");
    when(configurationReader.getTurnoverLimit()).thenReturn(new BigDecimal("1000000"));
    when(configurationReader.getProElectricityPrice(highTurnover))
        .thenReturn(expectedElectricityPrice);
    when(configurationReader.getProGasPrice(highTurnover)).thenReturn(expectedGasPrice);

    // Act
    EnergyPrice energyPrice = energyPriceFactory.createEnergyPrice(client);

    // Assert
    assertNotNull(energyPrice);
    assertEquals(expectedElectricityPrice, energyPrice.getElectricityPrice());
    assertEquals(expectedGasPrice, energyPrice.getGasPrice());
  }

  @Test
  void createEnergyPrice_WithParticularClient_ShouldReturnEnergyPrice() {
    // Arrange
    Client client = ConsumptionFixture.createParticularClient();
    BigDecimal expectedElectricityPrice = new BigDecimal("0.25");
    BigDecimal expectedGasPrice = new BigDecimal("0.20");
    when(configurationReader.getElectricityPrice()).thenReturn(expectedElectricityPrice);
    when(configurationReader.getGasPrice()).thenReturn(expectedGasPrice);

    // Act
    EnergyPrice energyPrice = energyPriceFactory.createEnergyPrice(client);

    // Assert
    assertNotNull(energyPrice);
    assertEquals(expectedElectricityPrice, energyPrice.getElectricityPrice());
    assertEquals(expectedGasPrice, energyPrice.getGasPrice());
  }

  @Test
  void createEnergyPrice_WithInvalidClientType_ShouldThrowException() {
    // Arrange
    Client client = mock(Client.class);
    when(client.getClientType()).thenReturn(null);

    // Act & Assert
    assertThrows(InvalidClientTypeException.class,
        () -> energyPriceFactory.createEnergyPrice(client));
  }

}
