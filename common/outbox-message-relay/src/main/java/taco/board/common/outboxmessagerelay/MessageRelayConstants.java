package taco.board.common.outboxmessagerelay;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageRelayConstants {
    public static final int SHARD_COUNT = 4; // 샤드를 4개라고 가정
}