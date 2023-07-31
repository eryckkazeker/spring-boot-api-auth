package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.AppointmentDetailsDto;
import med.voll.api.domain.appointment.AppointmentInsertDto;
import med.voll.api.domain.appointment.AppointmentListDto;
import med.voll.api.domain.appointment.AppointmentRepository;
import med.voll.api.domain.appointment.rules.AppointmentRuleHandler;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentRuleHandler appointmentRuleHandler;

    @Autowired
    EntityManager entityManager;

    @GetMapping
    public ResponseEntity<Page<AppointmentListDto>> list(@PageableDefault(size = 10, sort = {"date"}) Pageable pagination) {
        return ResponseEntity.ok(appointmentRepository.findAll(pagination).map(AppointmentListDto::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDto> getDetails(@PathVariable Long id) {
        var appointment = appointmentRepository.getReferenceById(id);

        return ResponseEntity.ok(new AppointmentDetailsDto(appointment));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AppointmentDetailsDto> insert(@RequestBody @Valid AppointmentInsertDto insertDto, UriComponentsBuilder uriComponentsBuilder) {

        var insertAppointment = appointmentRuleHandler.handleAppointmentRules(new Appointment(insertDto));

        var savedAppointment = appointmentRepository.saveAndFlush(insertAppointment);

        entityManager.refresh(savedAppointment);

        var uri = uriComponentsBuilder.path("/appointments/{id}").buildAndExpand(savedAppointment.getId()).toUri();

        // var insertedAppointment = appointmentRepository.getReferenceById(savedAppointment.getId());

        return ResponseEntity.created(uri).body(new AppointmentDetailsDto(savedAppointment));
    }
    
}
