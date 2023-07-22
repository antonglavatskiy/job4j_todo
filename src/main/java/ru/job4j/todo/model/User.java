package ru.job4j.todo.model;

import lombok.*;

import javax.persistence.*;
import java.time.ZoneId;
import java.util.TimeZone;

@Entity
@Table(name = "todo_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    @EqualsAndHashCode.Include
    private String login;

    private String password;

    @Column(name = "user_zone")
    private String timezone;
}
