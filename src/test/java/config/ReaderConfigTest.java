package config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import arquitetura.io.ReaderConfig;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ReaderConfig.class )
public class ReaderConfigTest {
	
	@Test
	public void shouldReturnPathToDirectorySaveModels(){
		String dir = ReaderConfig.getDirTarget();
		assertEquals("manipulation/", dir);
	}
	
	@Test
	public void shouldDirectorySaveModelsExists(){
		File dirSaveModels = new File(ReaderConfig.getDirTarget());
		assertTrue(dirSaveModels.exists());
		assertTrue(dirSaveModels.isDirectory());
	}
	
	@Test
	public void shouldReturnPathToDirectoryExportModels(){
		String dir = ReaderConfig.getDirExportTarget();
		assertNotNull(dir);
	}
	
	@Test
	public void shouldDirectoryExportModelsExists(){
		File exportModels = new File(ReaderConfig.getDirExportTarget());
		assertTrue(exportModels.exists());
		assertTrue(exportModels.isDirectory());
	}
	
	@Test 
	public void shouldReturnPathToProfile(){
		String path = ReaderConfig.getPathToProfileSMarty();
		assertNotNull(path);
	}
	
	@Test
	public void shouldProfileFileExistsOnConfiguredPath(){
		File profile = new File(ReaderConfig.getPathToProfileSMarty());
		assertTrue(profile.exists());
	}
	
	@Test
	public void semPerfilSetado(){
		PowerMockito.mockStatic(ReaderConfig.class);
		PowerMockito.when(ReaderConfig.getPathToProfileSMarty()).thenReturn(null);
		PowerMockito.when(ReaderConfig.getPathToProfileConcerns()).thenReturn(null);
		assertFalse(ReaderConfig.hasProfilesSeted());
	}
	
	@Test
	public void comPerfilSetado(){
		assertTrue(ReaderConfig.hasProfilesSeted());
	}
	
	@Test
	public void getPathToTemplateModelsDirectory(){
		String path = ReaderConfig.getPathToTemplateModelsDirectory();
		assertNotNull(path);
	}
	
}