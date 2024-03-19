package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public abstract class AccountBase implements Account {
    private static final int MAX_COURSES = 100;
    private final String username;
    private double balance;
    protected Course[] courses = new Course[100];
    protected int coursesCount = 0;
    protected double[] grades = new double[100];

    public AccountBase(String username, double balance) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username can't be null or blank!");
        }
        if (balance <= 0.0) {
            throw new IllegalArgumentException("Balance can't be less than or equal to 0!");
        }
        this.username = username;
        this.balance = balance;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addToBalance(double amount) {
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount has a negative value!");
        }
        balance += amount;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    protected void buyCourseBase(Course course, double discountPrice)
            throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (coursesCount == MAX_COURSES) {
            throw new MaxCourseCapacityReachedException("Max capacity for courses by user reached!");
        }
        for (int i = 0; i < coursesCount; ++i) {
            if (courses[i].getName().equals(course.getName())) {
                throw new CourseAlreadyPurchasedException("Course is already purchased!");
            }
        }
        if (discountPrice > balance) {
            throw new InsufficientBalanceException("User does not have enough funds!");
        }

        courses[coursesCount++] = course;
        balance -= discountPrice;
    }

    @Override
    public abstract void buyCourse(Course course)
            throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException;

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete)
            throws CourseNotPurchasedException, ResourceNotFoundException {
        if (course == null || resourcesToComplete == null) {
            throw new IllegalArgumentException("Course or resourcesToComplete is null!");
        }
        validateCourseIsPurchased(course);

        for (Resource resource : resourcesToComplete) {
            course.completeResource(resource);
        }
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (course == null || grade < 2.00 || grade > 6.00) {
            throw new IllegalArgumentException("Course is null or grade is not valid!");
        }
        validateCourseIsPurchased(course);
        for (int i = 0; i < coursesCount; ++i) {
            if (!courses[i].isCompleted()) {
                throw new CourseNotCompletedException("Course is not completed!");
            }
        }
        for (int i = 0; i < coursesCount; ++i) {
            if (course.getName().equals(courses[i].getName())) {
                grades[i] = grade;
            }
        }
    }

    @Override
    public Course getLeastCompletedCourse() {
        if (coursesCount == 0) {
            return null;
        }
        Course leastCompletedCourse = courses[0];
        for (int i = 1; i < coursesCount; ++i) {
            if (courses[i].getCompletionPercentage() < courses[i - 1].getCompletionPercentage()) {
                leastCompletedCourse = courses[i];
            }
        }
        return leastCompletedCourse;
    }

    protected abstract AccountType getType();

    private void validateCourseIsPurchased(Course course) throws CourseNotPurchasedException {
        boolean purchased = false;
        for (int i = 0; i < coursesCount; ++i) {
            if (course.getName().equals(courses[i].getName())) {
                purchased = true;
                break;
            }
        }
        if (!purchased) {
            throw new CourseNotPurchasedException("Course is not purchased!");
        }
    }
}
