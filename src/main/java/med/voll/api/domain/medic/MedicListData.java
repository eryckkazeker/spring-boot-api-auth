package med.voll.api.domain.medic;

public record MedicListData(Long id, String name, String email, String register, Field field) {

    public MedicListData(Medic medic) {
        this(medic.getId(), medic.getName(), medic.getEmail(), medic.getRegister(), medic.getField());
    }

}
