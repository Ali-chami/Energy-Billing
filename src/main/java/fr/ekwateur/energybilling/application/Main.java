package fr.ekwateur.energybilling.application;

import fr.ekwateur.energybilling.domain.model.Client;
import fr.ekwateur.energybilling.domain.model.Consumption;
import fr.ekwateur.energybilling.domain.model.EnergyType;
import fr.ekwateur.energybilling.infrastructure.config.ConfigurationReader;
import fr.ekwateur.energybilling.infrastructure.config.PropertiesConfigurationReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    // Utilisation des données de consommation pour calculer la facture
    // Récuperation de la tarification
    final ConfigurationReader configurationReader = new PropertiesConfigurationReader();
    final EnergyBillingService energyBillingService = new EnergyBillingService(
        configurationReader);

    generateBillForParticularClient(energyBillingService);
    System.out.println("________________________________________________________________________");
    generateBillForProClient(energyBillingService, true);
    System.out.println("________________________________________________________________________");
    generateBillForProClient(energyBillingService, false);

  }

  private static void generateBillForParticularClient(EnergyBillingService energyBillingService) {
    // Création d'un client particulier
    final Client particularClient = ConsumptionFixture.createParticularClient();
    System.out.println("Client: " + particularClient);

    // Génération de consommations aléatoires pour un mois donné
    final LocalDate startDate = LocalDate.of(2023, 5, 1);
    final LocalDate endDate = LocalDate.of(2023, 5, 31);
    final BigDecimal minConsumption = new BigDecimal("50.00");
    final BigDecimal maxConsumption = new BigDecimal("200.00");

    generateConsumptionsAndCalculateTheBill(energyBillingService, particularClient, startDate,
        endDate,
        minConsumption, maxConsumption, YearMonth.of(2023, 5));
  }

  private static void generateBillForProClient(EnergyBillingService energyBillingService,
      Boolean higherTurnover) {
    // Création d'un client pro avec un chiffre d'affaire superieur a 1M
    final Client proClient = ConsumptionFixture.createProClient(higherTurnover);
    System.out.println("Client: " + proClient);

    // Génération de consommations aléatoires pour un mois donné
    final LocalDate startDate = LocalDate.of(2023, 5, 1);
    final LocalDate endDate = LocalDate.of(2023, 5, 31);
    final BigDecimal minConsumption = new BigDecimal("5000.00");
    final BigDecimal maxConsumption = new BigDecimal("200000.00");

    generateConsumptionsAndCalculateTheBill(energyBillingService, proClient, startDate, endDate,
        minConsumption, maxConsumption, YearMonth.of(2023, 5));
  }

  private static void generateConsumptionsAndCalculateTheBill(
      EnergyBillingService energyBillingService, Client particularClient, LocalDate startDate,
      LocalDate endDate, BigDecimal minConsumption, BigDecimal maxConsumption, YearMonth month) {
    final List<Consumption> consumptionsOfElectricity = ConsumptionFixture
        .createConsumptions(particularClient, startDate, endDate, EnergyType.ELECTRICITY,
            minConsumption,
            maxConsumption);
    final List<Consumption> consumptionsOfGas = ConsumptionFixture
        .createConsumptions(particularClient, startDate, endDate, EnergyType.GAS, minConsumption,
            maxConsumption);

    final BigDecimal totalElectricityBill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(particularClient, month,
            EnergyType.ELECTRICITY,
            consumptionsOfElectricity);

    final BigDecimal totalGasBill = energyBillingService
        .calculateBillForCalendarMonthAndEnergyType(particularClient, month,
            EnergyType.GAS,
            consumptionsOfGas);

    System.out.println(
        "Total bill for Electricity - " + month.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            + " : " + totalElectricityBill);
    System.out.println(
        "Total bill for Gas - " + month.format(DateTimeFormatter.ofPattern("MMMM yyyy")) + " : "
            + totalGasBill);
  }

}