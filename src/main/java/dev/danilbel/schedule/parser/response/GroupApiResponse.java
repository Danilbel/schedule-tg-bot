package dev.danilbel.schedule.parser.response;

import dev.danilbel.schedule.domain.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GroupApiResponse {

    List<Group> data;
}
