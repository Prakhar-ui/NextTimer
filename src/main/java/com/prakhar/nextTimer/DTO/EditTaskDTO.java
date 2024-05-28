package com.prakhar.nextTimer.DTO;

public record EditTaskDTO(Long id, String name, String description, String timerType, long seconds) {
}