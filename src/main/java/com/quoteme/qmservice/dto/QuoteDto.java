package com.quoteme.qmservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quoteme.qmservice.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto {

    UUID id;

    String author;

    UUID userId;

    @NotNull(message = "category cannot be empty")
    Category category;

    @NotEmpty(message = "text cannot be empty")
    String text;

    Set<String> tags;

    @JsonProperty(value = "private")
    boolean privateQuote;

}
