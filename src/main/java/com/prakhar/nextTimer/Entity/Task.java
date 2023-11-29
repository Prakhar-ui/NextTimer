package com.prakhar.nextTimer.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String timerType;
    private long seconds;
    private int priority;
    private Boolean enabled;

    public Task(String name, String description, String timerType, long seconds, int priority, Boolean enabled) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.timerType = timerType;
        this.seconds = seconds;
        this.priority = priority;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timerType='" + timerType + '\'' +
                ", timer=" + seconds +
                ", priority=" + priority +
                ", enabled=" + enabled +
                '}';
    }
}
