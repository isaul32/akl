package fi.tite.akl.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserListDto {
    List<UserTransferDto> users;
}
