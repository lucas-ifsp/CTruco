package com.bueno.persistence.dto;

import com.bueno.domain.usecases.bot.dtos.RemoteBotDto;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.domain.usecases.user.dtos.ApplicationUserDto;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "REMOTE_BOT")
public class RemoteBotEntity {
    @Id
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String name;
    private String url;
    private String port;


    public static RemoteBotEntity from(RemoteBotDto bot, UserRepository userRepository) {
        ApplicationUserDto userOpt = Objects.requireNonNull(userRepository.findByUuid(bot.user()).orElse(null),"User not found");
        UserEntity user = UserEntity.from(userOpt);
        return new RemoteBotEntity(bot.uuid(), user, bot.name(), bot.url(), bot.port());
    }

    public static RemoteBotDto toRemoteBotDto(RemoteBotEntity dto) {
        Objects.requireNonNull(dto,"null dto");
        return new RemoteBotDto(dto.id, UserEntity.toApplicationUser(dto.user).uuid(), dto.name, dto.url, dto.port);
    }
}
