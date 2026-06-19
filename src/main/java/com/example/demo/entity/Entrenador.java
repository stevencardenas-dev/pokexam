package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "entrenador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_vinculacion", nullable = false)
    private LocalDate fechaVinculacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pueblo_id", nullable = false)
    private Pueblo pueblo;

    @Column(nullable = false, unique = true, length = 100)
    private String uuid;

    @OneToMany(mappedBy = "entrenador", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<com.example.demo.entity.Captura> capturas = new HashSet<>();

    public Set<Pokemon> getPokemons() {
        Set<Pokemon> result = new HashSet<>();
        for (com.example.demo.entity.Captura c : capturas) {
            if (c.getPokemon() != null) result.add(c.getPokemon());
        }
        return result;
    }
}
