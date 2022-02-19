package com.it5240.sportfriend.model.unit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBase {
    @Id
    @Pattern(regexp = "^0\\d{9}$", message = "phone number invalid")
    protected String phoneNumber;

    @NotBlank(message = "name is required")
    protected String name;

    protected LocalDate birthday;
    protected Media avatar;
    protected Gender gender;
    protected String description;
    protected Location location;
    protected boolean updatedInfo;
}
