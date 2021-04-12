package com.example.DesafioSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private TicketDTO ticket;
    private StatusCodeDTO statusCode;
}
