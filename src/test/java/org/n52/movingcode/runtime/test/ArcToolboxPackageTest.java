package org.n52.movingcode.runtime.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Test;
import org.n52.movingcode.runtime.codepackage.MovingCodePackage;
import org.n52.movingcode.runtime.iodata.IIOParameter;
import org.n52.movingcode.runtime.iodata.MediaData;
import org.n52.movingcode.runtime.processors.AbstractProcessor;
import org.n52.movingcode.runtime.processors.ProcessorFactory;

public class ArcToolboxPackageTest {
	
	private static final String packageFileName = "src/test/resources/testpackages/arcgis_ndvi_tbx.zip";
	
	private static final String nir_data = "src/test/resources/testpackages/arctoolbox_ndvi/testdata/nir.tif";
	private static final String nir_id = "NIR";
	private static final int nir_position = 1;
	
	private static final String red_data = "src/test/resources/testpackages/arctoolbox_ndvi/testdata/red.tif";
	private static final String red_id = "RED";
	private static final int red_position = 2;
	
	
	private static final String ndvi_id = "NDVI";
	private static final int ndvi_position = 3;
	private static final String mimeType = "application/geotiff";
	
	
	
	@Test
	public void loadNDVIPackage() {

		// Arrange
		File packageFile = new File(packageFileName);
		System.out.println(packageFile.getAbsolutePath());

		// Act
		MovingCodePackage mcPackage = new MovingCodePackage(packageFile);
		
		// Assert
		Assert.assertTrue(mcPackage.isValid());
		Assert.assertTrue(new File(nir_data).exists());
		Assert.assertTrue(new File(red_data).exists());
		Assert.assertTrue(ProcessorFactory.getInstance().newProcessor(mcPackage) != null);
	}
	
	@Test
	public void executeNDVIPackage() throws IllegalArgumentException, FileNotFoundException {
		
		BasicConfigurator.configure();
		
		// Arrange
		File packageFile = new File(packageFileName);
		System.out.println(packageFile.getAbsolutePath());

		// Act
		MovingCodePackage mcPackage = new MovingCodePackage(packageFile);
		AbstractProcessor processor = ProcessorFactory.getInstance().newProcessor(mcPackage); //get a processor
		
		// add NIR
		Assert.assertTrue(processor.addData(new IIOParameter.ParameterID(nir_position), new MediaData(new FileInputStream(new File(nir_data)), mimeType)));
		// add RED
		Assert.assertTrue(processor.addData(new IIOParameter.ParameterID(red_position), new MediaData(new FileInputStream(new File(red_data)), mimeType)));
		// add output (NDVI) declaration
		Assert.assertTrue(processor.addData(new IIOParameter.ParameterID(ndvi_position), new MediaData(null, mimeType)));
		
		
		
		// Assert
		Assert.assertTrue(processor.isFeasible());
		
		boolean succExecute = false;
		try {
			processor.execute(0);
			succExecute = true;
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Assert.assertTrue(succExecute);
		
	}
	


}