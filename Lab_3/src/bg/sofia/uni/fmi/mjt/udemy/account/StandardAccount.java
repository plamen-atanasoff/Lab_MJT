package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class StandardAccount extends AccountBase {
    public StandardAccount(String username, double balance) {
        super(username, balance);
    }

    @Override
    public void buyCourse(Course course)
            throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        super.buyCourseBase(course, course.getPrice() * (1 - getType().getDiscount()));
    }

    @Override
    protected AccountType getType() {
        return AccountType.STANDARD;
    }
}
