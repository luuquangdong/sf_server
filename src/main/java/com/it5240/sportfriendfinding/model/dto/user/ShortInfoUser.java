package com.it5240.sportfriendfinding.model.dto.user;

import com.it5240.sportfriendfinding.model.unit.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortInfoUser {
    protected String phoneNumber;
    protected String name;
    protected LocalDate birthday;
    protected Media avatar;
}
