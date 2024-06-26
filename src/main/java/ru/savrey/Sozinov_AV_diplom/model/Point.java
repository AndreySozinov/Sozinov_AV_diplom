package ru.savrey.Sozinov_AV_diplom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

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
    @Positive(message = "Координаты обязательны")
    private double latitude;

    @Column(name = "longitude", nullable = false, precision = 7)
    @Schema(name = "Долгота")
    @Positive(message = "Координаты обязательны")
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
    private double ph;

    @Column(name = "density", precision = 2)
    @Schema(name = "Плотность")
    private double density;

    @Column(name = "sampled_in")
    @Schema(name = "Дата отбора")
    private LocalDate sampled_in;

    public Point(Field field, double latitude, double longitude, LocalDate sampled_in) {
        this.field = field;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sampled_in = sampled_in;
    }
}
