package ru.savrey.Sozinov_AV_diplom.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "farms")
@Data
@RequiredArgsConstructor
@Schema(name = "Хозяйство")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(name = "ID")
    private long farmId;

    @Column(name = "title")
    @Schema(name = "Название", minLength = 4, maxLength = 100)
    private String title;

    @Column(name = "address")
    @Schema(name = "Адрес", minLength = 3, maxLength = 100)
    private String address;

    @OneToMany(mappedBy = "farm",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    List<Field> fields;

    public void addField(Field field) {
        fields.add(field);
        field.setFarm(this);
    }

    public void removeField(Field field) {
        fields.remove(field);
        field.setFarm(null);
    }

    public Farm(String title) {
        this.title = title;
    }
}
