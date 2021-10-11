package hu.me.iit.malus.thesis.task.model;

import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class TaskTest {

    @Test
    public void toggleHelpNeededStudent() {
        var task = new Task();
        task.setHelpNeededStudentIds(new HashSet<>());

        String id1 = "IfTi";
        String id2 = "58IWrdAM";
        task.toggleHelpNeededStudent(id1);
        task.toggleHelpNeededStudent(id2);
        task.toggleHelpNeededStudent(id1);

        assertThat(task.getHelpNeededStudentIds().size(), is(1));
        assertFalse(task.getHelpNeededStudentIds().contains(id1));
        assertTrue(task.getHelpNeededStudentIds().contains(id2));
    }

    @Test
    public void toggleCompletedStudent() {
        var task = new Task();
        task.setCompletedStudentIds(new HashSet<>());

        String id1 = "vkTA2";
        String id2 = "YBM";
        task.toggleCompletedStudent(id1);
        task.toggleCompletedStudent(id2);
        task.toggleCompletedStudent(id1);

        assertThat(task.getCompletedStudentIds().size(), is(1));
        assertFalse(task.getCompletedStudentIds().contains(id1));
        assertTrue(task.getCompletedStudentIds().contains(id2));
    }
}