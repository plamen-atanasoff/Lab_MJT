package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {

    public CourseDuration {
        if (hours < 0 || hours >= 24) {
            throw new IllegalArgumentException("Hours is not in the range [0, 24)!");
        }
        if (minutes < 0 || minutes >= 60) {
            throw new IllegalArgumentException("Minutes is not in the range [0, 60)!");
        }
    }
    public static CourseDuration of(Resource[] content) {
        int sum = 0;
        for (Resource resource: content) {
            sum += resource.getDuration().minutes();
        }
        return new CourseDuration(sum / 60, sum % 60);
    }

    public int getDurationInMinutes() {
        return hours * 60 + minutes;
    }
}
