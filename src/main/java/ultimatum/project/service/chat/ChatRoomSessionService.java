package ultimatum.project.service.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatRoomSessionService {
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();

    public void addSessionToRoom(Long chatRoomId, WebSocketSession session) {
        chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> Collections.newSetFromMap(new ConcurrentHashMap<>())).add(session);
    }

    public void removeSessionFromRoom(Long chatRoomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = chatRoomSessionMap.get(chatRoomId);
        if (sessions != null) {
            boolean isRemoved = sessions.remove(session);
            log.info("Removing session {} from chat room {}: {}", session.getId(), chatRoomId, isRemoved);
            if (sessions.isEmpty()) {
                chatRoomSessionMap.remove(chatRoomId);
                log.info("Chat room {} is now empty and removed from map.", chatRoomId);
            }
        } else {
            log.warn("No sessions found for chat room {}, nothing to remove.", chatRoomId);
        }
    }

    public Set<WebSocketSession> getSessionsForRoom(Long chatRoomId) {
        return chatRoomSessionMap.getOrDefault(chatRoomId, Collections.emptySet());
    }

    public List<Long> getConnectedRoomsForMember(Long memberId) {
        return chatRoomSessionMap.entrySet().stream()
                .filter(entry -> entry.getValue().stream()
                        .anyMatch(session -> {
                            Long sessionMemberId = (Long) session.getAttributes().get("email");
                            return sessionMemberId != null && sessionMemberId.equals(memberId);
                        }))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}