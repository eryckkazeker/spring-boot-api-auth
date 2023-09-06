package med.voll.api.controller;

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

import jakarta.validation.Valid;
import med.voll.api.domain.appointment.Appointment;
import med.voll.api.domain.appointment.AppointmentDetailsDto;
import med.voll.api.domain.appointment.AppointmentInsertDto;
import med.voll.api.domain.appointment.AppointmentListDto;
import med.voll.api.domain.service.AppointmentService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentListDto>> list(@PageableDefault(size = 10, sort = {"date"}) Pageable pagination) {
        return ResponseEntity.ok(appointmentService.listAppointments(pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDetailsDto> getDetails(@PathVariable Long id) {
        
        var appointment = appointmentService.getAppointmentDetails(id);

        return ResponseEntity.ok(new AppointmentDetailsDto(appointment));

    }

    @PostMapping
    @Transactional
    public ResponseEntity<AppointmentDetailsDto> insert(@RequestBody @Valid AppointmentInsertDto insertDto, UriComponentsBuilder uriComponentsBuilder) {

        var insertedAppointment = appointmentService.saveAppointment(new Appointment(insertDto));

        var uri = uriComponentsBuilder.path("/appointments/{id}").buildAndExpand(insertedAppointment.getId()).toUri();

        return ResponseEntity.created(uri).body(new AppointmentDetailsDto(insertedAppointment));
    }
    
}
