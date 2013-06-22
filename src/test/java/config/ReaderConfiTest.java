package config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import arquitetura.io.ReaderConfig;

public class ReaderConfiTest {
	
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
		assertEquals("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/", dir);
	}
	
	@Test
	public void shouldDirectoryExportModelsExists(){
		File exportModels = new File(ReaderConfig.getDirExportTarget());
		assertTrue(exportModels.exists());
		assertTrue(exportModels.isDirectory());
	}
	
	@Test @Ignore //TODO Stub configure file
	public void shouldReturnPathToProfile(){
		String path = ReaderConfig.getPathToProfileSMarty();
		assertEquals("resources/smarty.profile.uml", path);
	}
	
	@Test
	public void shouldProfileFileExistsOnConfiguredPath(){
		File profile = new File(ReaderConfig.getPathToProfileSMarty());
		assertTrue(profile.exists());
	}
	
	@Test @Ignore //TODO Stub configure file
	public void shouldReturnsPathToProfileConcerns(){
		String path = ReaderConfig.getPathToProfileConcerns();
		assertEquals("resources/perfilConcerns.profile.uml", path);
	}
}