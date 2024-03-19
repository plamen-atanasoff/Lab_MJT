package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    void testOfConstructsObjectCorrectly() {
        String line = "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;" +
                      "CF,RW,ST;Argentina;94;94;110500000;565000;Left";

        Player res = Player.of(line);

        assertEquals("L. Messi", res.name());
        assertEquals("Lionel Andrés Messi Cuccittini", res.fullName());
        assertEquals("1987-06-24", res.birthDate().toString());
        assertEquals(31, res.age());
        assertEquals(170.18, res.heightCm());
        assertEquals(72.1, res.weightKg());
        assertEquals("CF,RW,ST", res.positions().stream()
            .map(Position::name)
            .collect(Collectors.joining(",")));
        assertEquals("Argentina", res.nationality());
        assertEquals(94, res.overallRating());
        assertEquals(94, res.potential());
        assertEquals(110500000, res.valueEuro());
        assertEquals(565000, res.wageEuro());
        assertEquals("LEFT", res.preferredFoot().toString());
    }
}
