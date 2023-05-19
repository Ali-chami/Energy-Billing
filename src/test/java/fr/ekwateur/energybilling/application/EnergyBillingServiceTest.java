package fr.ekwateur.energybilling.application;

import fr.ekwateur.energybilling.domain.model.Client;
import fr.ekwateur.energybilling.domain.model.Consumption;
import fr.ekwateur.energybilling.domain.model.EnergyType;
import fr.ekwateur.energybilling.infrastructure.config.ConfigurationReader;
import fr.ekwateur.energybilling.infrastructure.config.PropertiesConfigurationReader;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnergyBillingServiceTest {

  private EnergyBillingService energyBillingService;
  private YearMonth yearMonth;
  private ConfigurationReader configurationReader;

  @BeforeEach
  public void setUp() {
    configurationReader = new PropertiesConfigurationReader();
    energyBillingService = new EnergyBillingService(configurationReader);
    yearMonth = YearMonth.now();
  }

  @Test
  public void testCalculateBillForParticularClientWithElectricityConsumptions() {
    // Arrange
    Client client = ConsumptionFixture.createParticularClient();
    EnergyType energyType = EnergyType.ELECTRICITY;
    List<Consumption> consumptions = ConsumptionFixture
        .createConsumptions(client, yearMonth.atDay(1),
            yearMonth.atEndOfMonth(), energyType, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

    BigDecimal expectedBill = getExpectedBill(consumptions,
        configurationReader.getElectricityPrice());

    // Act
    BigDecimal bill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(client, yearMonth, energyType, consumptions);

    Assertions.assertEquals(expectedBill, bill);
  }

  @Test
  public void testCalculateBillForParticularClientWithGasConsumptions() {
    // Arrange
    Client client = ConsumptionFixture.createParticularClient();
    YearMonth yearMonth = YearMonth.now();
    EnergyType energyType = EnergyType.GAS;
    List<Consumption> consumptions = ConsumptionFixture
        .createConsumptions(client, yearMonth.atDay(1),
            yearMonth.atEndOfMonth(), energyType, BigDecimal.valueOf(50), BigDecimal.valueOf(100));

    BigDecimal expectedBill = getExpectedBill(consumptions,
        configurationReader.getGasPrice());

    // Act
    BigDecimal bill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(client, yearMonth,
            energyType, consumptions);

    // Assert
    Assertions.assertEquals(expectedBill, bill);
  }


  @Test
  public void testCalculateBillForProClientWithHighTurnoverForElectricityConsumptions() {
    // Arrange
    Client client = ConsumptionFixture.createProClient(true);
    YearMonth yearMonth = YearMonth.now();
    EnergyType energyType = EnergyType.ELECTRICITY;
    List<Consumption> consumptions = ConsumptionFixture
        .createConsumptions(client, yearMonth.atDay(1),
            yearMonth.atEndOfMonth(), energyType, BigDecimal.valueOf(200), BigDecimal.valueOf(300));

    BigDecimal expectedBill = getExpectedBill(consumptions,
        configurationReader.getProElectricityPrice(true));

    // Act
    BigDecimal bill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(client, yearMonth,
            energyType, consumptions);

    // Assert
    Assertions.assertEquals(expectedBill, bill);
  }

  @Test
  public void testCalculateBillForProClientWithHighTurnoverForGasConsumptions() {
    // Arrange
    Client client = ConsumptionFixture.createProClient(true);
    YearMonth yearMonth = YearMonth.now();
    EnergyType energyType = EnergyType.GAS;
    List<Consumption> consumptions = ConsumptionFixture
        .createConsumptions(client, yearMonth.atDay(1),
            yearMonth.atEndOfMonth(), energyType, BigDecimal.valueOf(200), BigDecimal.valueOf(300));

    BigDecimal expectedBill = getExpectedBill(consumptions,
        configurationReader.getProGasPrice(true));

    // Act
    BigDecimal bill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(client, yearMonth,
            energyType, consumptions);

    // Assert
    Assertions.assertEquals(expectedBill, bill);
  }

  @Test
  public void testCalculateBillForProClientWithLowTurnoverForElectricityConsumptions() {
    // Arrange
    Client client = ConsumptionFixture.createProClient(false);
    YearMonth yearMonth = YearMonth.now();
    EnergyType energyType = EnergyType.ELECTRICITY;
    List<Consumption> consumptions = ConsumptionFixture
        .createConsumptions(client, yearMonth.atDay(1),
            yearMonth.atEndOfMonth(), energyType, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

    BigDecimal expectedBill = getExpectedBill(consumptions,
        configurationReader.getProElectricityPrice(false));

    // Act
    BigDecimal bill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(client, yearMonth,
            energyType, consumptions);

    // Assert
    Assertions.assertEquals(expectedBill, bill);
  }

  @Test
  public void testCalculateBillForProClientWithLowTurnoverForGasConsumptions() {
    // Arrange
    Client client = ConsumptionFixture.createProClient(false);
    YearMonth yearMonth = YearMonth.now();
    EnergyType energyType = EnergyType.GAS;
    List<Consumption> consumptions = ConsumptionFixture
        .createConsumptions(client, yearMonth.atDay(1),
            yearMonth.atEndOfMonth(), energyType, BigDecimal.valueOf(100), BigDecimal.valueOf(200));

    BigDecimal expectedBill = getExpectedBill(consumptions,
        configurationReader.getProGasPrice(false));

    // Act
    BigDecimal bill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(client, yearMonth,
            energyType, consumptions);

    // Assert
    Assertions.assertEquals(expectedBill, bill);
  }

  private BigDecimal getExpectedBill(List<Consumption> consumptions, BigDecimal energyPrice) {
    return consumptions.stream()
        .map(consumption ->
            energyPrice.multiply(consumption.getConsumption())
        )
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
