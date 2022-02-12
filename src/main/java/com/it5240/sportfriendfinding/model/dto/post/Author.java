package com.it5240.sportfriendfinding.model.dto.post;

import com.it5240.sportfriendfinding.model.unit.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    private String id;
    private String name;
    private Media avatar;
}
