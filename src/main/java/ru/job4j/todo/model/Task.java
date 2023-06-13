package ru.job4j.todo.model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Task {
    @EqualsAndHashCode.Include
    private int id;

    @NonNull
    @EqualsAndHashCode.Include
    @ToString.Include
    private String description;

    private LocalDateTime created = LocalDateTime.now();

    @ToString.Include
    private boolean done;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy-EEEE HH:mm:ss");

    @ToString.Include
    public String created() {
        return FORMATTER.format(created);
    }
}
