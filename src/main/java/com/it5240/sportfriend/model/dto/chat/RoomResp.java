package com.it5240.sportfriend.model.dto.chat;

import com.it5240.sportfriend.model.dto.post.Author;
import com.it5240.sportfriend.model.entity.Message;
import com.it5240.sportfriend.model.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResp extends Room {
    private List<Message> messages;
    private List<Author> chatters;
}
