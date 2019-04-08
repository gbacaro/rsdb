package server.api.pointdb.feature;

import org.json.JSONWriter;

import pointdb.PointDB;
import pointdb.base.Rect;
import pointdb.processing.tilepoint.Counter;
import pointdb.processing.tilepoint.PointFilter;

public class Feature_pulse_density extends Feature {

	@Override
	public void calc(JSONWriter json, PointDB db, Rect rect) {
		
		
		Counter counter = new Counter(db.tilePointProducer(rect).filter(PointFilter.createAtomicFilter("last_return=1")));
		double value = counter.count()/rect.getArea();
		
		json.object();
		json.key("pulse_density");
		json.value(value);
		json.endObject();		
	}

}
