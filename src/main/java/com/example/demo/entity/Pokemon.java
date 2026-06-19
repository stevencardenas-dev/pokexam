package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pokemon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_pokemon", nullable = false)
    private TipoPokemon tipoPokemon;

    @Column(name = "fecha_descubrimiento")
    private LocalDate fechaDescubrimiento;

    @Column(name = "generacion")
    private Integer generacion;

    @Column(nullable = false, unique = true, length = 100)
    private String uuid;

    @ManyToMany(mappedBy = "pokemons", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<Entrenador> entrenadores = new HashSet<>();

    @OneToMany(mappedBy = "pokemon", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private Set<com.example.demo.entity.Captura> capturas = new HashSet<>();
}
