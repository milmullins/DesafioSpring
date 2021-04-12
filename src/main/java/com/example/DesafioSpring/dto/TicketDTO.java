package com.example.DesafioSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {
    private static final AtomicInteger count = new AtomicInteger(0);
    private int id;
    private List<ArticleResponseDTO> articles;
    private double total;

    public TicketDTO(List<ArticleResponseDTO> articles, double total) {
        this.id = count.incrementAndGet();
        this.articles = articles;
        this.total = total;
    }
}
