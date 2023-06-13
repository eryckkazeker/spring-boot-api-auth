package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medic.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medics")
public class MedicController {

    @Autowired
    private MedicRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<MedicDetailsDto> insert(@RequestBody @Valid MedicInsertDto dados, UriComponentsBuilder uriComponentsBuilder) {
        var medico = repository.save(new Medic(dados));

        var uri = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new MedicDetailsDto(medico));
    }

    @GetMapping
    public ResponseEntity<Page<MedicListData>> list(@PageableDefault(size = 10, sort = {"name"}) Pageable paginacao) {
        var page = repository.findAllByActiveTrue(paginacao).map(MedicListData::new);
        return ResponseEntity.ok(page);
    }

    
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<MedicDetailsDto> getDetails(@PathVariable Long id) {
        
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new MedicDetailsDto(medico));        

    }

    @PutMapping
    @Transactional
    public ResponseEntity<Object> update(@RequestBody @Valid MedicUpdateDto dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.updateInfo(dados);
        return ResponseEntity.ok(new MedicDetailsDto(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.delete();
        return ResponseEntity.noContent().build();
    }
}
