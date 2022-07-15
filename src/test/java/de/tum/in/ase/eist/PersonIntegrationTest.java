package de.tum.in.ase.eist;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepository;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testAddPerson() throws Exception {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        var response = this.mvc.perform(
                post("/persons")
                        .content(objectMapper.writeValueAsString(person))
                        .contentType("application/json")
        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(1, personRepository.findAll().size());
    }

    @Test
    void testDeletePerson() throws Exception {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        person = personRepository.save(person);

        var response = this.mvc.perform(
                delete("/persons/" + person.getId())
                        .contentType("application/json")
        ).andReturn().getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertTrue(personRepository.findAll().isEmpty());
    }

    @Test
    void testAddParent() throws Exception {
        var personParent = new Person();
        personParent.setFirstName("Max");
        personParent.setLastName("Mustermann");
        personParent.setBirthday(LocalDate.of(1990, 2, 18));

        var personChild = new Person();
        personChild.setFirstName("Jonas");
        personChild.setLastName("Mustermann");
        personChild.setBirthday(LocalDate.now());

        var responseParent = this.mvc.perform(
                put("/api/persons/{personId}/parents")
                        .content(objectMapper.writeValueAsString(personParent))
                        .contentType("application/json")
        ).andReturn().getResponse();

//        var responseChild = this.mvc.perform(
//                put("/api/persons/{personId}/parents")
//                        .content(objectMapper.writeValueAsString(personChild))
//                        .contentType("application/json")
//        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), responseParent.getStatus());
        assertEquals(2, personRepository.findAll().size());
    }

    @Test
    void testAddThreeParents() throws Exception {
        var personParent1 = new Person();
        personParent1.setFirstName("Max");
        personParent1.setLastName("Mustermann");
        personParent1.setBirthday(LocalDate.of(1990, 2, 18));

        var personParent2 = new Person();
        personParent2.setFirstName("Shawn");
        personParent2.setLastName("Mendes");
        personParent2.setBirthday(LocalDate.of(1995, 4, 9));

        var personParent3 = new Person();
        personParent3.setFirstName("Yamashita");
        personParent3.setLastName("Tomohisa");
        personParent3.setBirthday(LocalDate.of(1991, 9, 1));

        var personChild = new Person();
        personChild.setFirstName("Jonas");
        personChild.setLastName("Mustermann");
        personChild.setBirthday(LocalDate.now());

        var responseParent1 = this.mvc.perform(
                put("/api/persons/{personId}/parents")
                        .content(objectMapper.writeValueAsString(personParent1))
                        .contentType("application/json")
        ).andReturn().getResponse();

        var responseParent2 = this.mvc.perform(
                put("/api/persons/{personId}/parents")
                        .content(objectMapper.writeValueAsString(personParent2))
                        .contentType("application/json")
        ).andReturn().getResponse();

        var responseParent3 = this.mvc.perform(
                put("/api/persons/{personId}/parents")
                        .content(objectMapper.writeValueAsString(personParent3))
                        .contentType("application/json")
        ).andReturn().getResponse();

//        var responseChild = this.mvc.perform(
//                put("/api/persons/{personId}/parents")
//                        .content(objectMapper.writeValueAsString(personChild))
//                        .contentType("application/json")
//        ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), responseParent1.getStatus());
        assertEquals(HttpStatus.OK.value(), responseParent2.getStatus());
        assertEquals(HttpStatus.OK.value(), responseParent3.getStatus());
        assertEquals(4, personRepository.findAll().size());

    }
}
