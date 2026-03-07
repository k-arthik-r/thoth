package com.voidex.thoth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    @Nullable
    @JsonProperty
    private String fileName;

    @JsonProperty
    private byte[] content;

    @JsonProperty
    private String contentType;
}
