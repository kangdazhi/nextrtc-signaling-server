package org.nextrtc.signalingserver.repository;

import com.google.common.collect.Maps;
import org.nextrtc.signalingserver.domain.Conversation;
import org.nextrtc.signalingserver.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.nextrtc.signalingserver.exception.Exceptions.CONVERSATION_NAME_OCCUPIED;

@Repository
public class Conversations implements ConversationRepository {

    private Map<String, Conversation> conversations = Maps.newConcurrentMap();

    @Override
    public Optional<Conversation> findBy(String id) {
        if (isEmpty(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(conversations.get(id));
    }

    @Override
    public Optional<Conversation> findBy(Member from) {
        return conversations.values().stream().filter(conversation -> conversation.has(from)).findAny();
    }

    @Override
    public Conversation remove(String id) {
        return conversations.remove(id);
    }

    @Override
    public Conversation save(Conversation conversation) {
        if (conversations.containsKey(conversation.getId())) {
            throw CONVERSATION_NAME_OCCUPIED.exception();
        }
        conversations.put(conversation.getId(), conversation);
        return conversation;
    }

    @Override
    public Collection<String> getAllIds() {
        return conversations.keySet();
    }
}
