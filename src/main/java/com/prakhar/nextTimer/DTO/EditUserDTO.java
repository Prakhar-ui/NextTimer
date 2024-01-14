package com.prakhar.nextTimer.DTO;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Data
@AllArgsConstructor
public class EditUserDTO {
    private String id;
    private String name;
    private int age;
    private String email;
    private String password;


}
