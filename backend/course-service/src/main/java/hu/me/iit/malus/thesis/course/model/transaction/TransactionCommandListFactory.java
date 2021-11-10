package hu.me.iit.malus.thesis.course.model.transaction;

import hu.me.iit.malus.thesis.course.model.Course;

import java.util.List;

public interface TransactionCommandListFactory {
    List<TransactionCommand> create(Course course);
}
