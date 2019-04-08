package server.api.pointdb;

import java.io.IOException;

import javax.servlet.ServletOutputStream;

import org.eclipse.jetty.server.Response;

import pointdb.PointDB;
import pointdb.base.GeoPoint;
import pointdb.processing.geopoint.RasterSubGrid;
import util.ByteArrayOut;
import util.collections.vec.Vec;

public class JsWriter {
	//private static final Logger log = LogManager.getLogger();

	public static void writePoints(PointDB pointdb, Response response, Vec<GeoPoint> result, String[] columns) throws IOException {

		int n = result.size();
		ByteArrayOut out = ByteArrayOut.of(4+n*3*4+n);

		out.putIntRaw(n);
		for(GeoPoint p:result) {
			out.putFloatRaw(p.x);
			out.putFloatRaw(p.z);
			out.putFloatRaw(p.y);
		}

		for(GeoPoint p:result) {
			out.putByteRaw(p.classification);
		}

		response.setContentType("application/octet-stream");
		ServletOutputStream stream = response.getOutputStream();
		out.flip(stream);
	}

	public static void writeRaster(Response response, RasterSubGrid rasterGrid) throws IOException {
		double[][] data = rasterGrid.data;
		int xStart = rasterGrid.start_x;
		int yStart = rasterGrid.start_y;
		int xBorder = rasterGrid.border_x;
		int yBorder = rasterGrid.border_y;
		int xLen = xBorder - xStart;
		int yLen = yBorder - yStart;
		ByteArrayOut out = ByteArrayOut.of(xLen*yLen*4+2*4);
		out.putIntRaw(xLen);
		out.putIntRaw(yLen);
		out.putFloats2dBorderedRaw(data, yStart, yBorder, xStart, xBorder);
		/*for(int y = yStart; y<yBorder; y++) {
			double[] row = data[y];
			out.writeFloatsBorderedRaw(row, xStart, xBorder);
			//for(int x = xStart; x<xBorder; x++) {
			//	out.writeFloatRaw(row[x]);
			//}
		}*/
		response.setContentType("application/octet-stream");
		ServletOutputStream stream = response.getOutputStream();
		out.flip(stream);
	}

}
