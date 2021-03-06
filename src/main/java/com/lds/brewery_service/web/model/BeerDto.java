package com.lds.brewery_service.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerDto implements Serializable {

    static final long serialVersionUID = 1114715135625846949L;
    @Null
    private UUID id;

    @NotBlank
    @Size(min = 3,max = 100)
    private String beerName;

    @NotNull
    private BeerStyleEnum beerStyle;

    @Null
    private Integer version;

    @Null
    @JsonFormat(pattern = "yyy-MM-dd'T'HH:MM:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;

    @Null
    @JsonFormat(pattern = "yyy-MM-dd'T'HH:MM:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate;


    @NotNull
    private String upc;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Positive
    @NotNull
    private BigDecimal price;

    @Positive
    private Integer quantityOnHand;
}
