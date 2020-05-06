package com.lds.brewery_service.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lds.brewery_service.web.BeerService;
import com.lds.brewery_service.web.model.BeerDto;
import com.lds.brewery_service.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;

import static org.springframework.restdocs.request.RequestDocumentation.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    public static final String BEER_1_UPC = "763487556709";
    public static final String BEER_2_UPC = "073785456888";

    @MockBean
    BeerService beerService;

    MockMvc mockMvc;

    ObjectMapper objectMapper;

    @Autowired
    public BeerControllerTest(MockMvc mockMvc,ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void getAllBeer() throws Exception {
        Mockito.when(beerService.listAllBeer()).thenReturn(listAll());

        mockMvc.perform(
                get(BeerController.BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


    @Test
    void getBeer() throws Exception {

        Mockito.when(beerService.findByID(Mockito.any())).thenReturn(validBeerDto());

        mockMvc.perform(
            get(BeerController.BASE_URL+"/{beerId}", UUID.randomUUID())
                .param("test","test")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                    pathParameters(
                            parameterWithName("beerId").description("UUID of a beer you want to get.")
                    ),
                    responseFields(
                            fieldWithPath("id").ignored(),
                            fieldWithPath("beerName").description("Name of the beer"),
                            fieldWithPath("beerStyle").description("Style of the beer"),
                            fieldWithPath("version").ignored(),
                            fieldWithPath("createdDate").ignored(),
                            fieldWithPath("lastModifiedDate").ignored(),
                            fieldWithPath("upc").description("UPC of the beer"),
                            fieldWithPath("price").description("Price of the beer"),
                            fieldWithPath("quantityOnHand").description("Quantity on hand")
                    )
            ));
    }



    @Test
    void saveNewBeer() throws Exception {

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        mockMvc.perform(
            post(BeerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerDto())))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer-new",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("beerName").description("Name of the beer"),
                                fields.withPath("beerStyle").description("Style of the beer"),
                                fields.withPath("version").ignored(),
                                fields.withPath("createdDate").ignored(),
                                fields.withPath("lastModifiedDate").ignored(),
                                fieldWithPath("upc").attributes(Attributes.key("constraints").value(StringUtils
                                        .collectionToDelimitedString(new ConstraintDescriptions(BeerDto.class)
                                                .descriptionsForProperty("upc"), ". ")))
                                        .description("UPC of the beer"),
                                fields.withPath("price").description("Price of the beer"),
                                fields.withPath("quantityOnHand").description("Quantity on hand")
                        )));
    }

    @Test
    void updateBeer() throws Exception {

        Mockito.when(beerService.findByID(Mockito.any())).thenReturn(validBeerDto());

        mockMvc.perform(
                put(BeerController.BASE_URL+"/{beerId}",UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerDto())))
                .andExpect(status().isNoContent())
                .andDo(document("v1/beer-update",
                        pathParameters(
                            parameterWithName("beerId").description("Id of a beer you want.")
                        ),
                        requestFields(
                                fieldWithPath("id").ignored(),
                                fieldWithPath("beerName").description("Name of the beer"),
                                fieldWithPath("beerStyle").description("Style of the beer"),
                                fieldWithPath("version").ignored(),
                                fieldWithPath("createdDate").ignored(),
                                fieldWithPath("lastModifiedDate").ignored(),
                                fieldWithPath("upc").description("UPC of the beer"),
                                fieldWithPath("price").description("Price of the beer"),
                                fieldWithPath("quantityOnHand").description("Quantity on hand")
                        )));;
    }

    private List<BeerDto> listAll() {
        return Arrays.asList(
                BeerDto.builder()
                        .beerName("Galaxy cat")
                        .beerStyle(BeerStyleEnum.PALE_ALE)
                        .quantityOnHand(200)
                        .price(new BigDecimal("11.50"))
                        .upc(BEER_1_UPC)
                        .build(),
                BeerDto.builder()
                        .beerName("No Hammers on the bar")
                        .beerStyle(BeerStyleEnum.ALE)
                        .quantityOnHand(200)
                        .price(new BigDecimal("11.50"))
                        .upc(BEER_2_UPC)
                        .build()
        );
    }

    private BeerDto validBeerDto() {
        return BeerDto.builder()
                .beerName("Galaxy cat")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .quantityOnHand(200)
                .price(new BigDecimal("11.50"))
                .upc(BEER_1_UPC)
                .build();
    }

    private static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(Attributes.key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}