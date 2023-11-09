package com.polus.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.polus.test.bank.ctrls.BankCtrl;
import com.polus.test.bank.modals.Bank;
import com.polus.test.bank.repositories.BankRepository;
import com.polus.test.bank.services.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class BankCtrlTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BankService bankService;

    @InjectMocks
    private BankCtrl bankCtrl;

    Bank BANK_1 = new Bank(1, "BANK_1", "BANK_1@BANK_3.com", "9874563***");
    Bank BANK_2 = new Bank(2, "BANK_2", "BANK_2@BANK_3.com", "9874598***");
    Bank BANK_3 = new Bank(3, "BANK_3", "BANK_3@BANK_3.com", "9874563***");

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(bankCtrl,bankService).build();
    }

    @Test
    public void getAllBankDetails_success() throws Exception {
        List<Bank> bankList = new ArrayList<>(Arrays.asList(BANK_1, BANK_2, BANK_3));
        Mockito.when(bankRepository.findAll()).thenReturn(bankList);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/bank")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].id", is(3)));
    }

    @Test
    public void getAllBankDetailsById_success() throws Exception {
        Mockito.when(bankRepository.findById(BANK_2.getId())).thenReturn(java.util.Optional.of(BANK_2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/bank/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("BANK_2")));
    }

    @Test
    public void saveBank_success() throws Exception {
        Bank bank = Bank.builder()
                .id(100)
                .name("BANK_4")
                .email("BANK_3@BANK_3.com")
                .phoneNumber("9874521***").build();

        Mockito.when(bankService.saveBankDetails(Mockito.any(Bank.class))).thenReturn(bank);

        String bankObj = objectWriter.writeValueAsString(bank);
//        this.mockMvc.perform(post("/bank")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(bankObj)).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.name").value("Fedaral Bank"));

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/bank")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bankObj);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("BANK_4")));

    }

    @Test
    public void updateBankDetails_success() throws Exception {
        Bank bank = Bank.builder()
                .id(1)
                .name("BANK_1 Bank")
                .email("bank_1.bank@bank_1.com")
                .phoneNumber("98745*****").build();

        Mockito.when(bankRepository.findById(BANK_1.getId())).thenReturn(java.util.Optional.of(BANK_1));
        Mockito.when(bankRepository.save(Mockito.any(Bank.class))).thenReturn(bank);

        String bankObj = objectWriter.writeValueAsString(bank);
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/bank")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(bankObj);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("BANK_1 Bank")));

    }
}
