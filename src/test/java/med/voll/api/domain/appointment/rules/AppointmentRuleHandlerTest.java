package med.voll.api.domain.appointment.rules;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import med.voll.api.Mocks;
import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.persistence.repository.AppointmentRepository;
import med.voll.api.domain.persistence.repository.MedicRepository;
import med.voll.api.domain.persistence.repository.PatientRepository;
import med.voll.api.infra.exception.BadRequestException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class AppointmentRuleHandlerTest {

    AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    MedicRepository medicRepository = mock(MedicRepository.class);
    PatientRepository patientRepository = mock(PatientRepository.class);
    Clock clock = mock(Clock.class);

    private AppointmentRuleHandlerImpl appointmentRuleHandler = new AppointmentRuleHandlerImpl(appointmentRepository,
                                                                                        medicRepository,
                                                                                        patientRepository,
                                                                                        clock);

    private final int WORKING_HOURS_BEGIN = 9;
    private final int WORKING_HOURS_END = 19;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(appointmentRuleHandler, "workingHoursBegin", WORKING_HOURS_BEGIN);
        ReflectionTestUtils.setField(appointmentRuleHandler, "workingHoursEnd", WORKING_HOURS_END);

        given(medicRepository.existsByIdAndActiveTrue(any())).willReturn(Boolean.TRUE);
        given(patientRepository.existsByIdAndActiveTrue(any())).willReturn(Boolean.TRUE);
        given(appointmentRepository.existsByDateAndMedicId(any(), any())).willReturn(Boolean.FALSE);
        given(clock.instant()).willReturn(LocalDateTime.of(2023, 8, 7, 12, 0, 0).toInstant(ZoneOffset.UTC));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());
    }

    @Test
    void should_ReturnError_WhenSunday() {
        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 6, 15, 0, 0));

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals("Cannot schedule appointment on sunday.", ex.getMessage());

    }

    @Test
    void should_ReturnError_WhenScheduledBeforeWorkingHours() {
        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 7, WORKING_HOURS_BEGIN-1, 59, 0));

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals(MessageFormat.format("Invalid appointment hours. Valid hours are between {0}h and {1}h", WORKING_HOURS_BEGIN, WORKING_HOURS_END), ex.getMessage());
    }

    @Test
    void should_ReturnError_WhenScheduledAfterWorkingHours() {
        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 7, WORKING_HOURS_END+1, 0, 0));

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals(MessageFormat.format("Invalid appointment hours. Valid hours are between {0}h and {1}h", WORKING_HOURS_BEGIN, WORKING_HOURS_END), ex.getMessage());
    }

    @Test
    void should_ReturnError_WhenScheduledWithLessThan30MinutesAhead() {

        // Changing working hours to ignore the working hours rule
        ReflectionTestUtils.setField(appointmentRuleHandler, "workingHoursBegin", 0);
        ReflectionTestUtils.setField(appointmentRuleHandler, "workingHoursEnd", 23);

        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.now(clock).plusMinutes(29));

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals("Cannot schedule appointment with less than 30 ahead.", ex.getMessage());
    }

    @Test
    void should_ReturnError_WhenMedicInactive() {

        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 7, 15, 0, 0));

        given(medicRepository.existsByIdAndActiveTrue(appointment.getMedic().getId())).willReturn(Boolean.FALSE);

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals("The referred medic is not active.", ex.getMessage());
    }

    @Test
    void should_ReturnError_WhenPatientInactive() {

        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 7, 15, 0, 0));

        given(patientRepository.existsByIdAndActiveTrue(appointment.getPatient().getId())).willReturn(Boolean.FALSE);

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals("The referred patient is not active.", ex.getMessage());
    }

    @Test
    void should_ReturnError_WhenMedicAlreadyBooked() {

        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 7, 15, 0, 0));

        given(appointmentRepository.existsByDateAndMedicId(appointment.getDate(), appointment.getPatient().getId())).willReturn(Boolean.TRUE);

        BadRequestException ex = Assertions.assertThrows(BadRequestException.class, () -> {
            appointmentRuleHandler.handleAppointmentRules(appointment);
        });

        assertEquals(MessageFormat.format("The medic is already booked for {0}", appointment.getDate()), ex.getMessage());
    }

    @Test
    void should_ReturnSuccess_WhenAllConditionsMet() {

        var appointment = new Appointment(1L,
            Mocks.createValidMedic(),
            Mocks.createValidPatient(),
            LocalDateTime.of(2023, 8, 7, 15, 0, 0));

        var validAppointment = appointmentRuleHandler.handleAppointmentRules(appointment);

        assertEquals(appointment, validAppointment);
    }

}
