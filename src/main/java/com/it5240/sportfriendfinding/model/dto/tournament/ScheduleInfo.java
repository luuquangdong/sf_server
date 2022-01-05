package com.it5240.sportfriendfinding.model.dto.tournament;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.it5240.sportfriendfinding.model.atom.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInfo {
    @JsonSerialize(using = ToStringSerializer.class)
    protected ObjectId id;

    protected String organizationId;
    protected List<Schedule> schedules;
}
