package hu.me.iit.malus.thesis.task.model;

import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TaskTest {

    @Test
    public void addStudentIdToHelp() {
        var task = new Task();
        task.setHelpNeededStudentIds(new HashSet<>());

        String id1 = "IfTi";
        String id2 = "58IWrdAM";
        String id3 = "Gh0TG8";
        task.addStudentIdToHelp(id1);
        task.addStudentIdToHelp(id2);
        task.addStudentIdToHelp(id3);

        assertThat(task.getHelpNeededStudentIds().size(), is(3));
        assertTrue(task.getHelpNeededStudentIds().contains(id1));
        assertTrue(task.getHelpNeededStudentIds().contains(id2));
        assertTrue(task.getHelpNeededStudentIds().contains(id3));
    }

    @Test
    public void addStudentIdToCompleted() {
        var task = new Task();
        task.setCompletedStudentIds(new HashSet<>());

        String id1 = "vkTA2";
        String id2 = "YBM";
        String id3 = "yUW8I";
        task.addStudentIdToCompleted(id1);
        task.addStudentIdToCompleted(id2);
        task.addStudentIdToCompleted(id3);

        assertThat(task.getCompletedStudentIds().size(), is(3));
        assertTrue(task.getCompletedStudentIds().contains(id1));
        assertTrue(task.getCompletedStudentIds().contains(id2));
        assertTrue(task.getCompletedStudentIds().contains(id3));
    }
}