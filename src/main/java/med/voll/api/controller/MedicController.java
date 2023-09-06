package med.voll.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import med.voll.api.domain.medic.Medic;
import med.voll.api.domain.medic.MedicDetailsDto;
import med.voll.api.domain.medic.MedicInsertDto;
import med.voll.api.domain.medic.MedicListData;
import med.voll.api.domain.medic.MedicUpdateDto;
import med.voll.api.domain.service.MedicService;

@RestController
@RequestMapping("medics")
public class MedicController {

    private final MedicService medicService;

    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<MedicDetailsDto> insert(@RequestBody @Valid MedicInsertDto data, UriComponentsBuilder uriComponentsBuilder) {

        var medic = medicService.saveMedic(new Medic(data));

        var uri = uriComponentsBuilder.path("/medics/{id}").buildAndExpand(medic.getId()).toUri();

        return ResponseEntity.created(uri).body(new MedicDetailsDto(medic));
    }

    @GetMapping
    public ResponseEntity<Page<MedicListData>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        var page = medicService.listMedics(pageable);
        return ResponseEntity.ok(page);
    }

    
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<MedicDetailsDto> getDetails(@PathVariable Long id) {
        
        var medic = medicService.getDetails(id);

        return ResponseEntity.ok(new MedicDetailsDto(medic));        

    }

    @PutMapping
    @Transactional
    public ResponseEntity<MedicDetailsDto> update(@RequestBody @Valid MedicUpdateDto data) {
        var medic = medicService.update(data);
        return ResponseEntity.ok(new MedicDetailsDto(medic));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        medicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
