CREATE TABLE appointments
(
 `id`         bigint NOT NULL auto_increment,
 `medic_id`   bigint NOT NULL ,
 `patient_id` bigint NOT NULL ,
 `date`       timestamp NOT NULL,

PRIMARY KEY (`id`),
CONSTRAINT `medic_appointment_fk` FOREIGN KEY `FK_1` (`medic_id`) REFERENCES `medics` (`id`),
CONSTRAINT `patient_appointment_fk` FOREIGN KEY `FK_2` (`patient_id`) REFERENCES `patients` (`id`)
);
