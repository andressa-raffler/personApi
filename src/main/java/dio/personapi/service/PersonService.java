package dio.personapi.service;

import dio.personapi.dto.response.MessageResponseDTO;
import dio.personapi.dto.request.PersonDTO;
import dio.personapi.entity.Person;
import dio.personapi.exception.PersonNotFoundException;
import dio.personapi.mapper.PersonMapper;
import dio.personapi.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);
        Person savedPerson = personRepository.save(personToSave);
        return createMessageRespose("Person with id" + savedPerson.getId() + " was created!");
    }

    public List<PersonDTO> listAll() {
        List<Person> allPeople = personRepository.findAll();
        return allPeople.stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person personSaved = verifyIfPersonExists(id);
        return personMapper.toDTO(personSaved);
    }

    public void deleteById(Long id) throws PersonNotFoundException {
        Person personSaved = verifyIfPersonExists(id);
        personRepository.delete(personSaved);
    }
    private Person verifyIfPersonExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    public MessageResponseDTO updateById(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfPersonExists(id);
        Person personToUpdate = personMapper.toModel(personDTO);
        Person updatedPerson = personRepository.save(personToUpdate);
        return createMessageRespose("Person with id" + id + " was updated!");
    }

    private static MessageResponseDTO createMessageRespose(String text) {
        return MessageResponseDTO
                .builder()
                .message(text)
                .build();
    }
}
