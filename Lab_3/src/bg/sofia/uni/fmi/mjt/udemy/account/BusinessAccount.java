package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class BusinessAccount extends AccountBase {
    private final Category[] allowedCategories;

    public BusinessAccount(String username, double balance, Category[] allowedCategories) {
        super(username, balance);
        if (allowedCategories == null) {
            throw new IllegalArgumentException("AllowedCategories is null!");
        }
        this.allowedCategories = new Category[allowedCategories.length];
        System.arraycopy(allowedCategories, 0, this.allowedCategories, 0, allowedCategories.length);
    }

    @Override
    protected AccountType getType() {
        return AccountType.BUSINESS;
    }

    @Override
    public void buyCourse(Course course)
            throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        boolean exists = false;
        for (Category category : allowedCategories) {
            if (category == course.getCategory()) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new IllegalArgumentException("Course does not exist in allowed categories!");
        }

        double discountPrice = (1 - getType().getDiscount()) * course.getPrice();
        super.buyCourseBase(course, discountPrice);
    }

}
