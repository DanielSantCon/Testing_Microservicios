package com.minsait.testingMicroservices.repositories;

import com.minsait.testingMicroservices.models.Cuenta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

//Pruebas de integración


@DataJpaTest
public class CuentaRepositoryTest {
    @Autowired
    CuentaRepository repository;

    @BeforeEach
    void setUp()
    {

    }

    @Test
    void testFindById(){
        //When
        Optional<Cuenta> cuenta = repository.findById(1L);

        //Then
        assertTrue(cuenta.isPresent());
        assertEquals("Ricardo", cuenta.get().getPersona());
    }
    @Test
    void testFindByPersona(){
        Optional<Cuenta> cuenta = repository.findByPersona("Ricardo");
        assertFalse(cuenta.isEmpty());
        assertEquals(1L, cuenta.get().getId());
    }


    @Test
    void testBuscarPorPersona(){    Optional<Cuenta> cuenta = repository.buscarPorPersona("Ricardo");
        assertFalse(cuenta.isEmpty());    assertEquals(1L, cuenta.get().getId());}

    @Test
    void testSave()
    {
        Cuenta cuenta=new Cuenta(null,"Daniel",new BigDecimal(100000));
        Cuenta cuenta2=new Cuenta(null,"Javier",new BigDecimal(100000));
        repository.save(cuenta);
        repository.save(cuenta);
        Cuenta cuentaGuardada=repository.save(cuenta);

        assertEquals("Daniel",cuentaGuardada.getPersona());
        assertEquals(100000,cuentaGuardada.getSaldo().intValue());
    }

}
