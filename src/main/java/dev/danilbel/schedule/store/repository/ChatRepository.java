package dev.danilbel.schedule.store.repository;

import dev.danilbel.schedule.store.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
}
