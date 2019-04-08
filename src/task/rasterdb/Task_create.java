package task.rasterdb;

import org.json.JSONObject;

import broker.Broker;
import rasterdb.GeoReference;
import rasterdb.RasterDB;

public class Task_create {
	
	private final Broker broker;
	private final JSONObject task;
	
	public Task_create(Broker broker, JSONObject task) {
		this.broker = broker;
		this.task = task;
	}

	public void run() {
		String name = task.getString("rasterdb");
		
		double pixel_size_x = GeoReference.NO_PIXEL_SIZE;
		double pixel_size_y = GeoReference.NO_PIXEL_SIZE;
		double offset_x = GeoReference.NO_OFFSET;
		double offset_y = GeoReference.NO_OFFSET;		

		JSONObject pixel_size_obj = task.optJSONObject("pixel_size");
		if(pixel_size_obj != null) {
			double x = pixel_size_obj.optDouble("x");
			if(Double.isFinite(x)) {
				pixel_size_x = x;
			}
			double y = pixel_size_obj.optDouble("y");
			if(Double.isFinite(y)) {
				pixel_size_y = y;
			}
		}
		double pixel_size = task.optDouble("pixel_size");
		if(Double.isFinite(pixel_size)) {
			pixel_size_x = pixel_size;
			pixel_size_y = pixel_size;
		}
		
		JSONObject offset_obj = task.optJSONObject("offset");
		if(offset_obj != null) {
			double x = offset_obj.optDouble("x");
			if(Double.isFinite(x)) {
				offset_x = x;
			}
			double y = offset_obj.optDouble("y");
			if(Double.isFinite(y)) {
				offset_y = y;
			}
		}
		
		RasterDB rasterdb = broker.createRasterdb(name);
		rasterdb.setPixelSize(pixel_size_x, pixel_size_y, offset_x, offset_y);
		
		String code = task.optString("code");
		if(code != null) {
			rasterdb.setCode(code);
		}
		
		String proj4 = task.optString("proj4");
		if(proj4 != null) {
			rasterdb.setProj4(proj4);
		}
		
	}

}
