package src.test.java.hu.me.iit.malus.thesis.filemanagement.controller.dto;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileDescriptorDto;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import java.util.Date;
import org.junit.Test;

public class FileDescriptorTest {
	
	@Test
	public void testEquals() {
		//GIVEN
			FileDescriptorDto dto1 = new FileDescriptorDto();
			FileDescriptorDto dto2 = new FileDescriptorDto();
			dto2.setName("testName");
		//WHEN
			
		//THEN
			assertTrue(dto1.equals(dto1));
			assertFalse(dto1.equals(null));
			assertTrue(dto2.equals(dto2));
			assertFalse(dto2.equals(null));
			assertFalse(dto1.equals(dto2));
			assertFalse(dto2.equals(dto1));
	}
	
	@Test
	public void testHashCode() {
		//GIVEN
			FileDescriptorDto dto = new FileDescriptorDto();
		//WHEN
			int hashCode = dto.hashCode();
		//THEN
			assertNotEquals(null, hashCode);
	}
}
