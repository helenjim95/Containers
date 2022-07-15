package de.tum.in.ase.eist;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.repository.PersonRepository;
import de.tum.in.ase.eist.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PersonServiceTest {
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepository personRepository;

    @Test
    void testAddPerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        personService.save(person);

        assertEquals(1, personRepository.findAll().size());
    }

    @Test
    void testDeletePerson() {
        var person = new Person();
        person.setFirstName("Max");
        person.setLastName("Mustermann");
        person.setBirthday(LocalDate.now());

        person = personRepository.save(person);

        personService.delete(person);

        assertTrue(personRepository.findAll().isEmpty());
    }

    @Test
    void testAddParent() {
//        Create two instances of Person, one acts as a child and one as a parent,
//        save these objects to the database.
//        Invoke the method addParent in PersonService.
//        Verify that all instances of Person have been correctly saved to the database
//        and that the parent-child relationship has been created only for the first two added parents
//        (in the database and the returned object of addParent).
        var personParent = new Person();
        personParent.setFirstName("Max");
        personParent.setLastName("Mustermann");
        personParent.setBirthday(LocalDate.of(1990, 2, 18));

        var personChild = new Person();
        personChild.setFirstName("Jonas");
        personChild.setLastName("Mustermann");
        personChild.setBirthday(LocalDate.now());

        Person parentSavedDatabase = personService.save(personParent);
        Person childSavedDatabase = personService.save(personChild);

        Person childSaved = personService.addParent(personChild, personParent);
        Set<Person> parentSet = childSaved.getParents();

        Set<Person> childrenSetDatabase = parentSavedDatabase.getChildren();
        Set<Person> parentSetDatabase = childSavedDatabase.getParents();

        assertEquals(2, personRepository.findAll().size());
        assertTrue(personChild.equals(childSaved));
        assertEquals(parentSet, parentSetDatabase);
    }

    @Test
    void testAddThreeParents() {
//        Create four instances of Person,
//        one acts as a child and three as parents,
//        and save these objects to the database.
//        Invoke the method addParent twice
//        so that the parent-child relationships between the two instances of Person are created.
//        Assert that the method invocations return the correct persons,
//        the parent and child have been correctly saved to the database,
//        and that the parent-child relationship is created (in the database
//        and the returned objects of addParent).
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

        Person parent1SavedDatabase = personRepository.save(personParent1);
        Person parent2SavedDatabase = personRepository.save(personParent2);
        Person parent3SavedDatabase = personRepository.save(personParent3);
        Person childSavedDatabase = personRepository.save(personChild);

        Person childSaved = personService.addParent(personChild, personParent1);
        Set<Person> childrenSet = childSaved.getParents();
        Person child2Saved = personService.addParent(personChild, personParent2);
        Set<Person> childrenSet2 = child2Saved.getParents();

        Set<Person> childrenSetDatabase = parent1SavedDatabase.getChildren();
        Set<Person> parentSet = childSavedDatabase.getParents();

        assertEquals(4, personRepository.findAll().size());
        assertTrue(personParent1.equals(parent1SavedDatabase));
        assertTrue(personParent2.equals(parent2SavedDatabase));
        assertThrows(BadRequestAlertException.class, () -> personService.addParent(personChild, personParent3));
    }
}
