package co.edu.usbcali.ecommerceusb.service.impl;

import co.edu.usbcali.ecommerceusb.dto.CreateUserRequest;
import co.edu.usbcali.ecommerceusb.dto.DeleteUserResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateUserRequest;
import co.edu.usbcali.ecommerceusb.dto.UserResponse;
import co.edu.usbcali.ecommerceusb.exception.BadRequestException;
import co.edu.usbcali.ecommerceusb.exception.InternalServerErrorException;
import co.edu.usbcali.ecommerceusb.exception.NotFoundException;
import co.edu.usbcali.ecommerceusb.mapper.UserMapper;
import co.edu.usbcali.ecommerceusb.model.DocumentType;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.repository.CartRepository;
import co.edu.usbcali.ecommerceusb.repository.DocumentTypeRepository;
import co.edu.usbcali.ecommerceusb.repository.OrderRepository;
import co.edu.usbcali.ecommerceusb.repository.UserRepository;
import co.edu.usbcali.ecommerceusb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<UserResponse> getUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                return List.of();
            }
            return UserMapper.modelToUserResponseList(users);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error al obtener los usuarios: " + e.getMessage());
        }
    }

    @Override
    public UserResponse getUserById(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para buscar");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Usuario no encontrado con el id: %d", id)));
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("Debe ingresar el email");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Usuario no encontrado con el email: %s", email)));
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest createUserRequest) {
        if (Objects.isNull(createUserRequest)) {
            throw new BadRequestException("El objeto createUserRequest no puede ser nulo.");
        }
        if (Objects.isNull(createUserRequest.getFullName()) ||
                createUserRequest.getFullName().isBlank()) {
            throw new BadRequestException("El campo fullName no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createUserRequest.getPhone()) ||
                createUserRequest.getPhone().isBlank()) {
            throw new BadRequestException("El campo phone no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createUserRequest.getEmail()) ||
                createUserRequest.getEmail().isBlank()) {
            throw new BadRequestException("El campo email no puede ser nulo ni vacío.");
        }
        if (createUserRequest.getDocumentTypeId() == null ||
                createUserRequest.getDocumentTypeId() <= 0) {
            throw new BadRequestException("El campo documentTypeId debe contener un valor mayor a 0.");
        }
        if (createUserRequest.getDocumentNumber() == null ||
                createUserRequest.getDocumentNumber().isBlank()) {
            throw new BadRequestException("El campo documentNumber no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createUserRequest.getBirthDate()) ||
                createUserRequest.getBirthDate().isBlank()) {
            throw new BadRequestException("El campo birthDate no puede ser nulo.");
        }
        if (Objects.isNull(createUserRequest.getCountry()) ||
                createUserRequest.getCountry().isBlank()) {
            throw new BadRequestException("El campo country no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(createUserRequest.getAddress()) ||
                createUserRequest.getAddress().isBlank()) {
            throw new BadRequestException("El campo address no puede ser nulo ni vacío.");
        }

        DocumentType documentType = documentTypeRepository.findById(createUserRequest.getDocumentTypeId())
                .orElseThrow(() -> new NotFoundException("El documentType no existe."));

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con el email ingresado.");
        }
        if (userRepository.existsByDocumentNumberAndDocumentTypeId(
                createUserRequest.getDocumentNumber(), createUserRequest.getDocumentTypeId())) {
            throw new BadRequestException("Ya existe un usuario con el documento y tipo de documento ingresado.");
        }

        User user = UserMapper.createUserRequestToUser(createUserRequest, documentType);
        user = userRepository.save(user);
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public UserResponse updateUser(Integer id, UpdateUserRequest updateUserRequest) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para actualizar");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Usuario no encontrado con el id: %d", id)));
        if (Objects.isNull(updateUserRequest)) {
            throw new BadRequestException("El objeto updateUserRequest no puede ser nulo.");
        }
        if (Objects.isNull(updateUserRequest.getFullName()) ||
                updateUserRequest.getFullName().isBlank()) {
            throw new BadRequestException("El campo fullName no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateUserRequest.getPhone()) ||
                updateUserRequest.getPhone().isBlank()) {
            throw new BadRequestException("El campo phone no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateUserRequest.getEmail()) ||
                updateUserRequest.getEmail().isBlank()) {
            throw new BadRequestException("El campo email no puede ser nulo ni vacío.");
        }
        if (updateUserRequest.getDocumentTypeId() == null ||
                updateUserRequest.getDocumentTypeId() <= 0) {
            throw new BadRequestException("El campo documentTypeId debe contener un valor mayor a 0.");
        }
        if (updateUserRequest.getDocumentNumber() == null ||
                updateUserRequest.getDocumentNumber().isBlank()) {
            throw new BadRequestException("El campo documentNumber no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateUserRequest.getBirthDate()) ||
                updateUserRequest.getBirthDate().isBlank()) {
            throw new BadRequestException("El campo birthDate no puede ser nulo.");
        }
        if (Objects.isNull(updateUserRequest.getCountry()) ||
                updateUserRequest.getCountry().isBlank()) {
            throw new BadRequestException("El campo country no puede ser nulo ni vacío.");
        }
        if (Objects.isNull(updateUserRequest.getAddress()) ||
                updateUserRequest.getAddress().isBlank()) {
            throw new BadRequestException("El campo address no puede ser nulo ni vacío.");
        }

        DocumentType documentType = documentTypeRepository.findById(updateUserRequest.getDocumentTypeId())
                .orElseThrow(() -> new NotFoundException("El documentType no existe."));

        if (userRepository.existsByEmail(updateUserRequest.getEmail()) &&
                !user.getEmail().equals(updateUserRequest.getEmail())) {
            throw new BadRequestException("Ya existe un usuario con el email ingresado.");
        }
        if (userRepository.existsByDocumentNumberAndDocumentTypeId(
                updateUserRequest.getDocumentNumber(),
                updateUserRequest.getDocumentTypeId()) &&
                (!user.getDocumentNumber().equals(updateUserRequest.getDocumentNumber()) ||
                        !user.getDocumentType().getId().equals(updateUserRequest.getDocumentTypeId()))) {
            throw new BadRequestException("Ya existe un usuario con el documento y tipo de documento ingresado.");
        }

        user.setFullName(updateUserRequest.getFullName());
        user.setPhone(updateUserRequest.getPhone());
        user.setEmail(updateUserRequest.getEmail());
        user.setDocumentType(documentType);
        user.setDocumentNumber(updateUserRequest.getDocumentNumber());
        user.setBirthDate(LocalDate.parse(
                updateUserRequest.getBirthDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        user.setCountry(updateUserRequest.getCountry());
        user.setAddress(updateUserRequest.getAddress());
        user.setUpdatedAt(OffsetDateTime.now());

        user = userRepository.save(user);
        return UserMapper.modelToUserResponse(user);
    }

    @Override
    public DeleteUserResponse deleteUser(Integer id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Debe ingresar el id para eliminar");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Usuario no encontrado con el id: %d", id)));

        if (orderRepository.existsByUserId(id)) {
            throw new BadRequestException("No se puede eliminar el usuario porque tiene órdenes asociadas.");
        }
        if (cartRepository.existsByUserId(id)) {
            throw new BadRequestException("No se puede eliminar el usuario porque tiene carritos asociados.");
        }

        userRepository.delete(user);
        return DeleteUserResponse.builder()
                .message(String.format("Usuario con id %d eliminado correctamente", id))
                .build();
    }
}