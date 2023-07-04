package dev.danilbel.schedule.domain.dto;

import dev.danilbel.schedule.store.entity.ChatEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Chat {

    Long id;

    String groupName;

    String groupId;

    public static Chat makeDto(ChatEntity entity) {
        return builder()
                .id(entity.getId())
                .groupName(entity.getGroupName())
                .groupId(entity.getGroupId())
                .build();
    }
}
