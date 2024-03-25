package ru.savrey.Sozinov_AV_diplom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "points")
@Data
@RequiredArgsConstructor
@Schema(name = "Точка")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(name = "ID")
    private long pointId;

    @ManyToOne
    @JoinColumn(name = "field_id")
    @JsonIgnore
    @Schema(name = "ID поля")
    private Field field;

    @Column(name = "latitude", nullable = false, precision = 7)
    @Schema(name = "Широта")
    private double latitude;

    @Column(name = "longitude", nullable = false, precision = 7)
    @Schema(name = "Долгота")
    private double longitude;

    @Column(name = "humus", precision = 2)
    @Schema(name = "Гумус")
    private double humus;

    @Column(name = "phosphorus", precision = 1)
    @Schema(name = "Фосфор")
    private double phosphorus;

    @Column(name = "potassium")
    @Schema(name = "Калий")
    private int potassium;

    @Column(name = "pH", precision = 2)
    @Schema(name = "pH")
    private double pH;

    @Column(name = "density", precision = 2)
    @Schema(name = "Плотность")
    private double density;

    public Point(Field field, double latitude, double longitude) {
        this.field = field;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
