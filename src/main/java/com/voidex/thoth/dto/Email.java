package com.voidex.thoth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor

public class Email {
    @JsonProperty
    private String[] recipient;

    @Nullable
    @JsonProperty
    private String[] cc;

    @Nullable
    @JsonProperty
    private String[] bcc;

    @JsonProperty
    private String subject;

    @JsonProperty
    private String body;

    @Nullable
    @JsonProperty
    private List<Attachment> attachments;

    @JsonProperty
    private Boolean isHtmlContent;
}
