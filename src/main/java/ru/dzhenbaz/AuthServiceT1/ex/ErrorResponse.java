package ru.dzhenbaz.AuthServiceT1.ex;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private String field;
    private String message;
}
