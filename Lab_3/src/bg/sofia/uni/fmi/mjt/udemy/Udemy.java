package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform {
    private final Account[] accounts;
    private final Course[] courses;

    public Udemy(Account[] accounts, Course[] courses) {
        if (accounts == null) {
            throw new IllegalArgumentException("Accounts is null!");
        }
        if (courses == null) {
            throw new IllegalArgumentException("Courses is null!");
        }
        this.accounts = new Account[accounts.length];
        System.arraycopy(accounts, 0, this.accounts, 0, accounts.length);
        this.courses = new Course[courses.length];
        System.arraycopy(courses, 0, this.courses, 0, courses.length);
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank!");
        }
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        throw new CourseNotFoundException("Course with name " + name + " not found!");
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank() || !keyword.matches("^[A-Za-z]+$")) {
            throw new IllegalArgumentException("Keyword is null or blank!");
        }

        int counter = 0;
        for (Course course : courses) {
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                counter++;
            }
        }

        Course[] filteredCourses = new Course[counter];
        int ind = 0;
        for (Course course : courses) {
            if (counter == 0) {
                break;
            }
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                filteredCourses[ind++] = course;
                counter--;
            }
        }

        return filteredCourses;
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is null!");
        }
        int counter = 0;
        for (Course course : courses) {
            if (course.getCategory() == category) {
                counter++;
            }
        }
        Course[] allCoursesFromCategory = new Course[counter];
        counter = 0;
        for (Course course : courses) {
            if (course.getCategory().equals(category)) {
                allCoursesFromCategory[counter++] = course;
            }
        }
        return allCoursesFromCategory;
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is null or blank!");
        }
        for (Account account : accounts) {
            if (account.getUsername().equals(name)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Account was not found!");
    }

    @Override
    public Course getLongestCourse() {
        if (courses == null) {
            return null;
        }
        Course courseWithLongestDuration = courses[0];
        for (int i = 1; i < courses.length; ++i) {
            if (courses[i].getTotalTime().getDurationInMinutes() >
                    courses[i - 1].getTotalTime().getDurationInMinutes()) {
                courseWithLongestDuration = courses[i];
            }
        }
        return courseWithLongestDuration;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        if (courses == null) {
            return null;
        }
        if (category == null) {
            throw new IllegalArgumentException("Category is null!");
        }
        Course cheapestCourse = courses[0];
        for (int i = 1; i < courses.length; ++i) {
            if (courses[i].getPrice() < courses[i - 1].getPrice()) {
                cheapestCourse = courses[i];
            }
        }
        return cheapestCourse;
    }
}
