package org.trylma.common.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    void testPlayerEquality() {
        Player player1 = new Player("1", "Alice", Color.RED);
        Player player2 = new Player("1", "Alice", Color.RED);
        assertEquals(player1, player2);
    }

    @Test
    void testPlayerHashCode() {
        Player player1 = new Player("1", "Alice", Color.RED);
        Player player2 = new Player("1", "Alice", Color.RED);
        assertEquals(player1.hashCode(), player2.hashCode());
    }
}