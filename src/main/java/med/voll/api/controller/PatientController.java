package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.patient.*;
import med.voll.api.domain.persistence.repository.PatientRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("patients")
public class PatientController {

    private final PatientRepository repository;

    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PatientDetailsDto> insert(@RequestBody @Valid PatientInsertDto dados, UriComponentsBuilder uriComponentsBuilder) {
        var paciente = repository.save(new Patient(dados));

        var uri = uriComponentsBuilder.path("/patients/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new PatientDetailsDto(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<PatientListDto>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable page) {
        var pages = repository.findAllByActiveTrue(page).map(PatientListDto::new);

        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDetailsDto> getDetails(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new PatientDetailsDto(paciente));

    }

    @PutMapping
    @Transactional
    public ResponseEntity<PatientDetailsDto> update(@RequestBody @Valid PatientUpdateDto dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.updateInfo(dados);

        return ResponseEntity.ok(new PatientDetailsDto(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.remove();
        return ResponseEntity.noContent().build();
    }


}
