package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {
    private final String name;
    private final ResourceDuration duration;
    private boolean completed = false;

    public Resource(String name, ResourceDuration duration) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank!");
        }
        this.name = name;
        this.duration = duration;
    }

    /**
     * Returns the resource name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the total duration of the resource.
     */
    public ResourceDuration getDuration() { return duration; }

    /**
     * Marks the resource as completed.
     */
    public void complete() {
        completed = true;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public int getCompletionPercentage() {
        return completed ? 100 : 0;
    }
}
