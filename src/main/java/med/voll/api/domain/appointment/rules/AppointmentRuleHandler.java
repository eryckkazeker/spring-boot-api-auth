package med.voll.api.domain.appointment.rules;

import med.voll.api.domain.appointment.Appointment;

public interface AppointmentRuleHandler {

    Appointment handleAppointmentRules(Appointment appointment);
    
}
