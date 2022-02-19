package com.it5240.sportfriend.model.unit;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

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

    protected String details;
    protected String startTime;
    protected String endTime;
    protected String location;
    protected String sportName;
    protected String sportId;
    protected TournamentStatus status;

}
