package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    //    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    private SendAlertService alertService;
    private MedicalService medicalService;
    PatientInfoRepository patientInfoRepository;
    private final String id1 = "1";
    private final String message = "Warning, patient with id: null, need help";
    private final BloodPressure highPressure = new BloodPressure(150, 90);
    private final BloodPressure normalPressure = new BloodPressure(120, 80);
    private final BigDecimal highTemperature = new BigDecimal("40.0");
    private final BigDecimal normalTemperature = new BigDecimal("36.6");

    @BeforeEach
    void initData() {

        PatientInfo patientInfo = new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        alertService = Mockito.spy(SendAlertServiceImpl.class);
        patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo);
        medicalService = new MedicalServiceImpl(patientInfoRepository, alertService);
    }

    @Test
    void highBloodTest() {
        medicalService.checkBloodPressure(id1, highPressure);
        Mockito.verify(alertService, Mockito.atLeastOnce()).send(message);
    }

    @Test
    void highTemperatureTest() {
        medicalService.checkTemperature(id1, highTemperature);
        Mockito.verify(alertService, Mockito.atLeastOnce()).send(message);
    }

    @Test
    void normalBloodTest() {
        medicalService.checkBloodPressure(id1, normalPressure);
        Mockito.verify(alertService, Mockito.never()).send(message);
    }

    @Test
    void normalTemperatureTest() {
        medicalService.checkTemperature(id1, normalTemperature);
        Mockito.verify(alertService, Mockito.never()).send(message);
    }

}
