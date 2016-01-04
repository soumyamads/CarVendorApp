package com.car.vendor.api;

/**
 * Created by AMAN on 25-10-2015.
 */
public class Parser  {
    /*public static ArrayList<StatusPojo> parseJSON(String string) {
        ArrayList<StatusPojo> listMovies = new ArrayList<>();

        try {
            JSONObject response = new JSONObject(string);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (response != null && response.length() > 0) {
                try {
                    JSONArray array = response.getJSONArray(KEY_MEMBERS);

                    for (int i = 0; i < array.length(); i++) {
                        long id = -1;
                        String dob = Constants.NA;
                        String status = Constants.NA;
                        int ethnicity = 0;
                        int weight = 0;
                        int height = 0;
                        String is_veg = Constants.NA;
                        String drink = Constants.NA;
                        String image = Constants.NA;

                        JSONObject json = array.getJSONObject(i);

                        if (Utils.contains(json, KEY_ID)) {
                            id = json.getLong(KEY_ID);
                        }

                        if (Utils.contains(json, KEY_DATES)) {
                            dob = json.getString(KEY_DATES);
                        }
                        if (Utils.contains(json, KEY_STATUS)) {
                            status = json.getString(KEY_STATUS);
                        }

                        if (Utils.contains(json, KEY_ETHNICITY)) {
                            ethnicity = json.getInt(KEY_ETHNICITY);
                        }

                        if (Utils.contains(json, KEY_WEIGHT)) {
                            weight = json.getInt(KEY_WEIGHT);
                        }


                        if (Utils.contains(json, KEY_HEIGHT)) {
                            height = json.getInt(KEY_HEIGHT);
                        }

                        if (Utils.contains(json, KEY_IS_VEG)) {
                            is_veg = json.getString(KEY_IS_VEG);
                        }

                        if (Utils.contains(json, KEY_DRINK)) {
                            drink = json.getString(KEY_DRINK);
                        }

                        if (Utils.contains(json, KEY_IMAGE)) {
                            image = json.getString(KEY_IMAGE);
                        }

                        StatusPojo pojo = new StatusPojo();
                        pojo.setKEY_ID(id);
                        pojo.setKEY_STATUS(status);
                        Date date = null;
                        try {
                            date = dateFormat.parse(dob);
                        } catch (ParseException e) {
                            //a parse exception generated here will store null in the release date, be sure to handle it
                        }
                        pojo.setKEY_DATES(date);
                        pojo.setKEY_ETHNICITY(ethnicity);
                        pojo.setKEY_WEIGHT(weight);
                        pojo.setKEY_HEIGHT(height);
                        pojo.setKEY_IS_VEG(is_veg);
                        pojo.setKEY_DRINK(drink);
                        pojo.setKEY_IMAGE(image);
                        if (id != -1 && ethnicity != 0) {
                            listMovies.add(pojo);
                        }
                    }
                    Log.e("array", listMovies.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                L.t(getActivity(), listMovies.size() + " rows fetched");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return listMovies;
    }
*/

}
