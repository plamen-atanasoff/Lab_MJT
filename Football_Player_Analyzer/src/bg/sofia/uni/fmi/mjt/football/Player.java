package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm,
                     double weightKg, List<Position> positions, String nationality, int overallRating,
                     int potential, long valueEuro, long wageEuro, Foot preferredFoot) {
    private static final int NAME_POS = 0;
    private static final int FULL_NAME_POS = 1;
    private static final int BIRTH_DATE_POS = 2;
    private static final int AGE_POS = 3;
    private static final int HEIGHT_CM_POS = 4;
    private static final int WEIGHT_KG_POS = 5;
    private static final int POSITIONS_POS = 6;
    private static final int NATIONALITY_POS = 7;
    private static final int OVERALL_RATING_POS = 8;
    private static final int POTENTIAL_POS = 9;
    private static final int VALUE_EURO_POS = 10;
    private static final int WAGE_EURO_POS = 11;
    private static final int PREFERRED_FOOT_POS = 12;

    public static Player of(String line) {
        String[] words = line.split(";");

        StringJoiner correctFormatDate = getCorrectFormatDate(words[BIRTH_DATE_POS].split("/"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        return new Player(
            words[NAME_POS],
            words[FULL_NAME_POS],
            LocalDate.parse(correctFormatDate.toString(), formatter),
            Integer.parseInt(words[AGE_POS]),
            Double.parseDouble(words[HEIGHT_CM_POS]),
            Double.parseDouble(words[WEIGHT_KG_POS]),
            Stream.of(words[POSITIONS_POS].split(",")).map(Position::valueOf).toList(),
            words[NATIONALITY_POS],
            Integer.parseInt(words[OVERALL_RATING_POS]),
            Integer.parseInt(words[POTENTIAL_POS]),
            Long.parseLong(words[VALUE_EURO_POS]),
            Long.parseLong(words[WAGE_EURO_POS]),
            Foot.valueOf(words[PREFERRED_FOOT_POS].toUpperCase()));
    }

    private static StringJoiner getCorrectFormatDate(String[] dateTokens) {
        StringJoiner date = new StringJoiner("-");

        for (int i = 0; i <= 1; i++) {
            if (dateTokens[i].length() != 2) {
                date.add("0" + dateTokens[i]);
            } else {
                date.add(dateTokens[i]);
            }
        }
        date.add(dateTokens[2]);

        return date;
    }

}
