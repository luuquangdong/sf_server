package com.it5240.sportfriendfinding.model.atom;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentBase {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    protected ObjectId id;

    protected String organizationId;
    @NotBlank
    protected String name;
    @NotBlank
    protected String description;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected TournamentStatus status;

}
