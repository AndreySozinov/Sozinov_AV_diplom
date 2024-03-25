package ru.savrey.Sozinov_AV_diplom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "fields")
@Data
@RequiredArgsConstructor
@Schema(name = "Поле")
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(name = "ID")
    private long fieldId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farm_id", nullable = false)
    @JsonIgnore
    @Schema(name = "ID хозяйства")
    private Farm farm;

    @Column(name = "area")
    @Schema(name = "Площадь")
    private int area;

    @Column(name = "soil")
    @Schema(name = "Почва", minLength = 4, maxLength = 100)
    private String soil;

    @Column(name = "description")
    @Schema(name = "Описание", minLength = 3, maxLength = 100)
    private String description;

    @OneToMany(mappedBy = "field",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    List<Point> points;

    public void addPoint(Point point) {
        points.add(point);
        point.setField(this);
    }

    public void removePoint(Point point) {
        points.remove(point);
        point.setField(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Field field = (Field) o;
        return fieldId == field.fieldId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldId);
    }

    public Field(Farm farm, int area) {
        this.farm = farm;
        this.area = area;
    }
}
