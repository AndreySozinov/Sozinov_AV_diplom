package ru.savrey.Sozinov_AV_diplom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Название необходимо")
    @Size(min = 5, message = "Название должно быть не короче 5 букв")
    private String title;

    @Column(name = "address")
    @Schema(name = "Адрес", minLength = 3, maxLength = 100)
    private String address;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @Schema(name = "ID пользователя")
    private User user;

    @OneToMany(mappedBy = "farm",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL)
    private List<Field> fields = new ArrayList<>();

    public void addField(Field field) {
        fields.add(field);
        field.setFarm(this);
    }

    public void removeField(Field field) {
        fields.remove(field);
        field.setFarm(null);
    }

    public Farm(User user, String title) {
        this.user = user;
        this.title = title;
    }
}
