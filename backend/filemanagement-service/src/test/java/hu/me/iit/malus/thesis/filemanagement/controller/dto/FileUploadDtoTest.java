package src.test.java.hu.me.iit.malus.thesis.filemanagement.controller.dto;

import hu.me.iit.malus.thesis.filemanagement.controller.dto.FileUploadDto;
import hu.me.iit.malus.thesis.filemanagement.model.ServiceType;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.util.Date;
import java.util.List;
import org.junit.Test;

public class FileUploadDtoTest {
	
	@Test
	public void testSets() {
		//GIVEN
			FileUploadDto dto = new FileUploadDto();
		//WHEN
			dto.setServiceType(ServiceType.COURSE);
			dto.setTagId(1L);
		//THEN
			assertEquals(ServiceType.COURSE, dto.getServiceType());
			assertEquals((Long) 1L, dto.getTagId());
	}
	
	@Test
	public void testEquals() {
		//GIVEN
			FileUploadDto dto1 = new FileUploadDto();
			FileUploadDto dto2 = new FileUploadDto();
		//WHEN
			dto2.setServiceType(ServiceType.COURSE);
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
			FileUploadDto dto = new FileUploadDto();
		//WHEN
			int hashCode = dto.hashCode();
		//THEN
			assertNotEquals(null, hashCode);
	}
}
