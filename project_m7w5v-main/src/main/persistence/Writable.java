package persistence;

import org.json.JSONObject;

public interface Writable {

    //effects returns class as JSON object
    JSONObject toJson();
}
