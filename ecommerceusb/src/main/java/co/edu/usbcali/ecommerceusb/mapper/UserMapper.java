package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.model.DocumentType;
import co.edu.usbcali.ecommerceusb.model.User;
import co.edu.usbcali.ecommerceusb.dto.UserResponse;

import java.util.List;


public class UserMapper {
    public static UserResponse modelToUserResponse(User user){
        DocumentType documentType = user.getDocumentType();
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .documentTypeId(user.getDocumentType() != null? user.getDocumentType().getId(): null)
                .documentTypeName(user.getDocumentType() != null? user.getDocumentType().getName(): null)
                .documentNumber(user.getDocumentNumber())
                .build();

    }
    public static List<UserResponse> modelToUserResponseList(List<User> users) {
        return users.stream().map(UserMapper::modelToUserResponse).toList();
    }
}
