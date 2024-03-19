package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase {

    public EducationalAccount(String username, double balance) {
        super(username, balance);
    }

    @Override
    public void buyCourse(Course course)
            throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        double discountPrice = course.getPrice();
        if (coursesCount > 0 && coursesCount % 5 == 0) {
            double sum = 0.0;
            boolean eligibleForDiscount = true;
            for (int i = coursesCount - 5; i < coursesCount; ++i) {
                if (!courses[i].isCompleted()) {
                    eligibleForDiscount = false;
                    break;
                }
                sum += grades[i];
            }
            double average = sum / 5;
            if (eligibleForDiscount && (average > 4.50 || Math.abs(average - 4.50) < 0.001)) {
                discountPrice *= (1 - getType().getDiscount());
            }
        }
        super.buyCourseBase(course, discountPrice);
    }

    @Override
    protected AccountType getType() {
        return AccountType.EDUCATION;
    }
}
