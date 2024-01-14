package com.prakhar.nextTimer.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TimerDTO {
    private String id;
    private long seconds;
}
