package config;

import static org.junit.Assert.assertEquals;
import mestrado.arquitetura.parser.ReaderConfig;

import org.junit.Test;

public class ReaderConfiTest {
	
	@Test
	public void shouldReturnPathToDirectorySaveModels(){
		String dir = ReaderConfig.getDirTarget();
		assertEquals("manipulation/", dir);
	}
	
	@Test
	public void shouldReturnPathToDirectoryExportModels(){
		String dir = ReaderConfig.getDirExportTarget();
		assertEquals("/Users/edipofederle/Documents/modelingParaEscrita/TesteVisualizacao/", dir);
	}
}