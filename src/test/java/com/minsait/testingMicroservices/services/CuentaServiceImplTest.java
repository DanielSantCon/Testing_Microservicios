package com.minsait.testingMicroservices.services;


import com.minsait.testingMicroservices.TestingMicroservicesApplication;
import com.minsait.testingMicroservices.controllers.Datos;
import com.minsait.testingMicroservices.models.Banco;
import com.minsait.testingMicroservices.models.Cuenta;
import com.minsait.testingMicroservices.repositories.BancoRepository;
import com.minsait.testingMicroservices.repositories.CuentaRepository;
import com.minsait.testingMicroservices.services.CuentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CuentaServiceImplTest {
    @Mock
    BancoRepository bancoRepository;
    @Mock
    CuentaRepository cuentaRepository;

    @InjectMocks
    CuentaServiceImpl service;


    @Test
    void testfindAll() {
        List<Cuenta> cuentas = new ArrayList<>();
        when(service.findAll()).thenReturn(List.of(Datos.crearCuenta1()
                .get(), Datos.crearCuenta2().get()));
        cuentas = service.findAll();
        assertTrue(!cuentas.isEmpty());
        verify(cuentaRepository, atLeastOnce()).findAll();
    }

    @Test
    void testFindById() {
        given(cuentaRepository.findById(1L)).willReturn(Optional.of(Datos.crearCuenta1().get()));

        Cuenta cuenta = service.findById(Datos.crearCuenta1().get().getId());

        assertThat(cuenta).isNotNull();

    }

    @Test
    void testSave() {
        Cuenta cuenta = new
                Cuenta(11L, "VICTOR", new BigDecimal(100));
        given(cuentaRepository.save(cuenta)).willReturn(cuenta);
        Cuenta nuevaCuenta = service.save(cuenta);
        assertThat(nuevaCuenta).isNotNull();
    }

    @Test
    void testDeletedById(){
        given(cuentaRepository.findById(1L)).willReturn(Optional.of(Datos.crearCuenta1().get()));
        boolean deleted = service.deletedById(1L);
        assertTrue(deleted);}

    @Test
    void testRevisarSaldo()
    {

        given(cuentaRepository.findById(1L)).willReturn(Optional.of(Datos.crearCuenta1().get()));
        BigDecimal saldo = service.revisarSaldo(1L);
        assertTrue(Datos.crearCuenta1().get().getSaldo().equals(saldo));}

    @Test
    void testRevisarTotalTransferencia() {
        given(bancoRepository.findById(1L))
            .willReturn(Optional.of(Datos.crearBanco().get()));
        int total = service.revisarTotalTransferencias(1L);
        assertTrue(total >= 0);}


    @Test
    void testTransferir()
    {
        Banco banco = new Banco(1L, "BANCO", 0);
        Cuenta cuentaOrigen = new Cuenta(1L, "Yamani", new BigDecimal(1000));
        Cuenta cuentaDestino = new Cuenta(2L, "Daniel", new BigDecimal(1000));
        int monto = 500;
        given(cuentaRepository.findById(cuentaOrigen.getId())).willReturn(Optional.of(Datos.crearCuenta1().get()));
        given(cuentaRepository.findById(cuentaDestino.getId())).willReturn(Optional.of(Datos.crearCuenta2().get()));
        given(bancoRepository.findById(banco.getId())).willReturn(Optional.of(Datos.crearBanco().get()));
        service.transferir(cuentaOrigen.getId(), cuentaDestino.getId(), new BigDecimal(monto), banco.getId());
        int total = bancoRepository.findById(banco.getId()).get().getTotalTransferencias();    assertTrue(total==1);


    }
    @Test
    void testDeletedByIdNotFound()
    {
        boolean deleted = service.deletedById(5L);
        assertFalse(deleted);
    }

}