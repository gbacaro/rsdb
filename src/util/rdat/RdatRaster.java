package util.rdat;

import java.io.DataOutput;
import java.io.IOException;

import org.eclipse.jetty.server.Response;
import org.mapdb.DataIO.DataOutputByteArray;

import pointdb.processing.geopoint.RasterSubGrid;
import util.Serialisation;

public class RdatRaster {

	public static void write_RDAT_RASTER(DataOutput out, RasterSubGrid rasterSubGrid, String proj4) throws IOException {
		write_RDAT_RASTER(out, new RasterSubGrid[]{rasterSubGrid}, proj4);
	}

	public static void write_RDAT_RASTER(DataOutput out, RasterSubGrid[] rasterSubGrids, String proj4) throws IOException {
		out.write(Rdat.SIGNATURE_RDAT);
		out.write(Rdat.RDAT_TYPE_RASTER);

		RasterSubGrid firstGrid = rasterSubGrids[0];

		RdatList list = new RdatList();
		list.addInteger("xmn", firstGrid.local_min_x);
		list.addInteger("ymn", firstGrid.local_min_y);
		list.addInteger("xmx", firstGrid.local_max_x);
		list.addInteger("ymx", firstGrid.local_max_y);
		list.addString("proj4", proj4);
		list.write(out);

		out.writeByte(Rdat.TYPE_FLOAT64);
		out.writeByte(Rdat.TYPE_FLOAT64_SIZE);
		out.writeInt(rasterSubGrids.length); // layers
		out.writeInt(firstGrid.range_y);
		out.writeInt(firstGrid.range_x);
		for(RasterSubGrid raster:rasterSubGrids) {
			RdatList rasterMeta = new RdatList();
			rasterMeta.addAll(raster.meta);
			rasterMeta.write(out);
			raster.writeRawGrid(out);
		}
	}

	public static void write_RDAT_RASTER(DataOutput out, short[][][] rasters, RdatList rastersMeta, RdatList[] rasterBandMeta) throws IOException {
		out.write(Rdat.SIGNATURE_RDAT);
		out.write(Rdat.RDAT_TYPE_RASTER);
		rastersMeta.write(out);
		out.writeByte(Rdat.TYPE_UINT16);
		out.writeByte(Rdat.TYPE_UINT16_SIZE);
		int len = rasters.length;
		int height = rasters[0].length;
		int width = rasters[0][0].length;
		out.writeInt(len); // raster count
		out.writeInt(height); // height
		out.writeInt(width); // width		
		for(int i = 0 ; i < len; i++) {
			rasterBandMeta[i].write(out);	
			Rdat.writeShortArrayArray(out, rasters[i]);
		}
	}
	
	public static void write_RDAT_RASTER(DataOutput out, double[][][] rasters, RdatList rastersMeta, RdatList[] rasterBandMeta) throws IOException {
		out.write(Rdat.SIGNATURE_RDAT);
		out.write(Rdat.RDAT_TYPE_RASTER);
		rastersMeta.write(out);
		out.writeByte(Rdat.TYPE_FLOAT64);
		out.writeByte(Rdat.TYPE_FLOAT64_SIZE);
		int len = rasters.length;
		int height = rasters[0].length;
		int width = rasters[0][0].length;
		out.writeInt(len); // raster count
		out.writeInt(height); // height
		out.writeInt(width); // width		
		for(int i = 0 ; i < len; i++) {
			rasterBandMeta[i].write(out);
			Serialisation.writeArrayArrayBE(out, rasters[i]);
		}
	}
	
	public static void write_RDAT_RASTER(Response response, RasterSubGrid[] rasterGrids, String proj4) throws IOException {
		DataOutputByteArray out = new DataOutputByteArray();
		write_RDAT_RASTER(out, rasterGrids, proj4);		
		response.setContentType("application/octet-stream");
		response.getOutputStream().write(out.buf,0,out.pos);	
	}

	public static void write_RDAT_RASTER(Response response, RasterSubGrid rasterGrid, String proj4) throws IOException {
		DataOutputByteArray out = new DataOutputByteArray();
		write_RDAT_RASTER(out, rasterGrid, proj4);		
		response.setContentType("application/octet-stream");
		response.getOutputStream().write(out.buf,0,out.pos);		
	}
}
