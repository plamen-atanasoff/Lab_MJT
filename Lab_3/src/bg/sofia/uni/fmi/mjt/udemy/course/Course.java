package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {
    private final String name;
    private final String description;
    private final double price;
    private final Resource[] content;
    private final Category category;
    private final CourseDuration totalTime;
    private boolean purchased = false;

    public Course(String name, String description, double price, Resource[] content, Category category) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank!");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank!");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price is negative or zero!");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content is null!");
        }

        this.name = name;
        this.description = description;
        this.price = price;
        this.content = new Resource[content.length];
        System.arraycopy(content, 0, this.content, 0,  content.length);
        this.category = category;
        totalTime = CourseDuration.of(content);
    }

    /**
     * Returns the name of the course.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the price of the course.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of the course.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the content of the course.
     */
    public Resource[] getContent() {
        return content;
    }

    /**
     * Returns the total duration of the course.
     */
    public CourseDuration getTotalTime() {
        return totalTime;
    }

    /**
     * Completes a resource from the course.
     *
     * @param resourceToComplete the resource which will be completed.
     * @throws IllegalArgumentException if resourceToComplete is null.
     * @throws ResourceNotFoundException if the resource could not be found in the course.
     */
    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        if (resourceToComplete == null) {
            throw new IllegalArgumentException("ResourceToComplete is null!");
        }

        for (Resource resource: content) {
            if (resource.getName().equals(resourceToComplete.getName())) {
                resource.complete();
                return;
            }
        }

        throw new ResourceNotFoundException("ResourceToComplete was not found in content!");
    }

    @Override
    public boolean isCompleted() {
        for (Resource resource : content) {
            if (!resource.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getCompletionPercentage() {
        if (content.length == 0) {
            return 100;
        }
        int res = 0;
        for (Resource resource : content) {
            if (resource.isCompleted()) {
                res++;
            }
        }
        return (int)Math.round((double)res / content.length * 100);
    }

    @Override
    public void purchase() {
        purchased = true;
    }

    @Override
    public boolean isPurchased() {
        return purchased;
    }
}
