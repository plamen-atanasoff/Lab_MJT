package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FootballPlayerAnalyzerTest {
    private static final String textDefault =
        "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;" +
            "potential;value_euro;wage_euro;preferred_foot" + System.lineSeparator() +
        "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;" +
            "110500000;565000;Left" + System.lineSeparator() +
        "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;" +
            "69500000;205000;Right" + System.lineSeparator() +
        "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;" +
            "88;91;73000000;255000;Right" + System.lineSeparator();

    private static final String textDuplicatingNationalities =
        "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;" +
            "potential;value_euro;wage_euro;preferred_foot" + System.lineSeparator() +
            "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;" +
            "110500000;565000;Left" + System.lineSeparator() +
            "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Argentina;88;89;" +
            "69500000;205000;Right" + System.lineSeparator() +
            "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;" +
            "88;91;73000000;255000;Right" + System.lineSeparator();
    private static FootballPlayerAnalyzer analyzer;

    private static void createObjectDefault(String text) {
        StringReader reader = new StringReader(text);
        analyzer = new FootballPlayerAnalyzer(reader);
    }

    @Test
    void testConstructorCreatesObjectCorrectly() {
        createObjectDefault(textDefault);
        List<Player> players = analyzer.getAllPlayers();

        assertNotEquals(null, players);
        assertEquals(3, players.size());
        assertEquals("L. Messi", players.get(0).name());
        assertEquals("C. Eriksen", players.get(1).name());
        assertEquals("P. Pogba", players.get(2).name());
    }

    @Test
    void testGetAllNationalitiesReturnsCorrectly() {
        createObjectDefault(textDefault);

        Set<String> res = analyzer.getAllNationalities();

        assertEquals(3, res.size());
        assertTrue(res.contains("Denmark"));
        assertTrue(res.contains("France"));
        assertTrue(res.contains("Argentina"));
    }

    @Test
    void testGetAllNationalitiesReturnsEmptySetWhenNoPlayers() {
        createObjectDefault("");

        Set<String> res = analyzer.getAllNationalities();

        assertEquals(0, res.size());
    }

    @Test
    void testGetAllNationalitiesReturnsCorrectSetWhenPlayersHaveSameNationalities() {
        createObjectDefault(textDuplicatingNationalities);

        Set<String> res = analyzer.getAllNationalities();

        assertEquals(2, res.size());
        assertTrue(res.contains("Argentina"));
        assertTrue(res.contains("France"));
    }

    @Test
    void testGetHighestPaidPlayerByNationalityReturnsCorrectly() {
        createObjectDefault(textDuplicatingNationalities);

        Player res = analyzer.getHighestPaidPlayerByNationality("France");

        assertEquals("France", res.nationality());
        assertEquals("P. Pogba", res.name());
    }

    @Test
    void testGetHighestPaidPlayerByNationalityThrowsWhenPassedAbsentNationality() {
        createObjectDefault(textDuplicatingNationalities);

        assertThrows(NoSuchElementException.class, () -> analyzer.getHighestPaidPlayerByNationality("Bulgaria"));
    }

    @Test
    void testGetHighestPaidPlayerByNationalityThrowsWhenPassedNull() {
        createObjectDefault(textDuplicatingNationalities);

        assertThrows(IllegalArgumentException.class, () -> analyzer.getHighestPaidPlayerByNationality(null));
    }

    @Test
    void test() {
        createObjectDefault(textDefault);

        Map<Position, Set<Player>> res =  analyzer.groupByPosition();
    }
}
