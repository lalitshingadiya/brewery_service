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
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
            get(BeerController.BASE_URL+"/"+ UUID.randomUUID())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }



    @Test
    void saveNewBeer() throws Exception {
        mockMvc.perform(
            post(BeerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerDto())))
                .andExpect(status().isCreated());
    }

    @Test
    void updateBeer() throws Exception {

        Mockito.when(beerService.findByID(Mockito.any())).thenReturn(validBeerDto());

        mockMvc.perform(
                put(BeerController.BASE_URL+"/"+ UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerDto())))
                .andExpect(status().isNoContent());
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
}