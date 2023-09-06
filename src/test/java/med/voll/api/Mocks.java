package med.voll.api;

import java.time.LocalDateTime;

import med.voll.api.domain.address.Address;
import med.voll.api.domain.address.AddressData;
import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.AppointmentInsertDto;
import med.voll.api.domain.appointment.AppointmentListDto;
import med.voll.api.domain.medic.Field;
import med.voll.api.domain.medic.Medic;
import med.voll.api.domain.medic.MedicInsertDto;
import med.voll.api.domain.medic.MedicListData;
import med.voll.api.domain.patient.Patient;
import med.voll.api.domain.patient.PatientInsertDto;
import med.voll.api.domain.user.User;
import med.voll.api.domain.user.UserProfileEnum;

public class Mocks {

    public static Address createValidAddress() {
        return new Address("Valid Street",
        "214128",
        "10",
        "Valid City",
        "Valid State");
    }

    public static Medic createValidMedic() {
        return new Medic(1L,
            "John Smith",
            "johnsmith@email.com",
            "23542352",
            "13415414", 
            Field.CARDIOLOGY,
            createValidAddress(),
            true);
    }

    public static MedicListData createValidMedicListData() {
        return new MedicListData(createValidMedic());
    }

    public static Patient createValidPatient() {
        return new Patient(1L,
        "Edgard Sick",
        "edgard_sick@mail.com",
        "13423553",
        "13542365",
        createValidAddress(),
        true);
    }

    public static User createValidUser() {
        return new User(1L,
            "newUser",
            "3j4bik213b4",
            UserProfileEnum.ROLE_USER,
            true);
    }

    public static AddressData createValidAddressData() {
        return new AddressData("New Street",
            "11124",
            "New City",
            "NS",
            "134");
    }

    public static PatientInsertDto createPatientInsertDto() {
        return new PatientInsertDto("New Patient",
            "new_patient@mail.com",
            "13942343",
            "3942034",
            createValidAddressData());
    }

    public static MedicInsertDto createMedicInsertDto() {
        return new MedicInsertDto("New Medic",
            "new_medic@email.com",
            "234235",
            "235235",
            Field.CARDIOLOGY,
            createValidAddressData());
    }

    public static Appointment createValidAppointment() {
        return new Appointment(1L,
            createValidMedic(),
            createValidPatient(),
            LocalDateTime.now());
    }

    public static AppointmentInsertDto createAppointmentInsertDto() {
        return new AppointmentInsertDto(1L,
            1L,
            LocalDateTime.now().plusHours(2L));
    }

    public static AppointmentListDto createAppointmentListDto() {
        return new AppointmentListDto(1L,
                            "Patient Name",
                            "Medic Name",
                            Field.CARDIOLOGY,
                            LocalDateTime.now());
    }

}
